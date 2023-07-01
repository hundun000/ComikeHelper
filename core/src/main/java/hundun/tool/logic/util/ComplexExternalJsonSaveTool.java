package hundun.tool.logic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.save.DeskSaveData;
import hundun.tool.logic.data.save.RoomSaveData;

public class ComplexExternalJsonSaveTool implements IComplexExternalHandler<ExternalMainData, ExternalDeskData> {

    private static final String charSet = "UTF-8";
    private final ObjectMapper objectMapper;
    private final TypeReference<HashMap<String,RoomSaveData>> typeRef = new TypeReference<HashMap<String,RoomSaveData>>() {};
    private final String folder;

    private FileHandle mainJsonFileHandle;
    private FileHandle mainImageFolderHandle;
    //@Getter
    //private final Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap = new HashMap<>();
    private FileHandle subRootFolderFileHandle;
    private static final String deskRootFolder = "desks/";
    private static final String deskImageFolder = "images/";
    private static final String mainJsonFileName = "main.json";
    private static final String deskJsonFileName = "desk.json";
    FileHandle defaultCover;
    
    public ComplexExternalJsonSaveTool(String folder) {

        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                ;
        this.folder = folder;

    }

    

    public void lazyInitOnGameCreate(FileHandle defaultCover) {
        subRootFolderFileHandle = Gdx.files.external(folder + "/" + deskRootFolder);
        subRootFolderFileHandle.mkdirs();
        mainJsonFileHandle = Gdx.files.external(folder + "/" + mainJsonFileName);
        mainImageFolderHandle = Gdx.files.external(folder + "/");
        mainImageFolderHandle.mkdirs();
        this.defaultCover = defaultCover;
    }

    @Override
    public void writeMainData(ExternalMainData externalMainData) {
        try {
            String str = objectMapper.writeValueAsString(externalMainData.getRoomSaveDataMap());
            mainJsonFileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExternalMainData readMainData() {
        if (!mainJsonFileHandle.exists()) {
            return null;
        }
        String str = mainJsonFileHandle.readString(charSet);
        
        try {
            HashMap<String,RoomSaveData> roomSaveDataMap = objectMapper.readValue(str, typeRef);
            Map<String, FileHandle> acceptedRoomImageMap = new HashMap<>();
            Map<String, FileHandle> imageRawNameMap = Arrays.stream(mainImageFolderHandle.list())
                    .collect(Collectors.toMap(it -> it.name(), it -> it));
            roomSaveDataMap.keySet().forEach(roomName -> {
                imageRawNameMap.forEach((imageRawName, imageFile) -> {
                    if (imageRawName.startsWith(roomName + ".")) {
                        acceptedRoomImageMap.put(roomName, imageFile);
                    }
                });
            });
            return ExternalMainData.builder()
                    .roomSaveDataMap(roomSaveDataMap)
                    .roomImageMap(acceptedRoomImageMap)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeSubFolderData(String subFolderName, ExternalDeskData runtimeData) {
        writeSubFolderData(subFolderName, runtimeData.getDeskSaveData());
    }

    public void writeSubFolderData(String subFolderName, DeskSaveData deskSaveData) {
        FileHandle subFileHandle = Gdx.files.external(subRootFolderFileHandle.path() + "/" + subFolderName + "/" + deskJsonFileName);
        if (subFileHandle == null) {
            return;
        }
        subFileHandle.parent().mkdirs();
        try {
            String str = objectMapper.writeValueAsString(deskSaveData);
            subFileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExternalDeskData readSubFolderData(String subFolderName) {
        FileHandle subFileHandle = Gdx.files.external(subRootFolderFileHandle.path() + "/" + subFolderName + "/" + deskJsonFileName);
        if (subFileHandle == null || !subFileHandle.exists()) {
            return null;
        }
        String str = subFileHandle.readString(charSet);
        try {
            DeskSaveData deskSaveData = objectMapper.readValue(str, DeskSaveData.class);
            FileHandle imageFolder = Gdx.files.external(subRootFolderFileHandle.path() + "/" + subFolderName + "/" + deskImageFolder);
            ExternalDeskData externalDeskData = ExternalDeskData.Factory.fromFolder(imageFolder, subFolderName, defaultCover);
            externalDeskData.setDeskSaveData(deskSaveData);
            return externalDeskData;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, ExternalDeskData> readAllSubFolderData() {
        return Arrays.stream(subRootFolderFileHandle.list())
            .filter(it -> it.isDirectory())
            .collect(Collectors.toMap(
                it -> it.name(),
                it -> readSubFolderData(it.name())
            ));
    }

    @Override
    public void writeAllSubFolderData(Map<String, ExternalDeskData> saveDataMap) {
        saveDataMap.forEach((k, v) -> writeSubFolderData(k, v));
    }

    public List<String> previewAllUnknownSubFolder(Collection<String> names) {
        return Arrays.stream(subRootFolderFileHandle.list())
                .filter(it -> it.isDirectory() && !names.contains(it.name()))
                .map(FileHandle::name)
                .collect(Collectors.toList());
    }

    public void deleteAllUnknownSubFolder(Collection<String> names) {
        Arrays.stream(subRootFolderFileHandle.list())
                .filter(it -> it.isDirectory() && !names.contains(it.name()))
                .forEach(FileHandle::deleteDirectory);
    }
}

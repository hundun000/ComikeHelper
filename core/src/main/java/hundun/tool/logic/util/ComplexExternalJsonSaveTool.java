package hundun.tool.logic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool.DeskExternalRuntimeData;
import lombok.Getter;
import lombok.Setter;

public class ComplexExternalJsonSaveTool implements IComplexExternalHandler<ExternalMainData, DeskExternalRuntimeData> {

    private static final String charSet = "UTF-8";
    private final ObjectMapper objectMapper;

    private final String folder;

    private FileHandle mainFileHandle;
    //@Getter
    //private final Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap = new HashMap<>();
    private FileHandle baseFolderFileHandle;
    private static final String mainFileName = "main.json";
    private static final String subFileName = "desk.json";
    FileHandle defaultCover;
    @Setter
    @Getter
    public static class DeskExternalRuntimeData {
        DeskSaveData deskSaveData;
        FileHandle coverFileHandle;
        List<FileHandle> imageFileHandles;

        public static DeskExternalRuntimeData forDefault(FileHandle defaultCover, DeskSaveData deskSaveData) {
            DeskExternalRuntimeData result = new DeskExternalRuntimeData();
            result.setDeskSaveData(deskSaveData);
            result.setCoverFileHandle(defaultCover);
            result.setImageFileHandles(new ArrayList<>());
            return result;
        }
    }

    public ComplexExternalJsonSaveTool(String folder) {

        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
        ;
        this.folder = folder;

    }

    public static DeskExternalRuntimeData toExternalRuntimeData(FileHandle imageFolder, String key, FileHandle defaultCover) {
        DeskExternalRuntimeData deskExternalRuntimeData = new DeskExternalRuntimeData();
        deskExternalRuntimeData.setImageFileHandles(new ArrayList<>());
        if (imageFolder.exists()) {
            Arrays.stream(imageFolder.list()).forEach(it -> {
                if (it.name().startsWith("cover.")) {
                    deskExternalRuntimeData.setCoverFileHandle(it);
                } else {
                    deskExternalRuntimeData.getImageFileHandles().add(it);
                }
            });
        } else {
            deskExternalRuntimeData.setCoverFileHandle(defaultCover);
        }
        return deskExternalRuntimeData;
    }

    public void lazyInitOnGameCreate(FileHandle defaultCover) {
        baseFolderFileHandle = Gdx.files.external(folder);
        baseFolderFileHandle.mkdirs();
        mainFileHandle = Gdx.files.external(folder + "/" + mainFileName);
        this.defaultCover = defaultCover;
    }

    @Override
    public void writeMainData(ExternalMainData saveData) {
        try {
            String str = objectMapper.writeValueAsString(saveData);
            mainFileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExternalMainData readMainData() {
        if (!mainFileHandle.exists()) {
            return null;
        }
        String str = mainFileHandle.readString(charSet);
        try {
            return objectMapper.readValue(str, ExternalMainData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeSubFolderData(String subFolderName, DeskExternalRuntimeData runtimeData) {
        writeSubFolderData(subFolderName, runtimeData.getDeskSaveData());
    }

    public void writeSubFolderData(String subFolderName, DeskSaveData deskSaveData) {
        FileHandle subFileHandle = Gdx.files.external(folder + "/" + subFolderName + "/" + subFileName);
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
    public DeskExternalRuntimeData readSubFolderData(String subFolderName) {
        FileHandle subFileHandle = Gdx.files.external(folder + "/" + subFolderName + "/" + subFileName);
        if (subFileHandle == null || !subFileHandle.exists()) {
            return null;
        }
        String str = subFileHandle.readString(charSet);
        try {
            DeskSaveData deskSaveData = objectMapper.readValue(str, DeskSaveData.class);
            FileHandle imageFolder = Gdx.files.external(folder + "/" + subFolderName + "/images/");
            DeskExternalRuntimeData deskExternalRuntimeData = toExternalRuntimeData(imageFolder, subFolderName, defaultCover);
            deskExternalRuntimeData.setDeskSaveData(deskSaveData);
            return deskExternalRuntimeData;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, DeskExternalRuntimeData> readAllSubFolderData() {
        return Arrays.stream(baseFolderFileHandle.list())
            .filter(it -> it.isDirectory())
            .collect(Collectors.toMap(
                it -> it.name(),
                it -> readSubFolderData(it.name())
            ));
    }

    @Override
    public void writeAllSubFolderData(Map<String, DeskExternalRuntimeData> saveDataMap) {
        saveDataMap.forEach((k, v) -> writeSubFolderData(k, v));
    }
}

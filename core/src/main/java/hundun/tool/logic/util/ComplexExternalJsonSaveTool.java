package hundun.tool.logic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.tool.libgdx.screen.ScreenContext.LayoutConst;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.DeskRuntimeData.DeskLocation;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;

public class ComplexExternalJsonSaveTool implements IComplexExternalHandler<ExternalMainData, ExternalDeskData> {

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
    
    public ComplexExternalJsonSaveTool(String folder) {

        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                ;
        this.folder = folder;

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
    public void writeSubFolderData(String subFolderName, ExternalDeskData runtimeData) {
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
    public ExternalDeskData readSubFolderData(String subFolderName) {
        FileHandle subFileHandle = Gdx.files.external(folder + "/" + subFolderName + "/" + subFileName);
        if (subFileHandle == null || !subFileHandle.exists()) {
            return null;
        }
        String str = subFileHandle.readString(charSet);
        try {
            DeskSaveData deskSaveData = objectMapper.readValue(str, DeskSaveData.class);
            FileHandle imageFolder = Gdx.files.external(folder + "/" + subFolderName + "/images/");
            ExternalDeskData externalDeskData = ExternalDeskData.Factory.fromFolder(imageFolder, subFolderName, defaultCover);
            externalDeskData.setDeskSaveData(deskSaveData);
            return externalDeskData;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, ExternalDeskData> readAllSubFolderData() {
        return Arrays.stream(baseFolderFileHandle.list())
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
}

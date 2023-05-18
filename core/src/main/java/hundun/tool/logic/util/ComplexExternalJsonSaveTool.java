package hundun.tool.logic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

public class ComplexExternalJsonSaveTool<T_MAIN, T_SUB> implements IComplexExternalHandler<T_MAIN, T_SUB> {

    private static final String charSet = "UTF-8";
    private final ObjectMapper objectMapper;
    private final Class<T_MAIN> mainClazz;
    private final Class<T_SUB> subClazz;
    private final String folder;
    private final String mainFileName;
    private final String subFileName;
    private FileHandle mainFileHandle;
    //@Getter
    private final Map<String, FileHandle> subFolderFileHandleMap = new HashMap<>();
    private FileHandle baseFolderFileHandle;

    public ComplexExternalJsonSaveTool(String folder,
                                       String mainFileName, Class<T_MAIN> mainClazz,
                                       String subFileName, Class<T_SUB> subClazz
    ) {
        this.mainClazz = mainClazz;
        this.subClazz = subClazz;
        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
        ;
        this.folder = folder;
        this.mainFileName = mainFileName;
        this.subFileName = subFileName;
    }


    @Override
    public void lazyInitOnGameCreate() {
        baseFolderFileHandle = Gdx.files.external(folder);
        baseFolderFileHandle.mkdirs();
        mainFileHandle = Gdx.files.external(folder + "/" + mainFileName);
        update();

    }

    private void update() {
        subFolderFileHandleMap.clear();
        Arrays.stream(baseFolderFileHandle.list())
            .filter(it -> it.isDirectory())
            .forEach(it -> {
                subFolderFileHandleMap.put(
                    it.name(),
                    Gdx.files.external(folder + "/" + it.name() + "/" + subFileName)
                );
            });
    }

    @Override
    public void writeMainData(T_MAIN saveData) {
        try {
            String str = objectMapper.writeValueAsString(saveData);
            mainFileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T_MAIN readMainData() {
        if (!mainFileHandle.exists()) {
            return null;
        }
        String str = mainFileHandle.readString(charSet);
        try {
            return objectMapper.readValue(str, mainClazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeSubFolderData(String subFolderName, T_SUB saveData) {
        FileHandle subFileHandle = Gdx.files.external(folder + "/" + subFolderName + "/" + subFileName);
        if (subFileHandle == null) {
            return;
        }
        subFileHandle.parent().mkdirs();
        try {
            String str = objectMapper.writeValueAsString(saveData);
            subFileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T_SUB readSubFolderData(String subFolderName) {
        FileHandle subFileHandle = subFolderFileHandleMap.get(subFolderName);
        if (subFileHandle == null || !subFileHandle.exists()) {
            return null;
        }
        String str = subFileHandle.readString(charSet);
        try {
            return objectMapper.readValue(str, subClazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, T_SUB> readAllSubFolderData() {
        return subFolderFileHandleMap.keySet().stream()
            .collect(Collectors.toMap(
                it -> it,
                it -> readSubFolderData(it)
            ));
    }

    @Override
    public void writeAllSubFolderData(Map<String, T_SUB> saveDataMap) {
        saveDataMap.forEach((k, v) -> writeSubFolderData(k, v));
        update();
    }
}

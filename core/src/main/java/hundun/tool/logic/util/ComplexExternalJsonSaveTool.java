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

import lombok.Getter;
import lombok.Setter;

public class ComplexExternalJsonSaveTool<T_MAIN, T_SUB> implements IComplexExternalHandler<T_MAIN, T_SUB> {

    private static final String charSet = "UTF-8";
    private final ObjectMapper objectMapper;
    private final Class<T_MAIN> mainClazz;
    private final Class<T_SUB> subClazz;
    private final String folder;
    private final String mainFileName;
    private final String subFileName;
    private FileHandle mainFileHandle;
    @Getter
    private final Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap = new HashMap<>();
    private FileHandle baseFolderFileHandle;

    @Setter
    @Getter
    public static class DeskExternalRuntimeData {
        FileHandle coverFileHandle;
        List<FileHandle> imageFileHandles;

        public static DeskExternalRuntimeData forDefault(FileHandle defaultCover) {
            DeskExternalRuntimeData result = new DeskExternalRuntimeData();
            result.setCoverFileHandle(defaultCover);
            result.setImageFileHandles(new ArrayList<>());
            return result;
        }
    }

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

    public static void putDeskExternalRuntimeData(Map<String, DeskExternalRuntimeData> map, FileHandle imageFolder, String key, FileHandle defaultCover) {
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
        map.put(
            key,
            deskExternalRuntimeData
        );
    }

    public void lazyInitOnGameCreate(FileHandle defaultCover) {
        baseFolderFileHandle = Gdx.files.external(folder);
        baseFolderFileHandle.mkdirs();
        mainFileHandle = Gdx.files.external(folder + "/" + mainFileName);

        Arrays.stream(baseFolderFileHandle.list())
            .filter(it -> it.isDirectory())
            .forEach(subFolderFileHandle -> {
                FileHandle imageFolder = Gdx.files.external(folder + "/" + subFolderFileHandle.name() + "/images/");
                putDeskExternalRuntimeData(deskExternalRuntimeDataMap, imageFolder, subFolderFileHandle.name(), defaultCover);
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
        FileHandle subFileHandle = Gdx.files.external(folder + "/" + subFolderName + "/" + subFileName);
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
        return Arrays.stream(baseFolderFileHandle.list())
            .filter(it -> it.isDirectory())
            .collect(Collectors.toMap(
                it -> it.name(),
                it -> readSubFolderData(it.name())
            ));
    }

    @Override
    public void writeAllSubFolderData(Map<String, T_SUB> saveDataMap) {
        saveDataMap.forEach((k, v) -> writeSubFolderData(k, v));
    }
}

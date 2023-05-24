package hundun.tool.logic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hundun.gdxgame.gamelib.base.save.ISaveTool;

public class ExternalExcelSaveTool<T> implements ISaveTool<T> {

    private static final String charSet = "UTF-8";
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;
    private final String folder;
    private final String path;
    private FileHandle fileHandle;
    private FileHandle baseFolderFileHandle;
    public ExternalExcelSaveTool(String folder, String fileName, Class<T> clazz) {
        this.clazz = clazz;
        this.objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
        ;
        this.folder = folder;
        this.path = folder + "/" + fileName;
    }


    @Override
    public void lazyInitOnGameCreate() {
        fileHandle = Gdx.files.external(path);
        baseFolderFileHandle = Gdx.files.external(folder);
        baseFolderFileHandle.mkdirs();
    }

    @Override
    public boolean hasRootSave() {
        return fileHandle.exists();
    }

    @Override
    public void writeRootSaveData(T saveData) {
        try {
            String str = objectMapper.writeValueAsString(saveData);
            fileHandle.writeString(str, false, charSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T readRootSaveData() {
        if (!fileHandle.exists()) {
            return null;
        }
        String str = fileHandle.readString(charSet);
        try {
            return objectMapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

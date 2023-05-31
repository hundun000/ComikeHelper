package hundun.tool.logic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hundun.gdxgame.gamelib.base.save.ISaveTool;

public class ExternalExcelSaveTool implements ISaveTool<List<Map<Integer, String>>> {


    private final String folder;
    private String path;
    private FileHandle fileHandle;
    private FileHandle baseFolderFileHandle;
    
    private List<Map<Integer, String>> cachedDataList;
    
    public ExternalExcelSaveTool(String folder) {
        this.folder = folder;
    }


    @Override
    public void lazyInitOnGameCreate() {
        
        baseFolderFileHandle = Gdx.files.external(folder);
        baseFolderFileHandle.mkdirs();

    }
    
    public boolean lazyInitOnRuntime(String fileName) {
        this.path = folder + "/" + fileName;
        fileHandle = Gdx.files.external(path);
        
        if (fileHandle.exists()) {
            String filePath = fileHandle.file().getPath();
            cachedDataList = EasyExcel.read(filePath).sheet().doReadSync();
            return true;
        } else {
            return false;
        }
        
    }

    @Override
    public boolean hasRootSave() {
        return fileHandle.exists();
    }

    @Override
    public void writeRootSaveData(List<Map<Integer, String>> saveData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Map<Integer, String>> readRootSaveData() {
        return cachedDataList;
    }
    
    

}

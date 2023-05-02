package hundun.tool.logic.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import hundun.gdxgame.gamelib.starter.save.IRootSaveExtension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/11/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootSaveData {
    MySystemSettingSaveData systemSettingSaveData;
    MyGameplaySaveData gameplaySaveData;

    @Data
    public static class MySystemSettingSaveData {
        
    }
    
    @Data
    public static class MyGameplaySaveData {
        List<DeskData> deskDatas;

    }
    
    public static final class Factory implements IRootSaveExtension<RootSaveData, MySystemSettingSaveData, MyGameplaySaveData> {
        
        public static final Factory INSTANCE = new Factory();
        
        @Override
        public MySystemSettingSaveData getSystemSave(RootSaveData rootSaveData) {
            return rootSaveData.getSystemSettingSaveData();
        }
    
        @Override
        public MyGameplaySaveData getGameplaySave(RootSaveData rootSaveData) {
            return rootSaveData.getGameplaySaveData();
        }
    
        @Override
        public RootSaveData newRootSave(MyGameplaySaveData gameplaySave, MySystemSettingSaveData systemSettingSave) {
            return new RootSaveData(systemSettingSave, gameplaySave);
        }
    
        @Override
        public MyGameplaySaveData newGameplaySave() {
            return new MyGameplaySaveData();
        }
    
        @Override
        public MySystemSettingSaveData newSystemSave() {
            return new MySystemSettingSaveData();
        }

        
        
    }
}

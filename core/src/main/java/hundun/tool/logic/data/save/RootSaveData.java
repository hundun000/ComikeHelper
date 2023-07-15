package hundun.tool.logic.data.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.gamelib.starter.save.IRootSaveExtension;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.external.ExternalUserPrivateData.GoodPrivateData;
import hundun.tool.logic.data.generic.GenericPosData;
import hundun.tool.logic.data.save.RoomSaveData.DeskAreaInfo;
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
        ExternalMainData defaultExternalMainData;
        Map<String, DeskSaveData> defaultDeskSaveDatas;
        ExternalUserPrivateData defaultUserPrivateData;
    }

    public static final class Extension implements IRootSaveExtension<RootSaveData, MySystemSettingSaveData, MyGameplaySaveData> {

        public static final Extension INSTANCE = new Extension();
        
        public static MyGameplaySaveData genereateStarterGameplaySaveData() {
            throw new UnsupportedOperationException();
        }

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

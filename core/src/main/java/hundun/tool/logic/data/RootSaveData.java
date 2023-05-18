package hundun.tool.logic.data;

import java.util.List;
import java.util.Map;

import hundun.gdxgame.gamelib.starter.save.IRootSaveExtension;
import hundun.tool.logic.data.external.ExternalMainData;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        List<String> defaultCartGoodIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GoodSaveData {
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeskSaveData {
        String name;
        String posDataLine;
        List<GoodSaveData> goodSaveDatas;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomSaveData {
        String name;
        int startX;
        int startY;
        int roomWidth;
        int roomHeight;
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

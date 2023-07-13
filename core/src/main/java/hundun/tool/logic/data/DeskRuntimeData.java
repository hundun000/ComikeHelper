package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.save.DeskSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2023/05/09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeskRuntimeData {

    String uiName;
    DeskLocation mainLocation;
    List<DeskLocation> companionLocationList;
    List<GoodRuntimeData> goodSaveDatas;
    List<FileHandle> detailFileHandles;
    FileHandle coverFileHandle;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeskLocation {
        String room;
        String area;
        int areaIndex;
        Vector2 pos;
        
        public static class Companion {
            public static String SPLIT = ";";
            
            public static String toLine(String room, String area, int areaIndex) {
                return room + SPLIT + area + SPLIT + areaIndex;
            }

            public static String toLine(DeskSaveData deskSaveData) {
                return deskSaveData.getRoom() + SPLIT + deskSaveData.getArea() + SPLIT + deskSaveData.getPos().getAreaIndex();
            }
        }

        public static class Factory {

            
            public static DeskLocation locationAsMain(LayoutConst layoutConst, DeskSaveData deskSaveData) {
                return DeskLocation.builder()
                        .room(deskSaveData.getRoom())
                        .area(deskSaveData.getArea())
                        .areaIndex(deskSaveData.getPos().getAreaIndex())
                        .pos(new Vector2(deskSaveData.getPos().getX(), deskSaveData.getPos().getY()))
                        .build();
            }

            public static List<DeskLocation> locationAsCompanion(LayoutConst layoutConst, DeskSaveData deskSaveData) {
                return deskSaveData.getCompanionPosList().stream()
                        .map(companionPos -> DeskLocation.builder()
                                .room(deskSaveData.getRoom())
                                .area(deskSaveData.getArea())
                                .areaIndex(companionPos.getAreaIndex())
                                .pos(new Vector2(companionPos.getX(), companionPos.getY()))
                                .build())
                        .collect(Collectors.toList())
                        ;
            }
            
        }
    }

    public static class Factory {
        public static DeskRuntimeData fromExternalData(LayoutConst layoutConst, ExternalDeskData externalDeskData) {
            DeskSaveData deskSaveData = externalDeskData.getDeskSaveData();

            DeskRuntimeData mainResult = DeskRuntimeData.builder()
                    .uiName(deskSaveData.getRealName() != null ? deskSaveData.getRealName() : deskSaveData.getIdName())
                    .mainLocation(DeskLocation.Factory.locationAsMain(layoutConst, deskSaveData))
                    .companionLocationList(DeskLocation.Factory.locationAsCompanion(layoutConst, deskSaveData))
                    .coverFileHandle(externalDeskData.getCoverFileHandle())
                    .detailFileHandles(externalDeskData.getImageFileHandles())
                    .build();
            mainResult.setGoodSaveDatas(deskSaveData.getGoodSaveDatas().stream().map(it ->
                    GoodRuntimeData.Factory.fromSaveData(mainResult, it)).collect(Collectors.toList())
            );


            return mainResult;
        }
        
    }

}

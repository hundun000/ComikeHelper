package hundun.tool.logic.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.screen.ScreenContext.LayoutConst;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
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

    private static List<String> AREA_LIST = JavaFeatureForGwt.arraysAsList("A","");




    String name;
    DeskLocation location;
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

            
        }

        public static class Factory {

            
            public static DeskLocation fromLine(LayoutConst layoutConst, DeskSaveData deskSaveData) {
                return DeskLocation.builder()
                        .room(deskSaveData.getRoom())
                        .area(deskSaveData.getArea())
                        .areaIndex(deskSaveData.getAreaIndex())
                        .pos(new Vector2(deskSaveData.getX(), deskSaveData.getY()))
                        .build();
            }
            
            public static Vector2 calculatePos(LayoutConst layoutConst, String area, int areaIndex) {
                int col = AREA_LIST.indexOf(area);
                int x = (col / 2) * (layoutConst.DESK_WIDTH + layoutConst.DESK_SMALL_COL_PADDING + layoutConst.DESK_WIDTH + layoutConst.DESK_BIG_COL_PADDING) + (col % 2 == 0 ? (layoutConst.DESK_BIG_COL_PADDING) : (layoutConst.DESK_SMALL_COL_PADDING + layoutConst.DESK_WIDTH + layoutConst.DESK_BIG_COL_PADDING));
                int y = areaIndex * (layoutConst.DESK_HEIGHT + 10);
                return new Vector2(x, y);
            }

            
        }
    }

    public static class Factory {
        public static DeskRuntimeData fromExternalRuntimeData(LayoutConst layoutConst, ExternalDeskData externalDeskData) {
            DeskSaveData deskSaveData = externalDeskData.getDeskSaveData();
            DeskRuntimeData result = DeskRuntimeData.builder()
                    .name(deskSaveData.getName())
                    .location(DeskLocation.Factory.fromLine(layoutConst, deskSaveData))
                    .coverFileHandle(externalDeskData.getCoverFileHandle())
                    .detailFileHandles(externalDeskData.getImageFileHandles())
                    .build();
            result.setGoodSaveDatas(deskSaveData.getGoodSaveDatas().stream().map(it -> GoodRuntimeData.Factory.fromSaveData(result, it)).collect(Collectors.toList()));
            return result;
        }
        
    }

}

package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.screen.ScreenContext.LayoutConst;
import hundun.tool.libgdx.screen.market.DeskVM;
import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;
import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
import hundun.tool.logic.data.RootSaveData.MySystemSettingSaveData;
import hundun.tool.logic.data.RootSaveData.RoomSaveData;
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
    
    public static List<String> AREA_LIST = JavaFeatureForGwt.arraysAsList("A", "B", "C", "D");
    
    

    
    String name;
    DeskLocation location;
    List<GoodRuntimeData> goodSaveDatas;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeskLocation {
        String area;
        int areaIndex;
        Vector2 pos;
        
        public static class Factory {
            
             
            
            public static DeskLocation fromLine(LayoutConst layoutConst, String line) {
                String[] parts = line.split(";");
                String area = parts[0];
                int areaIndex = Integer.valueOf(parts[1]);
                return DeskLocation.builder()
                        .area(area)
                        .areaIndex(areaIndex)
                        .pos(calculatePos(layoutConst, area, areaIndex))
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
        public static DeskRuntimeData fromSaveData(LayoutConst layoutConst, DeskSaveData saveData) {
            DeskRuntimeData result = DeskRuntimeData.builder()
                    .name(saveData.name)
                    .location(DeskLocation.Factory.fromLine(layoutConst, saveData.getPosDataLine()))
                    .build();
            result.setGoodSaveDatas(saveData.getGoodSaveDatas().stream().map(it -> GoodRuntimeData.Factory.fromSaveData(result, it)).collect(Collectors.toList()));
            return result;
        }
    }

}

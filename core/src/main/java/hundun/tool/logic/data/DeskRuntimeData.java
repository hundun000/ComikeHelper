package hundun.tool.logic.data;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
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
    
    
    public static final int SMALL_COL_PADDING = 50;
    public static final int BIG_COL_PADDING = 100;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 150;
    
    String name;
    DeskLocation location;
    List<GoodSaveData> goodSaveDatas;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeskLocation {
        String area;
        int areaIndex;
        Vector2 pos;
        
        public static class Factory {
            
             
            
            public static DeskLocation fromLine(String line) {
                String[] parts = line.split(";");
                String area = parts[0];
                int areaIndex = Integer.valueOf(parts[1]);
                return DeskLocation.builder()
                        .area(area)
                        .areaIndex(areaIndex)
                        .pos(calculatePos(area, areaIndex))
                        .build();
            }
            
            
            public static Vector2 calculatePos(String area, int areaIndex) {
                int col = AREA_LIST.indexOf(area);
                int x = (col / 2) * (WIDTH + SMALL_COL_PADDING + WIDTH + BIG_COL_PADDING) + (col % 2 == 0 ? (BIG_COL_PADDING) : (SMALL_COL_PADDING + WIDTH + BIG_COL_PADDING));
                int y = areaIndex * (HEIGHT + 10);
                return new Vector2(x, y);
            }
        }
    }
    
    public static class Factory {
        public static DeskRuntimeData fromSaveData(DeskSaveData saveData) {
            return DeskRuntimeData.builder()
                    .name(saveData.name)
                    .location(DeskLocation.Factory.fromLine(saveData.getPosDataLine()))
                    .goodSaveDatas(saveData.getGoodSaveDatas())
                    .build();
        }
    }

}

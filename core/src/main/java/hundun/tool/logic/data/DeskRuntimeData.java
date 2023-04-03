package hundun.tool.logic.data;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.logic.data.RootSaveData.DeskSaveData;
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
    String name;
    DeskLocation location;
    
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
            
            static List<String> AREA_LIST = JavaFeatureForGwt.arraysAsList("A", "B");
            
            public static Vector2 calculatePos(String area, int areaIndex) {
                int x = AREA_LIST.indexOf(area) * 150;
                int y = areaIndex * 50;
                return new Vector2(x, y);
            }
        }
    }
    
    public static class Factory {
        public static DeskRuntimeData fromSaveData(DeskSaveData saveData) {
            return DeskRuntimeData.builder()
                    .name(saveData.name)
                    .location(DeskLocation.Factory.fromLine(saveData.getPosDataLine()))
                    .build();
        }
    }

}

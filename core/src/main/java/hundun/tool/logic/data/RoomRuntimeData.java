package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import hundun.tool.logic.data.DeskRuntimeData.DeskLocation;
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
public class RoomRuntimeData {
    String name;
    int startX;
    int startY;
    List<DeskRuntimeData> deskDatas;
    
    public static class Factory {
        public static RoomRuntimeData fromSaveData(RoomSaveData saveData) {
            return RoomRuntimeData.builder()
                    .name(saveData.getName())
                    .startX(saveData.getStartX())
                    .startY(saveData.getStartY())
                    .deskDatas(saveData.getDeskSaveDatas().stream()
                            .map(it -> DeskRuntimeData.Factory.fromSaveData(it))
                            .collect(Collectors.toList())
                            )
                    .build();
        }
    }
    
}

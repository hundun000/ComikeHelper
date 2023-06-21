package hundun.tool.logic.data;

import java.util.List;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.save.RoomSaveData;
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
    int roomWidth;
    int roomHeight;
    List<DeskRuntimeData> deskDatas;

    public static class Factory {
        public static RoomRuntimeData fromSaveData(LayoutConst layoutConst, RoomSaveData saveData, List<DeskRuntimeData> deskRuntimeDatas) {
            return RoomRuntimeData.builder()
                    .name(saveData.getName())
                    .roomWidth(saveData.getRoomWidth())
                    .roomHeight(saveData.getRoomHeight())
                    .deskDatas(deskRuntimeDatas)
                    .build();
        }
        
    }

}

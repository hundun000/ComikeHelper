package hundun.tool.logic.data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import hundun.tool.libgdx.screen.ScreenContext.LayoutConst;
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
    int roomWidth;
    int roomHeight;
    List<DeskRuntimeData> deskDatas;

    public static class Factory {
        public static RoomRuntimeData fromSaveData(LayoutConst layoutConst, RoomSaveData saveData, List<DeskRuntimeData> deskRuntimeDatas) {
            return RoomRuntimeData.builder()
                    .name(saveData.getName())
                    .startX(saveData.getStartX())
                    .startY(saveData.getStartY())
                    .roomWidth(saveData.getRoomWidth())
                    .roomHeight(saveData.getRoomHeight())
                    .deskDatas(deskRuntimeDatas)
                    .build();
        }
    }

}

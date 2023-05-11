package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import hundun.tool.logic.data.DeskRuntimeData.DeskLocation;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;
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
public class GoodRuntimeData {
    DeskRuntimeData ownerRef;
    String name;
    
    public static class Factory {
        public static GoodRuntimeData fromSaveData(DeskRuntimeData ownerRef, GoodSaveData saveData) {
            return GoodRuntimeData.builder()
                    .ownerRef(ownerRef)
                    .name(saveData.getName())
                    .build();
        }
    }
    
}

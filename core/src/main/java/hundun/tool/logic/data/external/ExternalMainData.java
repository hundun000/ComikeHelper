package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.List;

import hundun.tool.logic.data.RootSaveData;
import hundun.tool.logic.data.RootSaveData.RoomSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalMainData {

    List<RoomSaveData> roomSaveDatas;

    public static class Factory {
        public static ExternalMainData empty() {
            return  ExternalMainData.builder()
                .roomSaveDatas(new ArrayList<>())
                .build();
        }

    }
}

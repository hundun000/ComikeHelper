package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.RootSaveData.RoomSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalAllData {

    ExternalMainData externalMainData;

    Map<String, DeskSaveData> deskDatas;

    public static class Factory {
        public static ExternalAllData empty() {
            return  ExternalAllData.builder()
                .deskDatas(new HashMap<>())
                .build();
        }

    }
}

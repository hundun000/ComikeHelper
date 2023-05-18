package hundun.tool.logic.data.external;

import java.util.HashMap;
import java.util.Map;

import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool.DeskExternalRuntimeData;
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
    Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap;
    Map<String, DeskSaveData> deskDataMap;

    public static class Factory {
        public static ExternalAllData empty() {
            return  ExternalAllData.builder()
                .deskExternalRuntimeDataMap(new HashMap<>())
                .deskDataMap(new HashMap<>())
                .build();
        }

    }
}

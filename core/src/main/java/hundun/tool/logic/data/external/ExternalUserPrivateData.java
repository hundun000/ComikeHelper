package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalUserPrivateData {
    Map<String, GoodPrivateData> goodPrivateDataMap;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GoodPrivateData {
        List<GoodRuntimeTag> tags;
    }

    public static class Factory {
        public static ExternalUserPrivateData empty() {
            return  ExternalUserPrivateData.builder()
                .goodPrivateDataMap(new HashMap<>())
                .build();
        }

    }
}

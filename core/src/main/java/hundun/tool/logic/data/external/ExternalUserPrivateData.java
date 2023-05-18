package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalUserPrivateData {
    List<String> cartGoodIds;

    public static class Factory {
        public static ExternalUserPrivateData empty() {
            return  ExternalUserPrivateData.builder()
                .cartGoodIds(new ArrayList<>())
                .build();
        }

    }
}

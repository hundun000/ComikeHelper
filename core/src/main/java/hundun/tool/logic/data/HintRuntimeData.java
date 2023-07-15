package hundun.tool.logic.data;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.generic.GenericPosData;
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
public class HintRuntimeData {

    String uiName;
    GenericPosData mainLocation;

    public static class Factory {
        public static HintRuntimeData fromExternalData(LayoutConst layoutConst, ExternalDeskData externalDeskData) {
            // TODO
            return null;
        }
    }

}

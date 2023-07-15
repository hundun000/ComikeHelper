package hundun.tool.logic.data.save;

import hundun.tool.logic.data.generic.GenericPosData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2023/06/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HintSaveData {
    String text;
    GenericPosData pos;
}
package hundun.tool.logic.data;

import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
import hundun.tool.logic.data.RootSaveData.MySystemSettingSaveData;
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
public class DeskData {
    String name;
}

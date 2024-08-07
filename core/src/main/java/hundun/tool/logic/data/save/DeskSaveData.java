package hundun.tool.logic.data.save;

import java.util.List;

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
public class DeskSaveData {
    String idName;
    String realName;
    GenericPosData mainPos;
    List<GenericPosData> companionPosList;
    List<GoodSaveData> goodSaveDatas;

}
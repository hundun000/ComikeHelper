package hundun.tool.logic.data.save;

import java.util.List;

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
    String room;
    String area;
    PosSaveData pos;
    List<PosSaveData> companionPosList;
    List<GoodSaveData> goodSaveDatas;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PosSaveData {
        int areaIndex;
        int x;
        int y;
    }
}
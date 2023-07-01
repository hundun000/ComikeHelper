package hundun.tool.logic.data.save;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2023/05/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomSaveData {
    String name;
    DeskAreaInfo deskAreaInfo;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeskAreaInfo {
        int deskAreaWidth;
        int deskAreaHeight;
        int deskAreaPadLeft;
        int deskAreaPadRight;
        int deskAreaPadTop;
        int deskAreaPadBottom;
    }
}
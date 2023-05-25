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
    int roomWidth;
    int roomHeight;
}
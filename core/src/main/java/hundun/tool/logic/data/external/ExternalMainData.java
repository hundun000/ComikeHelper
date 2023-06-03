package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.Map;

import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.save.RoomSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalMainData {

    Map<String, RoomSaveData> roomSaveDataMap;
    

    
}

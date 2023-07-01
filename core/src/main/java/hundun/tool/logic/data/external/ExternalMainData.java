package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;

import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.save.RoomSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalMainData {

    Map<String, RoomSaveData> roomSaveDataMap;
    
    Map<String, FileHandle> roomImageMap;
    
}

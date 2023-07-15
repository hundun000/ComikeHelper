package hundun.tool.logic.data;

import com.badlogic.gdx.files.FileHandle;

import java.util.List;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.save.HintSaveData;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.data.save.RoomSaveData.DeskAreaInfo;
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
public class RoomRuntimeData {
    String name;
    DeskAreaInfo deskAreaInfo;
    FileHandle roomImage;
    List<DeskRuntimeData> deskDatas;
    List<HintSaveData> hints;

    public static class Factory {
        public static RoomRuntimeData fromSaveData(
                LayoutConst layoutConst,
                RoomSaveData saveData,
                List<DeskRuntimeData> deskRuntimeDatas,
                FileHandle roomImage) {
            return RoomRuntimeData.builder()
                    .name(saveData.getName())
                    .deskAreaInfo(saveData.getDeskAreaInfo())
                    .hints(saveData.getHints())
                    .roomImage(roomImage)
                    .deskDatas(deskRuntimeDatas)
                    .build();
        }
        
    }

}

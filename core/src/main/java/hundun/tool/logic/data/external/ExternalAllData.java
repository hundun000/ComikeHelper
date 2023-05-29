package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.screen.ScreenContext.LayoutConst;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.DeskRuntimeData.DeskLocation;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool.DeskExternalRuntimeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalAllData {

    ExternalMainData externalMainData;
    Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap;

    public static class Factory {
        public static ExternalAllData empty() {
            return  ExternalAllData.builder()
                    .externalMainData(ExternalMainData.builder()
                            .roomSaveDataMap(new HashMap<>())
                            .build())
                    .deskExternalRuntimeDataMap(new HashMap<>())
                    .build();
        }
        
        public static ExternalAllData fromExcelData(LayoutConst layoutConst, List<Map<Integer, String>> data, FileHandle coverFileHandle) {
            Map<Integer, String> firstLine = data.remove(0);
            String roomName = firstLine.get(0);
            
            
            List<DeskSaveData> deskSaveDatas = new ArrayList<>();
            AtomicInteger maxCol = new AtomicInteger(0);
            for (int row = 0; row < data.size(); row++) {
                int y = layoutConst.DESK_HEIGHT * (data.size() - row);
                data.get(row).forEach((col, cell) -> {
                    if (col > maxCol.get()) {
                        maxCol.set(col);
                    }
                    if (cell == null || cell.equals("*")) {
                        return;
                    }
                    int x = layoutConst.DESK_WIDTH * col;
                    String[] posAndPosParts = cell.split(":");
                    String name;
                    String posLine = roomName + DeskLocation.Companion.SPLIT;
                    if (posAndPosParts.length == 2) {
                        posLine += posAndPosParts[0];
                        name = posAndPosParts[1];
                    } else {
                        posLine += posAndPosParts[0];
                        name = posLine.replace(DeskLocation.Companion.SPLIT, "_");
                    }
                    posLine += DeskLocation.Companion.SPLIT + x + DeskLocation.Companion.SPLIT + y;
                    deskSaveDatas.add(DeskSaveData.builder()
                                .name(name)
                                .posDataLine(posLine)
                                .goodSaveDatas(new ArrayList<>())
                                .build());
                });
                
            }
            
            Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap = deskSaveDatas.stream()
                    .map(it -> {
                        return DeskExternalRuntimeData.Factory.fromBasic(it, coverFileHandle);
                    })
                    .collect(Collectors.toMap(
                            it -> it.getDeskSaveData().getName(), 
                            it -> it
                            ))
                    ;
            
            int roomWidth = (maxCol.get() + 1) * layoutConst.DESK_WIDTH;
            int roomHeight = (data.size() + 1) * layoutConst.DESK_HEIGHT;
            RoomSaveData singleRoomSaveData = RoomSaveData.builder()
                    .name(roomName)
                    .roomWidth(roomWidth)
                    .roomHeight(roomHeight)
                    .build();
            
            return ExternalAllData.builder()
                    .externalMainData(ExternalMainData.builder()
                            .roomSaveDataMap(JavaFeatureForGwt.mapOf(
                                    singleRoomSaveData.getName(), 
                                    singleRoomSaveData
                                    ))
                            .build())
                    .deskExternalRuntimeDataMap(deskExternalRuntimeDataMap)
                    .build();
        }
    }
}

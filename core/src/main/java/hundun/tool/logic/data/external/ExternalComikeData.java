package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.files.FileHandle;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.ExternalResourceManager.DeskExcelTempData;
import hundun.tool.logic.data.save.DeskSaveData;
import hundun.tool.logic.data.save.GoodSaveData;
import hundun.tool.logic.data.save.RoomSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalComikeData {

    ExternalMainData externalMainData;
    /**
     * name key
     */
    Map<String, ExternalDeskData> deskExternalRuntimeDataMap;

    public static class Factory {
        
        private static String extractInteger(String str) {
            String integer = "";
            int i = str.length() - 1;
            while (i >= 0 && Character.isDigit(str.charAt(i))) {
                integer = str.charAt(i) + integer;
                i--;
            }
            return integer;
        }
        
        public static ExternalComikeData empty() {
            return  ExternalComikeData.builder()
                    .externalMainData(ExternalMainData.builder()
                            .roomSaveDataMap(new HashMap<>())
                            .build())
                    .deskExternalRuntimeDataMap(new HashMap<>())
                    .build();
        }
        
        
        private static RoomSaveData handleOneRoom(LayoutConst layoutConst,
                String roomName,
                List<Map<Integer, String>> lines, 
                Map<String, DeskExcelTempData> deskExcelTempDataMap,
                FileHandle coverFileHandle, 
                Map<String, ExternalDeskData> deskExternalRuntimeDataMap
                ) {
            
            
            AtomicInteger maxCol = new AtomicInteger(0);
            for (int row = 0; row < lines.size(); row++) {
                int y = layoutConst.DESK_HEIGHT * (lines.size() - row);
                lines.get(row).forEach((col, comikePos) -> {
                    if (col > maxCol.get()) {
                        maxCol.set(col);
                    }
                    if (comikePos == null || comikePos.equals("*")) {
                        return;
                    }
                    int x = layoutConst.DESK_WIDTH * col;
                    
                    final String areaIndexText = extractInteger(comikePos);
                    final int areaIndex = Integer.parseInt(areaIndexText);
                    final String area = comikePos.substring(0, comikePos.length() - areaIndexText.length());
                    
                    DeskExcelTempData deskExcelTempData = deskExcelTempDataMap.get(comikePos);
                    
                    String deskName;
                    String deskRealName;
                    List<GoodSaveData> goods;
                    if (deskExcelTempData != null) {
                        deskName = deskExcelTempData.getDeskName();
                        deskRealName = deskExcelTempData.getDeskName();
                        goods = deskExcelTempData.getGoods();
                    } else {
                        deskName = comikePos + "的店";
                        deskRealName = null;
                        goods = new ArrayList<>();
                    }
                    
                    DeskSaveData deskSaveData = DeskSaveData.builder()
                                .idName(deskName)
                                .realName(deskRealName)
                                .room(roomName)
                                .area(area)
                                .areaIndex(areaIndex)
                                .x(x)
                                .y(y)
                                .goodSaveDatas(goods)
                                .build();
                    ExternalDeskData externalDeskData = ExternalDeskData.Factory.fromBasic(deskSaveData, coverFileHandle);
                    deskExternalRuntimeDataMap.put(deskName, externalDeskData);
                });
                
            }
            

            
            int roomWidth = (maxCol.get() + 1) * layoutConst.DESK_WIDTH;
            int roomHeight = (lines.size() + 1) * layoutConst.DESK_HEIGHT;
            RoomSaveData roomSaveData = RoomSaveData.builder()
                    .name(roomName)
                    .roomWidth(roomWidth)
                    .roomHeight(roomHeight)
                    .build();
            return roomSaveData;
        }
        
        public static ExternalComikeData fromExcelData(LayoutConst layoutConst, 
                Map<String, List<Map<Integer, String>>> roomTempDataMap, 
                Map<String, DeskExcelTempData> deskExcelTempDataMap, 
                FileHandle coverFileHandle
                ) {
            Map<String, ExternalDeskData> deskExternalRuntimeDataMap = new HashMap<>();
            Map<String, RoomSaveData> roomSaveDataMap = new HashMap<>();
            
            roomTempDataMap.forEach((roomName, lines) -> {
                RoomSaveData roomSaveData = handleOneRoom(
                        layoutConst, 
                        roomName, 
                        lines, 
                        deskExcelTempDataMap,
                        coverFileHandle, 
                        deskExternalRuntimeDataMap
                        );
                roomSaveDataMap.put(roomName, roomSaveData);
            });
            
            
            return ExternalComikeData.builder()
                    .externalMainData(ExternalMainData.builder()
                            .roomSaveDataMap(roomSaveDataMap)
                            .build())
                    .deskExternalRuntimeDataMap(deskExternalRuntimeDataMap)
                    .build();
        }
    }
}

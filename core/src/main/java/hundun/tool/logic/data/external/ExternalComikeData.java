package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.files.FileHandle;

import org.apache.commons.math3.util.Pair;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.ExternalResourceManager.DeskExcelTempData;
import hundun.tool.logic.data.save.DeskSaveData;
import hundun.tool.logic.data.save.GoodSaveData;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.data.save.DeskSaveData.PosSaveData;
import hundun.tool.logic.data.save.RoomSaveData.DeskAreaInfo;
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
                            .roomImageMap(new HashMap<>())
                            .build())
                    .deskExternalRuntimeDataMap(new HashMap<>())
                    .build();
        }



        private static Pair<RoomSaveData, Map<String, ExternalDeskData>>  handleOneRoom(
                LayoutConst layoutConst,
                String roomName,
                List<Map<Integer, String>> lines,
                Map<String, DeskExcelTempData> deskExcelTempDataMap,
                FileHandle coverFileHandle
                ) {

            /*
             * deskExcelTempDataMap：
             *     id=Foo社团，位置=（A, 01 + [02, 03]），goods，RealName=Foo社团
             * lines数据：
             * [
             *     (id=A01的店，位置=（A，01，x1，y1），无商品，RealName=null),
             *     (id=A02的店，位置=（A，02，x2，y2），无商品，RealName=null)
             *     (id=A99的店，位置=（A，99，x99，y99），无商品，RealName=null)
             * ]
             *
             * saveDataMap:
             * [
             *     (id=Foo社团，位置=（A, （01，x1，y1） + [（02，x2，y2）]），goods，RealName=Foo社团),
             *     (id=A99的店，位置=（A, （99，x99，y99） + []），无商品，RealName=null),
             * ]
             */

            AtomicInteger maxCol = new AtomicInteger(0);
            Map<String, ExternalDeskData> deskResultMap = new HashMap<>();
            for (int row = 0; row < lines.size(); row++) {
                int y = layoutConst.DESK_HEIGHT * (lines.size() - row);
                lines.get(row).forEach((col, cellComikePos) -> {
                    if (col > maxCol.get()) {
                        maxCol.set(col);
                    }
                    if (cellComikePos == null || cellComikePos.equals("*")) {
                        return;
                    }
                    int x = layoutConst.DESK_WIDTH * col;
                    
                    final String areaIndexText = extractInteger(cellComikePos);
                    final int areaIndex = Integer.parseInt(areaIndexText);
                    final String area = cellComikePos.substring(0, cellComikePos.length() - areaIndexText.length());

                    final DeskExcelTempData targetDeskExcelTempData = deskExcelTempDataMap.values().stream()
                            .filter(it -> it.getPos().equals(cellComikePos)
                                    || it.getCompanionPosList().stream().anyMatch(companionPos -> companionPos.equals(cellComikePos)))
                            .findAny()
                            .orElse(null)
                            ;
                    final String deskIdName;
                    final String deskRealName;
                    final List<GoodSaveData> goods;
                    if (targetDeskExcelTempData == null) {
                        deskIdName = cellComikePos + "的店";
                        deskRealName = null;
                        goods = new ArrayList<>();
                    } else {
                        deskIdName = targetDeskExcelTempData.getDeskName();
                        deskRealName = targetDeskExcelTempData.getDeskName();
                        goods = targetDeskExcelTempData.getGoods();
                    }
                    final ExternalDeskData targetDeskSaveData;
                    if (deskResultMap.containsKey(deskIdName)) {
                        targetDeskSaveData = deskResultMap.get(deskIdName);
                    } else {
                        DeskSaveData deskSaveData = DeskSaveData.builder()
                                .idName(deskIdName)
                                .realName(deskRealName)
                                .room(roomName)
                                .area(area)
                                .pos(null)
                                .companionPosList(new ArrayList<>())
                                .goodSaveDatas(goods)
                                .build();
                        targetDeskSaveData = ExternalDeskData.Factory.fromBasic(deskSaveData, coverFileHandle);
                        deskResultMap.put(deskIdName, targetDeskSaveData);
                    }

                    PosSaveData cellPosSaveData = PosSaveData.builder()
                            .areaIndex(areaIndex)
                            .x(x)
                            .y(y)
                            .build();
                    if (targetDeskSaveData.getDeskSaveData().getPos() == null) {
                        targetDeskSaveData.getDeskSaveData().setPos(cellPosSaveData);
                    } else {
                        targetDeskSaveData.getDeskSaveData().getCompanionPosList().add(cellPosSaveData);
                    }
                });
                
            }


            int roomWidth = (maxCol.get() + 1) * layoutConst.DESK_WIDTH;
            int roomHeight = (lines.size() + 1) * layoutConst.DESK_HEIGHT;
            RoomSaveData roomSaveData =  RoomSaveData.builder()
                    .name(roomName)
                    .deskAreaInfo(DeskAreaInfo.builder()
                            .deskAreaWidth(roomWidth)
                            .deskAreaHeight(roomHeight)
                            // TODO pad from excel
                            .build())
                    .build();
            return Pair.create(roomSaveData, deskResultMap);
        }
        
        public static ExternalComikeData fromExcelData(LayoutConst layoutConst, 
                Map<String, List<Map<Integer, String>>> roomLinesMap,
                Map<String, DeskExcelTempData> deskExcelTempDataMap, 
                FileHandle coverFileHandle
                ) {
            Map<String, ExternalDeskData> deskExternalRuntimeDataMap = new HashMap<>();
            Map<String, RoomSaveData> roomSaveDataMap = new HashMap<>();
            
            roomLinesMap.forEach((roomName, lines) -> {
                Pair<RoomSaveData, Map<String, ExternalDeskData>> result = handleOneRoom(
                        layoutConst, 
                        roomName, 
                        lines, 
                        deskExcelTempDataMap,
                        coverFileHandle
                        );
                roomSaveDataMap.put(roomName, result.getFirst());
                deskExternalRuntimeDataMap.putAll(result.getValue());
            });
            
            
            return ExternalComikeData.builder()
                    .externalMainData(ExternalMainData.builder()
                            .roomSaveDataMap(roomSaveDataMap)
                            .roomImageMap(new HashMap<>())
                            .build())
                    .deskExternalRuntimeDataMap(deskExternalRuntimeDataMap)
                    .build();
        }
    }
}

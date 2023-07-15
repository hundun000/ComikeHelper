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
import hundun.tool.logic.ExternalResourceManager.ExcelRoomData;
import hundun.tool.logic.data.save.DeskSaveData;
import hundun.tool.logic.data.save.GoodSaveData;
import hundun.tool.logic.data.save.HintSaveData;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.data.generic.GenericPosData;
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
        
        public static String extractInteger(String str) {
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

        private static void handleOneCellAsHint(
                String cellText,
                LayoutConst layoutConst,
                Map<String, DeskExcelTempData> deskExcelTempDataMap,
                FileHandle coverFileHandle,
                int x,
                int y,
                String roomName,
                List<HintSaveData> hintResultList
        ) {
            String hintTextPart = cellText.replace(HINT_CELL_START, "");

            GenericPosData currentPos = GenericPosData.builder()
                    .room(roomName)
                    .x(x)
                    .y(y)
                    .build();
            HintSaveData hintSaveData = HintSaveData.builder()
                    .text(hintTextPart)
                    .pos(currentPos)
                    .build();
            hintResultList.add(hintSaveData);
        }

        private static void handleOneCellAsDesk(
                String cellText,
                LayoutConst layoutConst,
                Map<String, DeskExcelTempData> deskExcelTempDataMap,
                FileHandle coverFileHandle,
                int x,
                int y,
                String roomName,
                Map<String, ExternalDeskData> deskResultMap
        ) {


            final String cellAreaIndexText = extractInteger(cellText);
            final int cellAreaIndex = Integer.parseInt(cellAreaIndexText);
            final String cellArea = cellText.substring(0, cellText.length() - cellAreaIndexText.length());

            final DeskExcelTempData targetDeskExcelTempData = deskExcelTempDataMap.values().stream()
                    .filter(it -> it.getArea().equals(cellArea)
                            && it.getMainAreaIndex() <= cellAreaIndex
                            && cellAreaIndex <= it.getEndAreaIndex()
                    )
                    .findAny()
                    .orElse(null)
                    ;
            final int mainAreaIndex;
            final String deskIdName;
            final String deskRealName;
            final List<GoodSaveData> goods;
            if (targetDeskExcelTempData == null) {
                mainAreaIndex = cellAreaIndex;
                deskIdName = cellText + "的店";
                deskRealName = null;
                goods = new ArrayList<>();
            } else {
                mainAreaIndex = targetDeskExcelTempData.getMainAreaIndex();
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
                        .mainPos(null)
                        .companionPosList(new ArrayList<>())
                        .goodSaveDatas(goods)
                        .build();
                targetDeskSaveData = ExternalDeskData.Factory.fromBasic(deskSaveData, coverFileHandle);
                deskResultMap.put(deskIdName, targetDeskSaveData);
            }

            GenericPosData currentPos = GenericPosData.builder()
                    .room(roomName)
                    .area(cellArea)
                    .areaIndex(cellAreaIndex)
                    .x(x)
                    .y(y)
                    .build();
            if (cellAreaIndex == mainAreaIndex) {
                targetDeskSaveData.getDeskSaveData().setMainPos(currentPos);
            } else {
                targetDeskSaveData.getDeskSaveData().getCompanionPosList().add(currentPos);
            }
        }
        private static final String HINT_CELL_START = "HINT:";
        private static Pair<RoomSaveData, Map<String, ExternalDeskData>> handleOneRoom(
                LayoutConst layoutConst,
                String roomName,
                ExcelRoomData excelRoomData,
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
            final List<Map<Integer, String>> lines = excelRoomData.getLineDataList();
            AtomicInteger maxCol = new AtomicInteger(0);
            Map<String, ExternalDeskData> deskResultMap = new HashMap<>();
            List<HintSaveData> hintResultList = new ArrayList<>();
            for (int row = 0; row < lines.size(); row++) {
                int y = layoutConst.DESK_HEIGHT * (lines.size() - row);
                lines.get(row).forEach((col, cellText) -> {
                    int x = layoutConst.DESK_WIDTH * col;
                    if (col > maxCol.get()) {
                        maxCol.set(col);
                    }
                    if (cellText == null || cellText.equals("*")) {
                        return;
                    } else if (cellText.startsWith(HINT_CELL_START)) {
                        handleOneCellAsHint(
                                cellText,
                                layoutConst,
                                deskExcelTempDataMap,
                                coverFileHandle,
                                x,
                                y,
                                roomName,
                                hintResultList
                        );
                    } else {
                        handleOneCellAsDesk(
                                cellText,
                                layoutConst,
                                deskExcelTempDataMap,
                                coverFileHandle,
                                x,
                                y,
                                roomName,
                                deskResultMap
                        );
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
                            .deskAreaPadLeft(excelRoomData.getDeskAreaInfoList().get(0))
                            .deskAreaPadRight(excelRoomData.getDeskAreaInfoList().get(1))
                            .deskAreaPadTop(excelRoomData.getDeskAreaInfoList().get(2))
                            .deskAreaPadBottom(excelRoomData.getDeskAreaInfoList().get(3))
                            .build())
                    .hints(hintResultList)
                    .build();
            return Pair.create(roomSaveData, deskResultMap);
        }
        
        public static ExternalComikeData fromExcelData(LayoutConst layoutConst,
                                                       Map<String, ExcelRoomData> excelRoomDataMap,
                                                       Map<String, DeskExcelTempData> deskExcelTempDataMap,
                                                       FileHandle coverFileHandle
                ) {
            Map<String, ExternalDeskData> deskExternalRuntimeDataMap = new HashMap<>();
            Map<String, RoomSaveData> roomSaveDataMap = new HashMap<>();

            excelRoomDataMap.forEach((roomName, excelRoomData) -> {
                Pair<RoomSaveData, Map<String, ExternalDeskData>> result = handleOneRoom(
                        layoutConst, 
                        roomName,
                        excelRoomData,
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

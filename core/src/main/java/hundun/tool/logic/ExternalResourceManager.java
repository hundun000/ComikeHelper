package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.damios.guacamole.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalComikeData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.save.GoodSaveData;
import hundun.tool.logic.data.generic.GenericPosData;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool;
import hundun.tool.logic.util.ExternalExcelSaveTool;
import hundun.tool.logic.util.SimpleExternalJsonSaveTool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ExternalResourceManager {
    private static final String BUILDER_ROOT_FOLDER = "ComikeHelper/builder/";
    private static final String USER_ROOT_FOLDER = "ComikeHelper/user/";
    private static final String SHARED_ROOT_FOLDER = "ComikeHelper/shared/";
    @Getter
    FileHandle defaultCoverFileHandle;
    ComikeHelperGame game;
    ComplexExternalJsonSaveTool sharedComplexSaveTool;
    //ExternalJsonSaveTool<ExternalMainData> userMainDataSaveTool;
    SimpleExternalJsonSaveTool<ExternalUserPrivateData> userPrivateDataSaveTool;
    ExternalExcelSaveTool sharedExcelSaveTool;

    public ExternalResourceManager(ComikeHelperGame game) {
        this.game = game;
        this.sharedComplexSaveTool = new ComplexExternalJsonSaveTool(
            SHARED_ROOT_FOLDER
            );
        //this.userMainDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER + "main.json", ExternalMainData.class);
        this.userPrivateDataSaveTool = new SimpleExternalJsonSaveTool<>(USER_ROOT_FOLDER, "private.json", ExternalUserPrivateData.class);
        this.sharedExcelSaveTool = new ExternalExcelSaveTool(BUILDER_ROOT_FOLDER);
    }


    public MergeWorkInProgressModel providerExternalGameplayData(ExternalComikeData oldComikeData, ExternalUserPrivateData oldUserPrivateData) {
        
        MergeWorkInProgressModel model = new MergeWorkInProgressModel(
                oldComikeData, 
                oldUserPrivateData,
                sharedComplexSaveTool.readMainData(), 
                sharedComplexSaveTool.readAllSubFolderData(),
                userPrivateDataSaveTool.readRootSaveData()
                );
        return model;
    }
    
    @AllArgsConstructor
    @Getter
    @Builder
    public static class DeskExcelTempData {
        String area;
        int mainAreaIndex;
        int endAreaIndex;
        String deskName;
        List<GoodSaveData> goods;
    }

    @Getter
    @AllArgsConstructor
    public static class ExcelRoomData {
        List<Map<Integer, String>> lineDataList;
        List<Integer> deskAreaInfoList;
    }
    
    
    public MergeWorkInProgressModel providerExcelGameplayData(ExternalComikeData oldComikeData, ExternalUserPrivateData oldUserPrivateData) {

        
        Map<String, ExcelRoomData> roomTempDataMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            boolean exists = sharedExcelSaveTool.lazyInitOnRuntime("room_" + i + ".xlsx");
            if (!exists) {
                break;
            }
            List<Map<Integer, String>> roomRawExcelData = sharedExcelSaveTool.readRootSaveData();
            Map<Integer, String> firstLine = roomRawExcelData.remove(0);
            String roomName = firstLine.get(0);
            List<Integer> padInfoList = JavaFeatureForGwt.listOf(
                    Integer.parseInt(firstLine.get(3)),
                    Integer.parseInt(firstLine.get(4)),
                    Integer.parseInt(firstLine.get(5)),
                    Integer.parseInt(firstLine.get(6))
            );
            roomTempDataMap.put(roomName, new ExcelRoomData(roomRawExcelData, padInfoList));
        }


        
        sharedExcelSaveTool.lazyInitOnRuntime("desk.xlsx");
        List<Map<Integer, String>> deskGoodsRawExcelData = sharedExcelSaveTool.readRootSaveData();
        Map<String, DeskExcelTempData> deskGoodsMap = new HashMap<>();
        deskGoodsRawExcelData.forEach(line -> {
            String[] deskPosParts = line.get(0).split("-");
            String deskName = line.get(1);
            String goodName = line.get(2);
            String mainPos = deskPosParts[0];
            String endPos = deskPosParts.length > 1 ? deskPosParts[1] : deskPosParts[0];

            final String areaIndexText = ExternalComikeData.Factory.extractInteger(mainPos);
            final int mainAreaIndex = Integer.parseInt(ExternalComikeData.Factory.extractInteger(mainPos));
            final String area = mainPos.substring(0, mainPos.length() - areaIndexText.length());

            final int endAreaIndex = Integer.parseInt(ExternalComikeData.Factory.extractInteger(endPos));

            if (!deskGoodsMap.containsKey(mainPos)) {
                deskGoodsMap.put(mainPos, DeskExcelTempData.builder()
                                .deskName(deskName)
                                .area(area)
                                .mainAreaIndex(mainAreaIndex)
                                .endAreaIndex(endAreaIndex)
                                .goods(new ArrayList<>())
                                .build());
            }
            deskGoodsMap.get(mainPos).getGoods().add(GoodSaveData.builder()
                    .name(goodName)
                    .build());
        });

        ExternalComikeData externalAllDataFromExcel = ExternalComikeData.Factory.fromExcelData(
                game.getScreenContext().getLayoutConst(), 
                roomTempDataMap, 
                deskGoodsMap,
                defaultCoverFileHandle
                );
        
        ExternalUserPrivateData otherUserPrivateData = ExternalUserPrivateData.Factory.empty();
        
        MergeWorkInProgressModel model = new MergeWorkInProgressModel(
                oldComikeData, 
                oldUserPrivateData,
                externalAllDataFromExcel.getExternalMainData(), 
                externalAllDataFromExcel.getDeskExternalRuntimeDataMap(),
                otherUserPrivateData
                );
        
        return model;
    }
    
    
    public static class MergeWorkInProgressModel {
        final ExternalComikeData oldComikeData;
        final ExternalUserPrivateData oldUserPrivateData;
        final ExternalMainData otherMainData;
        final Map<String, ExternalDeskData> otherDeskDataMap;
        final ExternalUserPrivateData otherUserPrivateData;
        @Getter
        Map<String, RoomSaveData> previewNoChangeRoomDataMap = new HashMap<>();
        @Getter
        Map<String, RoomSaveData> previewAddRoomDataMap = new HashMap<>();
        @Getter
        Map<String, RoomSaveData> previewConflictRoomDataMap = new HashMap<>();
        @Getter
        Map<String, ExternalDeskData> previewNoChangeDeskDataMap = new HashMap<>();
        @Getter
        Map<String, ExternalDeskData> previewAddDeskDataMap = new HashMap<>();
        @Getter
        Map<String, ExternalDeskData> previewConflictByIdDeskDataMap = new HashMap<>();
        @Getter
        List<Pair<ExternalDeskData, ExternalDeskData>> previewConflictByPosDeskDataMap = new ArrayList<>();
        @Getter
        Pair<Integer, Integer> previewCartSizeCompare;
        @Getter
        Map<String, FileHandle> previewAddRoomImageMap = new HashMap<>();
        
        public MergeWorkInProgressModel(
                ExternalComikeData oldComikeData,
                ExternalUserPrivateData oldUserPrivateData,
                ExternalMainData otherMainData,
                Map<String, ExternalDeskData> otherDeskDataMap,
                ExternalUserPrivateData otherUserPrivateData
                ) {
            this.oldComikeData = oldComikeData;
            this.oldUserPrivateData = oldUserPrivateData;
            this.otherMainData = otherMainData;
            this.otherDeskDataMap = otherDeskDataMap;
            this.otherUserPrivateData = otherUserPrivateData;


            Map<String, ExternalDeskData> oldDeskPosMap = oldComikeData.getDeskExternalRuntimeDataMap().values().stream()
                            .collect(Collectors.toMap(
                                    it -> GenericPosData.Extension.toLine(it.getDeskSaveData().getMainPos()),
                                    it -> it
                            ));

            otherDeskDataMap.forEach((k, v) -> {
                ExternalDeskData oldSamePosDeskData = oldDeskPosMap.get(GenericPosData.Extension.toLine(v.getDeskSaveData().getMainPos()));
                if (oldSamePosDeskData != null) {
                    previewConflictByPosDeskDataMap.add(new Pair<>(oldSamePosDeskData, v));
                    return;
                }
                ExternalDeskData oldDeskData = oldComikeData.getDeskExternalRuntimeDataMap().get(k);
                if (oldDeskData != null) {
                    if (oldDeskData.getDeskSaveData().equals(v.getDeskSaveData())) {
                        previewNoChangeDeskDataMap.put(k, v);
                    } else {
                        previewConflictByIdDeskDataMap.put(k, v);
                    }
                    return;
                }
                previewAddDeskDataMap.put(k, v);
            });
            
            otherMainData.getRoomSaveDataMap().forEach((k, v) -> {
                RoomSaveData oldDeskData = oldComikeData.getExternalMainData().getRoomSaveDataMap().get(k);
                if (oldDeskData != null) {
                    if (oldDeskData.equals(v)) {
                        previewNoChangeRoomDataMap.put(k, v);
                    } else {
                        previewConflictRoomDataMap.put(k, v);
                    }
                } else {
                    previewAddRoomDataMap.put(k, v);
                }
            });
            previewAddRoomImageMap.putAll(otherMainData.getRoomImageMap());
            
            
            previewCartSizeCompare = new Pair<>(
                    oldUserPrivateData.getGoodPrivateDataMap().size(),
                    otherUserPrivateData.getGoodPrivateDataMap().size());
        }
        
        
        public String toDiaglogMessage() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("NoChange Room: " + this.getPreviewNoChangeRoomDataMap().size() + "; \n");
            stringBuilder.append("Add Room: " + this.getPreviewAddRoomDataMap().size() + "; \n");
            stringBuilder.append("Conflict Room: " + this.getPreviewConflictRoomDataMap().size() + "; \n");
            stringBuilder.append("Add Room Image: " + this.getPreviewAddRoomImageMap().size() + "; \n");
            stringBuilder.append("\n");
            stringBuilder.append("NoChange Desk: " + this.getPreviewNoChangeDeskDataMap().size() + "; \n");
            stringBuilder.append("Add Desk: " + this.getPreviewAddDeskDataMap().size() + "; \n");
            stringBuilder.append("ConflictById Desk: " + this.getPreviewConflictByIdDeskDataMap().size() + "; \n");
            stringBuilder.append("ConflictByPos Desk: " + this.getPreviewConflictByPosDeskDataMap().size() + "; \n");
            stringBuilder.append("\n");
            stringBuilder.append("CartSize: " + this.getPreviewCartSizeCompare().x + " -> " + this.getPreviewCartSizeCompare().y + ";");
            return stringBuilder.toString();
        }
        
        
        public void apply() {
            previewConflictByIdDeskDataMap.entrySet().stream()
                    .forEach(it -> {
                        oldComikeData.getDeskExternalRuntimeDataMap().get(it.getKey()).setDeskSaveData(it.getValue().getDeskSaveData());
                    });
            previewConflictByPosDeskDataMap.stream()
                    .forEach(it -> {
                        ExternalDeskData oldData = it.x;
                        ExternalDeskData newData = it.y;
                        assert oldData != null;
                        assert newData != null;
                        oldComikeData.getDeskExternalRuntimeDataMap().put(newData.getDeskSaveData().getIdName(), newData);
                        oldComikeData.getDeskExternalRuntimeDataMap().remove(oldData.getDeskSaveData().getIdName());
                    });
            oldComikeData.getDeskExternalRuntimeDataMap().putAll(previewAddDeskDataMap);
            
            previewConflictRoomDataMap.entrySet().stream()
                    .forEach(it -> {
                        oldComikeData.getExternalMainData().getRoomSaveDataMap().put(it.getKey(), it.getValue());
                    });
            oldComikeData.getExternalMainData().getRoomSaveDataMap().putAll(previewAddRoomDataMap);
            oldComikeData.getExternalMainData().getRoomImageMap().putAll(previewAddRoomImageMap);
            
            oldUserPrivateData.setGoodPrivateDataMap(otherUserPrivateData.getGoodPrivateDataMap());
        }
    }


    public String getExtRoot() {
        return Gdx.files.getExternalStoragePath();
    }

    public void saveAsSharedData(ExternalMainData externalMainData) {
        sharedComplexSaveTool.writeMainData(externalMainData);
    }

    public void saveAsUserData(ExternalUserPrivateData userPrivateData) {
        userPrivateDataSaveTool.writeRootSaveData(userPrivateData);
    }

    public void saveAsSharedData(Map<String, ExternalDeskData> deskDatas) {
        sharedComplexSaveTool.writeAllSubFolderData(deskDatas);
    }

    public List<String> previewAllUnknownSubFolder(Set<String> names) {
        return sharedComplexSaveTool.previewAllUnknownSubFolder(names);
    }

    public void deleteAllUnknownSubFolder(Set<String> names) {
        sharedComplexSaveTool.deleteAllUnknownSubFolder(names);
    }

    public void lazyInitOnCreateStage1() {
        this.defaultCoverFileHandle = Gdx.files.internal("defaultCover.png");

        sharedComplexSaveTool.lazyInitOnGameCreate(defaultCoverFileHandle);
        userPrivateDataSaveTool.lazyInitOnGameCreate();
        sharedExcelSaveTool.lazyInitOnGameCreate();
    }


    
}

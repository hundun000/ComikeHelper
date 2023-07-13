package hundun.tool.logic;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import hundun.tool.ComikeHelperGame;
import hundun.tool.cpp.JsonRootBean;
import hundun.tool.logic.ExternalResourceManager.MergeWorkInProgressModel;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.RoomRuntimeData.Factory;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalComikeData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.external.ExternalUserPrivateData.GoodPrivateData;
import hundun.tool.logic.data.save.RoomSaveData;
import hundun.tool.logic.data.save.RootSaveData;
import hundun.tool.logic.data.save.RootSaveData.MyGameplaySaveData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class LogicContext {
    ComikeHelperGame game;
    @Getter
    String extRoot;
    @Getter
    ExternalResourceManager externalResourceManager;
    @Getter
    List<IModifyGoodTagListener> modifyGoodTagListeners = new ArrayList<>();
    
    /*
     * load时拿到temp，然后计算并更新crossScreenDataPackage;
     * 运行时只读写crossScreenDataPackage;
     * save时用crossScreenDataPackage计算出temp，然后存temp;
     */
    
    @Getter
    CrossScreenDataPackage crossScreenDataPackage;
    private ExternalComikeData tempComikeData;
    private ExternalUserPrivateData tempUserPrivateData;

    public void lazyInitOnCreateStage1() {
        this.extRoot = externalResourceManager.getExtRoot();
        externalResourceManager.lazyInitOnCreateStage1();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class CrossScreenDataPackage {
        ComikeHelperGame game;

        RoomRuntimeData currentRoomData;

        Map<String, RoomRuntimeData> roomMap;
        Map<String, GoodRuntimeData> goodMap;


        Set<GoodRuntimeData> tagedGoods;


    }

    public LogicContext(ComikeHelperGame game) {
        this.game = game;
        this.externalResourceManager = new ExternalResourceManager(game);
    }

    public MergeWorkInProgressModel appendExcelData() {

        MergeWorkInProgressModel model = externalResourceManager.providerExcelGameplayData(tempComikeData, tempUserPrivateData);
        return model;
    }
    
    public void loadEmpty() {
        this.tempComikeData = ExternalComikeData.Factory.empty();
        this.tempUserPrivateData = ExternalUserPrivateData.Factory.empty();
    }

    public static interface IModifyGoodTagListener {
        void onModifyGoodTag(GoodRuntimeData thiz, GoodRuntimeTag tag, boolean setToOn);
    }
    
    public void modifyGoodTag(GoodRuntimeData thiz, GoodRuntimeTag tag, boolean setToOn) {
        
        game.getFrontend().log(this.getClass().getSimpleName(), "tag {0} setToOn = {1}", tag, setToOn);
        
        thiz.getTagStateMap().put(tag, setToOn);
        crossScreenDataPackage.getTagedGoods().add(thiz);

        modifyGoodTagListeners.forEach(it -> it.onModifyGoodTag(thiz, tag, setToOn));
    }
    
    public MergeWorkInProgressModel appendSaveData() {

        MergeWorkInProgressModel model = externalResourceManager.providerExternalGameplayData(tempComikeData, tempUserPrivateData);
        return model;
    }
    
    public MergeWorkInProgressModel appendDefaultData() {

        MyGameplaySaveData gameplaySave = RootSaveData.Extension.genereateStarterGameplaySaveData();
        Map<String, ExternalDeskData> defaultDeskSaveDatas = gameplaySave.getDefaultDeskSaveDatas().entrySet().stream().collect(Collectors.toMap(
            it -> it.getKey(),
            it -> ExternalDeskData.Factory.fromBasic(it.getValue(), externalResourceManager.getDefaultCoverFileHandle())
        ));
        
        MergeWorkInProgressModel model = new MergeWorkInProgressModel(
                tempComikeData, 
                tempUserPrivateData,
                gameplaySave.getDefaultExternalMainData(), 
                defaultDeskSaveDatas,
                gameplaySave.getDefaultUserPrivateData()
                );
        return model;
    }
    
    
    public void saveCurrentSharedData() {
        externalResourceManager.saveAsSharedData(tempComikeData.getExternalMainData());
        externalResourceManager.saveAsSharedData(tempComikeData.getDeskExternalRuntimeDataMap());
    }
    
    @Getter
    @AllArgsConstructor
    public static class CleanUpWorkInProgressModel {
        List<String> deskNames;
        List<String> roomNames;
    }

    public CleanUpWorkInProgressModel previewCleanUp() {
        List<String> deskNames = externalResourceManager.previewAllUnknownSubFolder(tempComikeData.getDeskExternalRuntimeDataMap().keySet());
        List<String> roomNames = tempComikeData.getExternalMainData().getRoomSaveDataMap().keySet().stream()
                .filter(roomIt -> !tempComikeData.getDeskExternalRuntimeDataMap().values().stream()
                            .anyMatch(deskIt -> deskIt.getDeskSaveData().getRoom().equals(roomIt))
                            )
                .collect(Collectors.toList());
        return new CleanUpWorkInProgressModel(deskNames, roomNames);
    }


    public void applyCleanUp() {
        tempComikeData.getExternalMainData().getRoomSaveDataMap().keySet()
                .removeIf(roomIt -> !tempComikeData.getDeskExternalRuntimeDataMap().values().stream()
                            .anyMatch(deskIt -> deskIt.getDeskSaveData().getRoom().equals(roomIt))
                            )
                ;
        externalResourceManager.saveAsSharedData(tempComikeData.getExternalMainData());
        externalResourceManager.deleteAllUnknownSubFolder(tempComikeData.getDeskExternalRuntimeDataMap().keySet());
    }

    public void calculateAndSaveCurrentUserData() {
        Map<String, GoodPrivateData> goodPrivateDataMap = crossScreenDataPackage.getTagedGoods().stream()
                .collect(Collectors.toMap(
                        it -> it.getName(), 
                        it -> GoodPrivateData.builder()
                                .tags(it.getTagStateMap().entrySet().stream()
                                        .filter(entry -> entry.getValue())
                                        .map(entry -> entry.getKey())
                                        .collect(Collectors.toList())
                                        )
                                .build()
                        ));
        
        tempUserPrivateData = ExternalUserPrivateData.builder()
                .goodPrivateDataMap(goodPrivateDataMap)
                .build();
        externalResourceManager.saveAsUserData(tempUserPrivateData);
    }
    
    public void updateCrossScreenDataPackage() {
        Map<String, RoomRuntimeData> roomMap = tempComikeData.getExternalMainData().getRoomSaveDataMap().entrySet().stream()
            .map(entry -> {
                String roomName = entry.getKey();
                RoomSaveData roomSaveData = entry.getValue();
                List<DeskRuntimeData> deskRuntimeDatas = tempComikeData.getDeskExternalRuntimeDataMap().values().stream()
                    .map(deskExternalRuntimeData -> DeskRuntimeData.Factory.fromExternalData(
                                game.getScreenContext().getLayoutConst(),
                                deskExternalRuntimeData
                                )
                    )
                    .filter(deskRuntimeData -> deskRuntimeData.getMainLocation().getRoom().equals(roomSaveData.getName()))
                    .collect(Collectors.toList())
                    ;
                FileHandle image = tempComikeData.getExternalMainData().getRoomImageMap().get(roomName);
                return Factory.fromSaveData(game.getScreenContext().getLayoutConst(), roomSaveData, deskRuntimeDatas, image);
            })
            .collect(Collectors.toMap(
                it -> it.getName(),
                it -> it
            ));
        Map<String, GoodRuntimeData> goodMap = new HashMap<>();
        roomMap.values().stream()
            .forEach(room ->
                room.getDeskDatas().stream().forEach(deskData ->
                    deskData.getGoodSaveDatas().stream()
                        .forEach(goodSaveData ->
                            goodMap.put(goodSaveData.getName(), goodSaveData)
                        )
                )
            );


        this.crossScreenDataPackage = CrossScreenDataPackage.builder()
            .game(game)
            .roomMap(roomMap)
            .goodMap(goodMap)
            .tagedGoods(tempUserPrivateData.getGoodPrivateDataMap().entrySet().stream()
                    .filter(it -> goodMap.containsKey(it.getKey()))
                    .map(it -> {
                        GoodRuntimeData result = goodMap.get(it.getKey());
                        result.lazyInit(tempUserPrivateData);
                        return result;
                    })
                    .collect(Collectors.toSet())
            )
            .build();

        crossScreenDataPackage.currentRoomData = crossScreenDataPackage.getRoomMap().values().stream().findFirst().orElse(null);

    }

    public MergeWorkInProgressModel appendCppBean(JsonRootBean cppBean) {
        // TODO Auto-generated method stub
        return appendDefaultData();
    }

}

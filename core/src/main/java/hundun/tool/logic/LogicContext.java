package hundun.tool.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubSystemSettingSaveHandler;
import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubGameplaySaveHandler;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.ExternalResourceManager.MergeWorkInProgressModel;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.RoomRuntimeData.Factory;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalComikeData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
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


        List<GoodRuntimeData> cartGoods;


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
        if (setToOn) {
            if (!thiz.getTags().contains(GoodRuntimeTag.IN_CART)) {
                thiz.getTags().add(GoodRuntimeTag.IN_CART);
                crossScreenDataPackage.getCartGoods().add(thiz);
            }
        } else {
            if (thiz.getTags().contains(GoodRuntimeTag.IN_CART)) {
                thiz.getTags().remove(GoodRuntimeTag.IN_CART);
                crossScreenDataPackage.getCartGoods().remove(thiz);
            }
        }
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

    public void calculateAndSaveCurrentUserData() {
        tempUserPrivateData = ExternalUserPrivateData.builder()
                .cartGoodIds(crossScreenDataPackage.getCartGoods().stream().map(it -> it.getName()).collect(Collectors.toList()))
                .build();
        externalResourceManager.saveAsUserData(tempUserPrivateData);
    }
    
    public void updateCrossScreenDataPackage() {
        Map<String, RoomRuntimeData> roomMap = tempComikeData.getExternalMainData().getRoomSaveDataMap().values().stream()
            .map(roomSaveData -> {
                List<DeskRuntimeData> deskRuntimeDatas = tempComikeData.getDeskExternalRuntimeDataMap().values().stream()
                    .map(deskExternalRuntimeData -> DeskRuntimeData.Factory.fromExternalRuntimeData(
                                game.getScreenContext().getLayoutConst(),
                                deskExternalRuntimeData
                                )
                    )
                    .filter(deskRuntimeData -> deskRuntimeData.getLocation().getRoom().equals(roomSaveData.getName()))
                    .collect(Collectors.toList())
                    ;
                return Factory.fromSaveData(game.getScreenContext().getLayoutConst(), roomSaveData, deskRuntimeDatas);
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
            .cartGoods(tempUserPrivateData.getCartGoodIds().stream()
                    .filter(it -> goodMap.containsKey(it))
                    .map(it -> {
                        GoodRuntimeData result = goodMap.get(it);
                        result.lazyInit(tempUserPrivateData);
                        return result;
                    })
                    .collect(Collectors.toList())
            )
            .build();

        crossScreenDataPackage.currentRoomData = crossScreenDataPackage.getRoomMap().values().stream().findFirst().orElse(null);

    }

}

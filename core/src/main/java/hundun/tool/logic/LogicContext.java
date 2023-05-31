package hundun.tool.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubSystemSettingSaveHandler;
import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubGameplaySaveHandler;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.RoomRuntimeData.Factory;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalAllData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.save.RootSaveData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
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
    CrossScreenDataPackage crossScreenDataPackage;
    @Getter
    ExternalResourceManager externalResourceManager;

    private ExternalAllData externalAllData;
    private ExternalUserPrivateData userPrivateData;

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
        DeskRuntimeData detailingDeskData;


    }

    public LogicContext(ComikeHelperGame game) {
        this.game = game;
        this.externalResourceManager = new ExternalResourceManager(game);
    }

    public void loadExcelData() {
        this.externalAllData = ExternalAllData.Factory.empty();
        this.userPrivateData = ExternalUserPrivateData.Factory.empty();

        boolean success = externalResourceManager.providerExcelGameplayData(externalAllData, userPrivateData);
        if (success) {
            handleFinalData(externalAllData, userPrivateData);
        } else {
            game.getFrontend().log(this.getClass().getSimpleName(), "providerExcelGameplayData failed.");
        }
    }


    public void loadCurrentOrUseDefaultData() {

        
        this.externalAllData = ExternalAllData.Factory.empty();
        this.userPrivateData = ExternalUserPrivateData.Factory.empty();


        externalResourceManager.providerExternalGameplayData(externalAllData, userPrivateData);
        
        boolean useDefault = externalAllData.getExternalMainData().getRoomSaveDataMap().size() == 0;
        if (useDefault) {
            MyGameplaySaveData gameplaySave = RootSaveData.Extension.genereateStarterGameplaySaveData();
            externalAllData.setExternalMainData(gameplaySave.getDefaultExternalMainData());
            Map<String, ExternalDeskData> defaultDeskSaveDatas = gameplaySave.getDefaultDeskSaveDatas().entrySet().stream().collect(Collectors.toMap(
                it -> it.getKey(),
                it -> ExternalDeskData.Factory.forDefault(externalResourceManager.getDefaultCoverFileHandle(), it.getValue())
            ));
            externalAllData.setDeskExternalRuntimeDataMap(defaultDeskSaveDatas);
            userPrivateData.setCartGoodIds(gameplaySave.getDefaultCartGoodIds());
            
            saveCurrent();
        }
        
        handleFinalData(externalAllData, userPrivateData);
    }
    
    
    public void saveCurrent() {
        externalResourceManager.saveAsSharedData(externalAllData.getExternalMainData());
        externalResourceManager.saveAsSharedData(externalAllData.getDeskExternalRuntimeDataMap());
        externalResourceManager.saveAsUserData(userPrivateData);
    }


    private void handleFinalData(ExternalAllData externalAllData, ExternalUserPrivateData userPrivateData) {
        Map<String, RoomRuntimeData> roomMap = externalAllData.getExternalMainData().getRoomSaveDataMap().values().stream()
            .map(roomSaveData -> {
                List<DeskRuntimeData> deskRuntimeDatas = externalAllData.getDeskExternalRuntimeDataMap().values().stream()
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
            .cartGoods(userPrivateData.getCartGoodIds().stream()
                .map(it -> {
                    GoodRuntimeData result = goodMap.get(it);
                    result.getTags().add(GoodRuntimeTag.IN_CART);
                    return result;
                })
                .collect(Collectors.toList())
            )
            .build();

        crossScreenDataPackage.currentRoomData = crossScreenDataPackage.getRoomMap().values().stream().findFirst().get();

    }

}

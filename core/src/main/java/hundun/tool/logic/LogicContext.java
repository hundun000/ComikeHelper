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
import hundun.tool.logic.data.RootSaveData;
import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
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
public class LogicContext implements ISubGameplaySaveHandler<MyGameplaySaveData>, ISubSystemSettingSaveHandler<RootSaveData.MySystemSettingSaveData> {
    ComikeHelperGame game;
    @Getter
    String extRoot;
    @Getter
    CrossScreenDataPackage crossScreenDataPackage;
    @Getter
    ExternalResourceManager externalResourceManager;


    @Override
    public void applySystemSetting(RootSaveData.MySystemSettingSaveData systemSettingSave) {
        this.extRoot = externalResourceManager.getExtRoot();
    }

    @Override
    public void currentSituationToSystemSetting(RootSaveData.MySystemSettingSaveData systemSettingSave) {

    }

    public void lazyInitOnGameCreateStage3() {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class CrossScreenDataPackage {
        ComikeHelperGame game;

        private float currentCameraX;
        private float currentCameraY;
        private float currentCameraZoomWeight;
        RoomRuntimeData currentRoomData;

        Map<String, RoomRuntimeData> roomMap;
        Map<String, GoodRuntimeData> goodMap;


        List<GoodRuntimeData> cartGoods;
        DeskRuntimeData detailingDeskData;

        public void modifyCurrentCamera(Float deltaX, Float deltaY) {
            if (deltaX != null) {
                currentCameraX += deltaX;
            }
            if (deltaY != null) {
                currentCameraY += deltaY;
            }
        }

        public void modifyCurrentCameraZoomWeight(Float delta) {
            currentCameraZoomWeight += delta;
        }

    }

    public LogicContext(ComikeHelperGame game) {
        this.game = game;
        this.externalResourceManager = new ExternalResourceManager();

    }



    @Override
    public void applyGameplaySaveData(MyGameplaySaveData gameplaySave) {

        Map<String, RoomRuntimeData> roomMap = gameplaySave.getRoomSaveDatas().stream()
                .map(it -> RoomRuntimeData.Factory.fromSaveData(game.getScreenContext().getLayoutConst(), it))
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
                .cartGoods(gameplaySave.getCartGoodIds().stream()
                        .map(it -> {
                            GoodRuntimeData result = goodMap.get(it);
                            result.getTags().add(GoodRuntimeTag.IN_CART);
                            return result;
                            })
                        .collect(Collectors.toList())
                        )
                .build();

        crossScreenDataPackage.currentRoomData = crossScreenDataPackage.getRoomMap().values().iterator().next();

        externalResourceManager.lazyInitOnGameCreate();
    }

    @Override
    public void currentSituationToGameplaySaveData(MyGameplaySaveData gameplaySave) {

    }
}

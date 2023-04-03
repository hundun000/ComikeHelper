package hundun.tool.logic;

import java.util.Map;
import java.util.stream.Collectors;

import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubGameplaySaveHandler;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.RoomRuntimeData;
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
public class ManagerContext implements ISubGameplaySaveHandler<MyGameplaySaveData> {
    ComikeHelperGame game;

    @Getter
    CrossScreenDataPackage crossScreenDataPackage;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class CrossScreenDataPackage {

        private float currentCameraX;
        private float currentCameraY;
        private float currentCameraZoomPower;
        RoomRuntimeData currentRoomData;
        
        Map<String, RoomRuntimeData> roomMap;
        
        public void modifyCurrentCamera(Float deltaX, Float deltaY) {
            if (deltaX != null) {
                currentCameraX += deltaX;
            }
            if (deltaY != null) {
                currentCameraY += deltaY;
            }
        }
        
        public void modifyCurrentCameraZoomPower(Float delta) {
            currentCameraZoomPower += delta;
        }
    }
    
    public ManagerContext(ComikeHelperGame game) {
        this.game = game;
        
        
    }
    
    public void lazyInitOnGameCreate() {
        game.getSaveHandler().registerSubHandler(this);
        
        
    }

    @Override
    public void applyGameplaySaveData(MyGameplaySaveData gameplaySave) {
         this.crossScreenDataPackage = CrossScreenDataPackage.builder()
                 .roomMap(gameplaySave.getRoomSaveDatas().stream()
                         .map(it -> RoomRuntimeData.Factory.fromSaveData(it))
                         .collect(Collectors.toMap(
                                 it -> it.getName(), 
                                 it -> it
                                 ))
                         )
                 .build();
         
         crossScreenDataPackage.currentRoomData = crossScreenDataPackage.getRoomMap().values().iterator().next();
    }

    @Override
    public void currentSituationToGameplaySaveData(MyGameplaySaveData gameplaySave) {
        
    }
}

package hundun.tool.logic;

import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler.ISubGameplaySaveHandler;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
import lombok.AccessLevel;
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

    public ManagerContext(ComikeHelperGame game) {
        this.game = game;
        
        
    }
    
    public void lazyInitOnGameCreate() {
        game.getSaveHandler().registerSubHandler(this);
        
        
    }

    @Override
    public void applyGameplaySaveData(MyGameplaySaveData gameplaySave) {
         
    }

    @Override
    public void currentSituationToGameplaySaveData(MyGameplaySaveData gameplaySave) {
        
    }
}

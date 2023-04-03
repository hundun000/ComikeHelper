package hundun.tool.libgdx.screen;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.market.PlayScreenLayoutConst;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/02
 */
@Getter
public class ScreenContext {
    MarketScreen mainScreen;
    MyMenuScreen menuScreen;

    public ScreenContext(ComikeHelperGame game) {
        
    }
    
    public void lazyInit(ComikeHelperGame game) {
        this.mainScreen = new MarketScreen(game, new PlayScreenLayoutConst());
        this.menuScreen = new MyMenuScreen(game);
        
        game.getScreenManager().addScreen(mainScreen.getClass().getSimpleName(), mainScreen);
        game.getScreenManager().addScreen(menuScreen.getClass().getSimpleName(), menuScreen);
        
        BlendingTransition blendingTransition = new BlendingTransition(game.getBatch(), 1F);
        game.getScreenManager().addScreenTransition(BlendingTransition.class.getSimpleName(), blendingTransition);
    }
}

package hundun.tool.libgdx.screen;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.tool.ComikeHelperGame;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/02
 */
@Getter
public class ScreenContext {
    MarketScreen mainScreen;
    MyMenuScreen menuScreen;
    BuilderScreen builderScreen;
    LayoutConst layoutConst;


    public ScreenContext(ComikeHelperGame game) {
        
    }
    
    public void lazyInit(ComikeHelperGame game) {
        this.layoutConst = new LayoutConst();
        this.mainScreen = new MarketScreen(game);
        this.menuScreen = new MyMenuScreen(game);
        this.builderScreen = new BuilderScreen(game);
        
        game.getScreenManager().addScreen(mainScreen.getClass().getSimpleName(), mainScreen);
        game.getScreenManager().addScreen(menuScreen.getClass().getSimpleName(), menuScreen);
        game.getScreenManager().addScreen(builderScreen.getClass().getSimpleName(), builderScreen);

        BlendingTransition blendingTransition = new BlendingTransition(game.getBatch(), 1F);
        game.getScreenManager().addScreenTransition(BlendingTransition.class.getSimpleName(), blendingTransition);
    
        
    }
}

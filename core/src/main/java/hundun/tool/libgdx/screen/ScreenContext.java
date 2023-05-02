package hundun.tool.libgdx.screen;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.mainscreen.MainScreen;
import hundun.tool.libgdx.screen.mainscreen.PlayScreenLayoutConst;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/11/02
 */
@Getter
public class ScreenContext {
    MainScreen mainScreen;


    public ScreenContext(ComikeHelperGame game) {
        
    }
    
    public void lazyInit(ComikeHelperGame game) {
        this.mainScreen = new MainScreen(game, new PlayScreenLayoutConst());

        
        game.getScreenManager().addScreen(mainScreen.getClass().getSimpleName(), mainScreen);

        BlendingTransition blendingTransition = new BlendingTransition(game.getBatch(), 1F);
        game.getScreenManager().addScreenTransition(BlendingTransition.class.getSimpleName(), blendingTransition);
    }
}

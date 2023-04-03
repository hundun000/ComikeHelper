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
    LayoutConst layoutConst;
    
    public static class LayoutConst {
        public int GOOD_IMAGE_SIZE = 200;
        public int GOOD_NODE_HEIGHT = 250;
        public int GOOD_NODE_WIDTH = 500;

        public int CART_BOARD_WIDTH = 600;
        
    }
    
    public ScreenContext(ComikeHelperGame game) {
        
    }
    
    public void lazyInit(ComikeHelperGame game) {
        this.layoutConst = new LayoutConst();
        this.mainScreen = new MarketScreen(game);
        this.menuScreen = new MyMenuScreen(game);
        
        game.getScreenManager().addScreen(mainScreen.getClass().getSimpleName(), mainScreen);
        game.getScreenManager().addScreen(menuScreen.getClass().getSimpleName(), menuScreen);
        
        BlendingTransition blendingTransition = new BlendingTransition(game.getBatch(), 1F);
        game.getScreenManager().addScreenTransition(BlendingTransition.class.getSimpleName(), blendingTransition);
    }
}

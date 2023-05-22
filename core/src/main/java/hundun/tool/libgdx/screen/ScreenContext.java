package hundun.tool.libgdx.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Value;

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
        public final int DESK_SMALL_COL_PADDING = 30;
        public final int DESK_BIG_COL_PADDING = 200;
        public final int DESK_WIDTH = 400;
        public final int DESK_HEIGHT = 200;
        
        public final int DESK_STAR_SIZE = 50;
        public final int GOOD_IMAGE_SIZE = 200;
        public final int GOOD_NODE_HEIGHT = GOOD_IMAGE_SIZE + 50;
        public final int GOOD_NODE_WIDTH = 300;

        public final int CART_BOARD_EXTRA_AREA_WIDTH = 500;
        public final int CART_BOARD_EXTRA_IMAGE_SIZE = 300;

        public final int CART_BOARD_WIDTH = 600;
        
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

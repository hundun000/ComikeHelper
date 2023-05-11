package hundun.tool.libgdx.screen.market;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;

/**
 * @author hundun
 * Created on 2023/05/10
 */
public class CartGoodVM extends Table {
    
    MarketScreen screen;
    
    public CartGoodVM(MarketScreen screen, GoodRuntimeData it) {
        this.add(new Image(new Texture(Gdx.files.internal("badlogic.jpg"))))
                .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_IMAGE_SIZE)
                .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_IMAGE_SIZE)
                ;
        this.add(new Label(it.getName(), screen.getGame().getMainSkin()));
        this.setBackground(DrawableFactory.getSimpleBoardBackground(
                screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH, 
                screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT
                ));
    }
}

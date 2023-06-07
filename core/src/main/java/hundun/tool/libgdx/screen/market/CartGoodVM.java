package hundun.tool.libgdx.screen.market;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;
import java.util.Map;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.MainBoardVM.MainBoardState;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;

/**
 * @author hundun
 * Created on 2023/05/10
 */
public class CartGoodVM extends Table {
    
    MarketScreen screen;
    Table tagImageTable;
    Image mainImage;

    public CartGoodVM(MarketScreen screen, GoodRuntimeData goodRuntimeData) {
        this.screen = screen;


        init(goodRuntimeData);
        update(goodRuntimeData);
    }

    private void init(GoodRuntimeData goodRuntimeData) {
        
        // ------ row 1 -----
        this.add(new Label(goodRuntimeData.getName(), screen.getGame().getMainSkin()))
                .colspan(2)
                .row()
        ;
        // ------ row 2 -----
        Texture texture = new Texture(goodRuntimeData.getOwnerRef().getCoverFileHandle());
        Image image = new Image(texture);
        this.add(image)
                .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_IMAGE_SIZE)
                .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_IMAGE_SIZE)
        ;
        tagImageTable = new Table();
        this.add(tagImageTable)
                .grow()
                ;


        this.setBackground(DrawableFactory.getSimpleBoardBackground());
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getMainBoardVM().setState(MainBoardState.GOOD);
                screen.getMainBoardVM().setDetailingGoodData(goodRuntimeData);
                screen.getMainBoardVM().updateByState(true);
            }
        });

    }

    public void update(GoodRuntimeData goodRuntimeData) {
        tagImageTable.clear();
        goodRuntimeData.getTagStateMap().forEach((tag, state) -> {
            if (state) {
                Texture texture = screen.getGame().getTextureManager().getTagImageMap().get(tag);
                tagImageTable.add(new Image(texture));
            }
        });


    }

}

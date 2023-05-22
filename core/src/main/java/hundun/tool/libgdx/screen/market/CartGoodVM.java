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
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;

/**
 * @author hundun
 * Created on 2023/05/10
 */
public class CartGoodVM extends Table {
    static Map<GoodRuntimeTag, Texture> tagImageMap = new HashMap<>();
    static {
        tagImageMap.put(GoodRuntimeTag.IN_CART, new Texture(Gdx.files.internal("star.png")));
    }
    MarketScreen screen;
    Table tagImageTable;
    Image mainImage;

    public CartGoodVM(MarketScreen screen, GoodRuntimeData goodRuntimeData) {
        this.screen = screen;


        init(goodRuntimeData);
        update(goodRuntimeData);
    }

    private void init(GoodRuntimeData goodRuntimeData) {
        tagImageTable = new Table();
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
        this.add(tagImageTable)
                .grow()
                ;


        this.setBackground(DrawableFactory.getSimpleBoardBackground(
                screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH,
                screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT
        ));


    }

    public void update(GoodRuntimeData goodRuntimeData) {
        tagImageTable.clear();
        goodRuntimeData.getTags().forEach(it -> {
            if (tagImageMap.containsKey(it)) {
                tagImageTable.add(new Image(tagImageMap.get(it)));
            }
        });


    }

}

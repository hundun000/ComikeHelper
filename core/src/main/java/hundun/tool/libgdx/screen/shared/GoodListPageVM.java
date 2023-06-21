package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.List;

import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.CartGoodVM;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.GoodRuntimeData;

public class GoodListPageVM extends Table {
    MarketScreen marketScreen;
    Table goodsTable;

    public GoodListPageVM(MarketScreen screen) {
        this.marketScreen = screen;

        MyWindow container = new MyWindow("作品列表", screen.getGame());
        this.goodsTable = new Table();
        ScrollPane scrollPane = new ScrollPane(goodsTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        container.addToMain(scrollPane);
        this.add(container)
                .grow();
    }

    public void updateGoods(List<GoodRuntimeData> needShowList) {
        goodsTable.clear();
        needShowList.forEach(it -> {
            CartGoodVM node = new CartGoodVM(marketScreen, it);
            goodsTable.add(node)
                    .width(marketScreen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                    .height(marketScreen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT)
                    .padBottom(marketScreen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD)
                    .row();
        });
    }

}

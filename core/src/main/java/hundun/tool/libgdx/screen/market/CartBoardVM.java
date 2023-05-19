package hundun.tool.libgdx.screen.market;
/**
 * @author hundun
 * Created on 2023/05/10
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;

public class CartBoardVM extends Table {

    MarketScreen screen;
    //List<CartGoodVM> nodes = new ArrayList<>();

    Table childrenTable;
    Label title;
    TextButton clearButton;

    public CartBoardVM(MarketScreen screen) {
        this.screen = screen;

        this.childrenTable = new Table();
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);

        this.title = new Label("TODO", screen.getGame().getMainSkin());
        this.clearButton = new TextButton("clear", screen.getGame().getMainSkin());
        clearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.setCartBoardVMDirty(true);
                screen.getGame().getLogicContext().getCrossScreenDataPackage().setDetailingDeskData(null);
            }
        });

        this.add(title);
        this.add(clearButton);
        this.row();
        this.add(scrollPane)
                //.width(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                //.height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT * 3)
                .grow()
                ;

        this.setBackground(DrawableFactory.getSimpleBoardBackground());
    }


    public void updateData(DeskRuntimeData detailingDeskData, List<GoodRuntimeData> cartGoods) {
        //nodes.clear();
        childrenTable.clear();

        List<GoodRuntimeData> needShowList = new ArrayList<>();
        if (detailingDeskData != null) {
            title.setText("goods of " + detailingDeskData.getName());
            clearButton.setVisible(true);
            needShowList.addAll(detailingDeskData.getGoodSaveDatas());
        } else {
            title.setText("goods of 心愿单");
            clearButton.setVisible(false);
            needShowList.addAll(cartGoods);
        }


        needShowList.forEach(it -> {
            CartGoodVM node = new CartGoodVM(screen, it);
            childrenTable.add(node).row();
        });
    }
}

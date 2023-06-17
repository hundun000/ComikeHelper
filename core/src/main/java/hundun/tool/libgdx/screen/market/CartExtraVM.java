package hundun.tool.libgdx.screen.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.MainBoardVM.MainBoardState;
import hundun.tool.libgdx.screen.shared.ImageBoxVM;
import hundun.tool.libgdx.screen.shared.MyWindow;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;

public class CartExtraVM extends MyWindow {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    
    
    Container<Table> currentTableContainer;
    Map<CartExtraState, Table> pageRootTableMap = new HashMap<>();
    //Table page1RootTable;
    Table goodsTable;

    
    public enum CartExtraState {
        PAGE1
        ;
    }
    
    public CartExtraVM(MarketScreen screen) {

        init("CartExtra", screen.getGame());
        //this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.screen = screen;

        
        this.currentTableContainer = new Container<>();
        currentTableContainer.fill();
        currentTableContainer.pad(25);
        
        TextButton button;
        HorizontalGroup horizontalGroup = new HorizontalGroup();

        

        button = new TextButton("page1", screen.getGame().getMainSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                updateByState(CartExtraState.PAGE1);
            }
        });
        horizontalGroup.addActor(button);

        this.addToMain(horizontalGroup);

        this.rowToMain();
        
        this.addToMain(currentTableContainer)
                .width(screen.getGame().getScreenContext().getLayoutConst().ANY_EXTRA_TOTAL_WIDTH)
                .growY()
                ;
        
        // ----- candidates ------
        Table page1RootTable = new Table();
        pageRootTableMap.put(CartExtraState.PAGE1, page1RootTable);
        //page1Table.setBackground(screen.getGame().getTextureManager().getMcStyleTable());
        MyWindow goodsTableContainer = new MyWindow("goods", screen.getGame());
        this.goodsTable = new Table();
        ScrollPane goodsScrollPane = new ScrollPane(goodsTable, screen.getGame().getMainSkin());
        goodsScrollPane.setScrollingDisabled(true, false);
        goodsScrollPane.setFadeScrollBars(false);
        goodsScrollPane.setForceScroll(true, false);
        goodsTableContainer.addToMain(goodsScrollPane);
        page1RootTable.add(goodsTableContainer)
                .grow();

        
    }


    private void updateByState(CartExtraState state) {
        currentTableContainer.setActor(pageRootTableMap.get(state));
    }
    
    public void updateForShow(String title, DeskRuntimeData detailingDeskData) {

        this.getTitleLabel().setText(title);
        
        updateByState(CartExtraState.PAGE1);
    }
    
    public void updateGoods(List<GoodRuntimeData> needShowList) {
        goodsTable.clear();
        needShowList.forEach(it -> {
            CartGoodVM node = new CartGoodVM(screen, it);
            goodsTable.add(node)
                    .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                    .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT)
                    .padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD)
                    .row();
        });

        
    }
}

package hundun.tool.libgdx.screen.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.shared.MyWindow;
import hundun.tool.libgdx.screen.shared.RoomSwitchNode;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;

public class CartExtraMainBoardVM extends MyWindow {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    
    
    Container<Table> currentTableContainer;
    Map<CartExtraState, Table> pageRootTableMap = new HashMap<>();
    //Table page1RootTable;
    Table goodsTable;
    VerticalGroup roomsTable;
    
    public enum CartExtraState {
        PAGE1,
        PAGE2
        ;
    }
    
    public CartExtraMainBoardVM(MarketScreen screen) {

        init("CartExtra", screen.getGame());
        //this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.screen = screen;

        
        this.currentTableContainer = new Container<>();
        currentTableContainer.fill();
        currentTableContainer.pad(25);

        HorizontalGroup horizontalGroup = new HorizontalGroup();


        {
            TextButton button = new TextButton("心愿单", screen.getGame().getMainSkin());
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    updateByState(CartExtraState.PAGE1);
                }
            });
            horizontalGroup.addActor(button);
        }
        {
            TextButton button = new TextButton("切换展馆", screen.getGame().getMainSkin());
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    updateByState(CartExtraState.PAGE2);
                }
            });
            horizontalGroup.addActor(button);
        }

        this.addToMain(horizontalGroup);

        this.rowToMain();
        
        this.addToMain(currentTableContainer)
                .width(screen.getGame().getScreenContext().getLayoutConst().ANY_EXTRA_TOTAL_WIDTH)
                .growY()
                ;
        
        // ----- candidates ------
        {
            Table pageRootTable = new Table();
            pageRootTableMap.put(CartExtraState.PAGE1, pageRootTable);
            MyWindow container = new MyWindow("goods", screen.getGame());
            this.goodsTable = new Table();
            ScrollPane scrollPane = new ScrollPane(goodsTable, screen.getGame().getMainSkin());
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false, true);
            container.addToMain(scrollPane);
            pageRootTable.add(container)
                    .grow();
        }
        {
            Table pageRootTable = new Table();
            pageRootTableMap.put(CartExtraState.PAGE2, pageRootTable);
            this.roomsTable = new VerticalGroup();
            roomsTable.padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD);

            MyWindow container = new MyWindow("rooms", screen.getGame());
            ScrollPane scrollPane = new ScrollPane(roomsTable, screen.getGame().getMainSkin());
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false, true);
            container.addToMain(scrollPane);
            pageRootTable.add(container)
                    .grow();
        }

    }


    private void updateByState(CartExtraState state) {
        currentTableContainer.setActor(pageRootTableMap.get(state));
    }
    
    public void updateForShow() {

        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        roomsTable.clear();
        crossScreenDataPackage.getRoomMap().values().forEach(it -> {
            RoomSwitchNode node = new RoomSwitchNode(screen);
            node.update(it, it == crossScreenDataPackage.getCurrentRoomData());
            roomsTable.addActor(node);
        });

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

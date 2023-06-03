package hundun.tool.libgdx.screen.market;
/**
 * @author hundun
 * Created on 2023/05/10
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import lombok.Setter;

public class MainBoardVM extends Table {

    MarketScreen screen;
    //List<CartGoodVM> nodes = new ArrayList<>();

    Table childrenTable;
    Label title;
    TextButton clearButton;

    Container<Table> extraArea;

    DeskExtraVM deskExtraVM;
    GoodExtraVM goodExtraVM;
    @Setter
    MainBoardState state;
    @Setter
    DeskRuntimeData detailingDeskData;
    @Setter
    GoodRuntimeData detailingGoodData;
    
    public enum MainBoardState {
        DESK,
        CART,
        GOOD,
        ;
    }
    
    public MainBoardVM(MarketScreen screen) {
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
                switch (state) {
                    case DESK:
                        setState(MainBoardState.CART);
                        updateByState();
                        break;
                    case GOOD:
                        if (detailingDeskData != null) {
                            setState(MainBoardState.DESK);
                        } else {
                            setState(MainBoardState.CART);
                        }
                        updateByState();
                        break;
                    default:
                        break;
                }
            }
        });

        this.extraArea = new Container<>();

        Table rightPart = new Table();
        rightPart.add(title);
        rightPart.add(clearButton);
        rightPart.row();
        rightPart.add(extraArea)
                .colspan(2)
                .grow();


        this.add(scrollPane)
                //.height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT * 3)
                .growY()
                ;
        this.add(rightPart)
                .width(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_AREA_WIDTH)
                .growY()
        ;
        this.setBackground(DrawableFactory.getSimpleBoardBackground());

        // ----- candidates ------
        this.deskExtraVM = new DeskExtraVM(screen);
        this.goodExtraVM = new GoodExtraVM(screen);
    }

    public void name() {
        
    }
    
    private void updateAsDetailingDesk(DeskRuntimeData detailingDeskData) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();
        Table newExtra;

        title.setText(detailingDeskData.getName());
        clearButton.setVisible(true);
        needShowList.addAll(detailingDeskData.getGoodSaveDatas());
        newExtra = deskExtraVM;
        deskExtraVM.updateData(detailingDeskData);

        updateCore(needShowList, newExtra);
    }
    
    private void updateAsDetailingGood(GoodRuntimeData detailingGood) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();
        Table newExtra;

        title.setText(detailingGood.getName());
        clearButton.setVisible(true);
        needShowList.add(detailingGood);
        newExtra = goodExtraVM;
        goodExtraVM.updateData(detailingGood);

        updateCore(needShowList, newExtra);
    }
    
    private void updateCore(List<GoodRuntimeData> needShowList, Table newExtra) {
        childrenTable.clear();
        needShowList.forEach(it -> {
            CartGoodVM node = new CartGoodVM(screen, it);
            childrenTable.add(node)
                    .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                    .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT)
                    .padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD)
                    .row();
        });

        extraArea.setActor(newExtra);
    }
    

    private void updateAsCart(List<GoodRuntimeData> cartGoods) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();
        Table newExtra;

        title.setText("心愿单");
        clearButton.setVisible(false);
        needShowList.addAll(cartGoods);
        newExtra = null;

        updateCore(needShowList, newExtra);
    }

    public void updateByState() {
        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        switch (state) {
            case CART:
                updateAsCart(crossScreenDataPackage.getCartGoods());
                break;
            case DESK:
                updateAsDetailingDesk(detailingDeskData);
                break;
            case GOOD:
                updateAsDetailingGood(detailingGoodData);
                break;
            default:
                break;
        }
        
    }
}

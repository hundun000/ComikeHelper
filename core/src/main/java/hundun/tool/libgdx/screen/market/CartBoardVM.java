package hundun.tool.libgdx.screen.market;
/**
 * @author hundun
 * Created on 2023/05/10
 */

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;

public class CartBoardVM extends Table {
    
    MarketScreen screen;
    //List<CartGoodVM> nodes = new ArrayList<>();
    
    Table childrenTable;
    
    public CartBoardVM(MarketScreen screen) {
        this.screen = screen;
        
        childrenTable = new Table();
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        
        this.add(new Label("心愿单", screen.getGame().getMainSkin())).row();
        this.add(scrollPane)
                .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT * 3)
                ;
        
        this.setBackground(DrawableFactory.getSimpleBoardBackground());
    }

    
    public void updateData(List<GoodRuntimeData> list) {
        //nodes.clear();
        childrenTable.clear();
        
        list.forEach(it -> {
            CartGoodVM node = new CartGoodVM(screen, it);
            childrenTable.add(node).row();
            
        });
    }
}

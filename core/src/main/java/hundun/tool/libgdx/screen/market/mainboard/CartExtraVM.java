package hundun.tool.libgdx.screen.market.mainboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.shared.GoodListPageVM;
import hundun.tool.libgdx.screen.shared.BasePageableTable;
import hundun.tool.libgdx.screen.shared.RoomSwitchPageVM;
import hundun.tool.logic.data.GoodRuntimeData;
import lombok.Getter;

public class CartExtraVM extends BasePageableTable {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;

    MarketScreen marketScreen;
    //Table page1RootTable;
    @Getter
    GoodListPageVM goodListPageVM;
    RoomSwitchPageVM roomSwitchPageVM;
    
    private enum CartExtraState {
        PAGE1,
        PAGE2
        ;
    }
    
    public CartExtraVM(MarketScreen screen) {
        super(screen);
        init("CartExtra", screen.getGame());
        //this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.marketScreen = screen;



        {
            goodListPageVM = new GoodListPageVM(screen);


            addPage(CartExtraState.PAGE1.name(),
                    "心愿单",
                    goodListPageVM
                    );
        }
        {
            roomSwitchPageVM = new RoomSwitchPageVM(screen);

            addPage(CartExtraState.PAGE2.name(),
                    "切换展馆",
                    roomSwitchPageVM
            );
        }

    }



    
    public void updateForShow() {

        roomSwitchPageVM.updateData();

        updateByState(CartExtraState.PAGE1.name());
    }
    


}

package hundun.tool.libgdx.screen.market;
/**
 * @author hundun
 * Created on 2023/05/10
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        
       


        this.extraArea = new Container<>();


        
        this.add(extraArea)
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

        //title.setText(detailingDeskData.getName());
        needShowList.addAll(detailingDeskData.getGoodSaveDatas());
        deskExtraVM.updateData(detailingDeskData.getName(), detailingDeskData);
        deskExtraVM.updateCore(needShowList);
        
        extraArea.setActor(deskExtraVM);
    }
    
    private void updateAsDetailingGood(GoodRuntimeData detailingGood) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();


        //title.setText(detailingGood.getName());
        needShowList.add(detailingGood);
        goodExtraVM.updateData(detailingGood);

        extraArea.setActor(goodExtraVM);
    }
    
    
    

    private void updateAsCart(Set<GoodRuntimeData> tagedGood) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();

        //title.setText("心愿单");
        needShowList.addAll(tagedGood);

        extraArea.setActor(null);
    }

    public void updateByState() {
        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        switch (state) {
            case CART:
                updateAsCart(crossScreenDataPackage.getTagedGoods());
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

    public void back() {
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
}

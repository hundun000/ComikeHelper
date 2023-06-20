package hundun.tool.libgdx.screen.market.mainboard;
/**
 * @author hundun
 * Created on 2023/05/10
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import lombok.Setter;

public class MarketMainBoardVM extends Table {

    MarketScreen screen;
    //List<CartGoodVM> nodes = new ArrayList<>();

    

    Container<Table> extraArea;

    DeskExtraVM deskExtraVM;
    GoodExtraVM goodExtraVM;
    CartExtraVM cartExtraVM;
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
    
    public MarketMainBoardVM(MarketScreen screen) {
        this.screen = screen;
        
       


        this.extraArea = new Container<>();
        extraArea.fill();
        extraArea.pad(25);
        
        this.add(extraArea)
                .growY()
                ;
        this.setBackground(screen.getGame().getTextureManager().getMcStyleTable());

        // ----- candidates ------
        this.deskExtraVM = new DeskExtraVM(screen);
        this.goodExtraVM = new GoodExtraVM(screen);
        this.cartExtraVM = new CartExtraVM(screen);
    }

    
    private void updateAsDetailingDesk(DeskRuntimeData detailingDeskData, boolean justChanged) {

        List<GoodRuntimeData> needShowList = new ArrayList<>(detailingDeskData.getGoodSaveDatas());
        
        if (justChanged) {
            deskExtraVM.updateForShow(detailingDeskData.getName(), detailingDeskData);
            deskExtraVM.getGoodListPageVM().updateGoods(needShowList);
            extraArea.setActor(deskExtraVM);
        } else {
            deskExtraVM.getGoodListPageVM().updateGoods(needShowList);
        }
    }
    
    private void updateAsDetailingGood(GoodRuntimeData detailingGood, boolean justChanged) {

        goodExtraVM.updateData(detailingGood);

        extraArea.setActor(goodExtraVM);
    }
    
    
    

    private void updateAsCart(Set<GoodRuntimeData> tagedGood, boolean justChanged) {
        List<GoodRuntimeData> needShowList = new ArrayList<>(tagedGood);

        if (justChanged) {
            cartExtraVM.updateForShow();
            cartExtraVM.getGoodListPageVM().updateGoods(needShowList);
            extraArea.setActor(cartExtraVM);
        } else {
            cartExtraVM.getGoodListPageVM().updateGoods(needShowList);
        }
    }

    public void updateByState(boolean justChanged) {
        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        switch (state) {
            case CART:
                updateAsCart(crossScreenDataPackage.getTagedGoods(), justChanged);
                break;
            case DESK:
                updateAsDetailingDesk(detailingDeskData, justChanged);
                break;
            case GOOD:
                updateAsDetailingGood(detailingGoodData, justChanged);
                break;
            default:
                break;
        }
        
    }

    public void back() {
        switch (state) {
            case DESK:
                setState(MainBoardState.CART);
                updateByState(true);
                break;
            case GOOD:
                if (detailingDeskData != null) {
                    setState(MainBoardState.DESK);
                } else {
                    setState(MainBoardState.CART);
                }
                updateByState(true);
                break;
            default:
                break;
        }    
    }
}

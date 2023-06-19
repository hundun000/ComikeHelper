package hundun.tool.libgdx.screen.market;
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

public class MainBoardVM extends Table {

    MarketScreen screen;
    //List<CartGoodVM> nodes = new ArrayList<>();

    

    Container<Table> extraArea;

    DeskMainBoardExtraVM deskMainBoardExtraVM;
    GoodMainBoardExtraVM goodMainBoardExtraVM;
    CartExtraMainBoardVM cartExtraMainBoardVM;
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
        extraArea.fill();
        extraArea.pad(25);
        
        this.add(extraArea)
                .growY()
                ;
        this.setBackground(screen.getGame().getTextureManager().getMcStyleTable());

        // ----- candidates ------
        this.deskMainBoardExtraVM = new DeskMainBoardExtraVM(screen);
        this.goodMainBoardExtraVM = new GoodMainBoardExtraVM(screen);
        this.cartExtraMainBoardVM = new CartExtraMainBoardVM(screen);
    }

    public void name() {
        
    }
    
    private void updateAsDetailingDesk(DeskRuntimeData detailingDeskData, boolean justChanged) {

        List<GoodRuntimeData> needShowList = new ArrayList<>();
        needShowList.addAll(detailingDeskData.getGoodSaveDatas());
        
        if (justChanged) {
            deskMainBoardExtraVM.updateForShow(detailingDeskData.getName(), detailingDeskData);
            deskMainBoardExtraVM.updateGoods(needShowList);
            extraArea.setActor(deskMainBoardExtraVM);
        } else {
            deskMainBoardExtraVM.updateGoods(needShowList);
        }
    }
    
    private void updateAsDetailingGood(GoodRuntimeData detailingGood, boolean justChanged) {
        List<GoodRuntimeData> needShowList = new ArrayList<>();


        //title.setText(detailingGood.getName());
        needShowList.add(detailingGood);
        goodMainBoardExtraVM.updateData(detailingGood);

        extraArea.setActor(goodMainBoardExtraVM);
    }
    
    
    

    private void updateAsCart(Set<GoodRuntimeData> tagedGood, boolean justChanged) {
        List<GoodRuntimeData> needShowList = new ArrayList<>(tagedGood);

        if (justChanged) {
            cartExtraMainBoardVM.updateForShow();
            cartExtraMainBoardVM.updateGoods(needShowList);
            extraArea.setActor(cartExtraMainBoardVM);
        } else {
            cartExtraMainBoardVM.updateGoods(needShowList);
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

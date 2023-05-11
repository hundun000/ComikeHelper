package hundun.tool.libgdx.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.market.CameraControlBoardVM;
import hundun.tool.libgdx.screen.market.CartBoardVM;
import hundun.tool.libgdx.screen.market.DeskAreaVM;
import hundun.tool.libgdx.screen.market.DeskVM;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.RootSaveData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class MarketScreen extends BaseHundunScreen<ComikeHelperGame, RootSaveData> {

    @Getter
    @Setter
    private boolean currentCameraZoomDirty;
    
    
    
    private final OrthographicCamera deskCamera;
    private final Stage deskStage;
    
    
    private DeskAreaVM deskAreaVM;
    private CameraControlBoardVM cameraControlBoardVM;
    
    private CartBoardVM cartBoardVM;
    
    public MarketScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());
        
        this.deskCamera = new OrthographicCamera();
        this.deskStage = new Stage(new ScreenViewport(deskCamera), game.getBatch());
    }

    @Override
    protected void create() {
        // ------ popup layer ------
        cameraControlBoardVM = new CameraControlBoardVM(this);
        popupRootTable.add(cameraControlBoardVM)
                .expand()
                .bottom()
                ;
        
        // ------ desk layer ------
        deskAreaVM = new DeskAreaVM(this);
        deskStage.addActor(deskAreaVM);
        
        // ------ UI layer ------
        cartBoardVM = new CartBoardVM(this);
        uiRootTable.add(cartBoardVM)
                .growY()
                .expand()
                .right();
    }

    @Override
    public void dispose() {
        
        
    }
    
    @Override
    public void show() {
        super.show();
        
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(popupUiStage);
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(deskStage);
        Gdx.input.setInputProcessor(multiplexer);
        
        //Gdx.input.setInputProcessor(uiStage);
        //game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

        updateUIForShow();


        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }
    
    private void updateUIForShow() {

        // for newest DeskDatas
        deskAreaVM.updateDeskDatas(game.getManagerContext().getCrossScreenDataPackage().getCurrentRoomData().getDeskDatas());
        // for newest cart
        cartBoardVM.updateData(game.getManagerContext().getCrossScreenDataPackage().getCartGoods());
        deskAreaVM.updateCartData(game.getManagerContext().getCrossScreenDataPackage().getCartGoods());
    }

    
    public static class TiledMapClickListener extends ClickListener {
        ComikeHelperGame game;
        private DeskVM vm;

        public TiledMapClickListener(ComikeHelperGame game, DeskVM vm) {
            this.game = game;
            this.vm = vm;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.getFrontend().log(this.getClass().getSimpleName(), vm.getDeskData().getName() + " has been clicked.");
        }
    }


    public static final TextureRegion RED_POINT = new TextureRegion(TextureFactory.createAlphaBoard(3, 3, Color.RED, 1.0f));
    
    @Override
    protected void belowUiStageDraw(float delta) {
        deskStage.act();
        deskStage.getViewport().getCamera().position.set(
                game.getManagerContext().getCrossScreenDataPackage().getCurrentCameraX(), 
                game.getManagerContext().getCrossScreenDataPackage().getCurrentCameraY(), 
                0);
        if (currentCameraZoomDirty) {
            currentCameraZoomDirty = false;
            deskCamera.zoom = (float) Math.pow(2, game.getManagerContext().getCrossScreenDataPackage().getCurrentCameraZoomPower());
        }
        deskStage.getViewport().apply();
        deskStage.draw();
    }

}

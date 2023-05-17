package hundun.tool.libgdx.screen;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.market.CameraControlBoardVM;
import hundun.tool.libgdx.screen.market.CartBoardVM;
import hundun.tool.libgdx.screen.market.DeskAreaVM;
import hundun.tool.libgdx.screen.market.DeskVM;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.GoodRuntimeData;
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

    @Getter
    @Setter
    private boolean cartBoardVMDirty;

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

    public class MyGestureListener extends ActorGestureListener {

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            super.zoom(event, initialDistance, distance);
            float deltaValue = (distance - initialDistance) < 0 ? 0.1f : -0.1f;
            MarketScreen.this.getGame().getLogicContext().getCrossScreenDataPackage().modifyCurrentCameraZoomWeight(deltaValue);
            MarketScreen.this.setCurrentCameraZoomDirty(true);
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            super.pan(event, x, y, deltaX, deltaY);
            float cameraDeltaX = -deltaX;
            float cameraDeltaY = -deltaY;
            MarketScreen.this.getGame().getLogicContext().getCrossScreenDataPackage().modifyCurrentCamera(cameraDeltaX, cameraDeltaY);
        }
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
                .right()
                ;
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
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        // for newest DeskDatas
        RoomRuntimeData currentRoomData = crossScreenDataPackage.getCurrentRoomData();
        deskAreaVM.updateDeskDatas(
                currentRoomData.getRoomWidth(),
                currentRoomData.getRoomHeight(),
                currentRoomData.getDeskDatas()
                );
        // for newest cart
        cartBoardVMDirty = true;
        checkCartBoardVMDirty();
        deskAreaVM.updateCartData(game.getLogicContext().getCrossScreenDataPackage().getCartGoods());
    }

    private void checkCartBoardVMDirty() {
        if (!cartBoardVMDirty) {
            return;
        }
        cartBoardVMDirty = false;
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        List<GoodRuntimeData> showCartGoods = game.getLogicContext().getCrossScreenDataPackage().getCartGoods().stream()
                .filter(it -> crossScreenDataPackage.getDetailingDeskData() == null || it.getOwnerRef() == crossScreenDataPackage.getDetailingDeskData())
                .collect(Collectors.toList());
        cartBoardVM.updateData(crossScreenDataPackage.getDetailingDeskData(), showCartGoods);
    }


    public static class TiledMapClickListener extends ClickListener {
        ComikeHelperGame game;
        MarketScreen screen;
        private DeskVM vm;

        public TiledMapClickListener(MarketScreen screen, DeskVM vm) {
            this.game = screen.game;
            this.screen = screen;
            this.vm = vm;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            screen.setCartBoardVMDirty(true);
            game.getLogicContext().getCrossScreenDataPackage().setDetailingDeskData(vm.getDeskData());
            game.getFrontend().log(this.getClass().getSimpleName(), vm.getDeskData().getName() + " has been clicked.");
        }
    }


    public static final TextureRegion RED_POINT = new TextureRegion(TextureFactory.createAlphaBoard(3, 3, Color.RED, 1.0f));

    @Override
    protected void belowUiStageDraw(float delta) {
        checkCartBoardVMDirty();

        deskStage.act();
        deskStage.getViewport().getCamera().position.set(
                game.getLogicContext().getCrossScreenDataPackage().getCurrentCameraX(),
                game.getLogicContext().getCrossScreenDataPackage().getCurrentCameraY(),
                0);
        if (currentCameraZoomDirty) {
            currentCameraZoomDirty = false;
            float weight = game.getLogicContext().getCrossScreenDataPackage().getCurrentCameraZoomWeight();
            float value = weight <= 0 ? (float)Math.pow(2, weight) : (float)Math.log(weight + 2);
            deskCamera.zoom = value;
            game.getFrontend().log(this.getClass().getSimpleName(), "deskCamera.zoom = %s", deskCamera.zoom);
        }
        deskStage.getViewport().apply();
        deskStage.draw();
    }

}

package hundun.tool.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.market.MainBoardVM;
import hundun.tool.libgdx.screen.market.DeskAreaVM;
import hundun.tool.libgdx.screen.market.DeskVM;
import hundun.tool.libgdx.screen.market.PopupCloseButton;
import hundun.tool.libgdx.screen.market.ImageViewerVM;
import hundun.tool.libgdx.screen.market.RoomSwitchBoardVM;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
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
    private boolean cartBoardVMDirty;


    // ------ desk layer ------
    private final OrthographicCamera deskCamera;
    private final Stage deskStage;
    private DeskAreaVM deskAreaVM;

    // ------ UI layer ------
    private MainBoardVM mainBoardVM;
    private RoomSwitchBoardVM roomSwitchBoardVM;

    // ------ image previewer layer ------
    private final OrthographicCamera imagePreviewerCamera;
    private final Stage imagePreviewerStage;
    @Getter
    private ImageViewerVM imageViewerVM;

    // ------ popup layer ------
    @Getter
    private PopupCloseButton popupCloseButton;

    public MarketScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());

        this.deskCamera = new OrthographicCamera();
        this.deskStage = new Stage(new ScreenViewport(deskCamera), game.getBatch());

        this.imagePreviewerCamera = new OrthographicCamera();
        this.imagePreviewerStage = new Stage(new ScreenViewport(imagePreviewerCamera), game.getBatch());
    }



    @Override
    protected void create() {


        // ------ desk layer ------
        deskAreaVM = new DeskAreaVM(this);
        deskStage.addActor(deskAreaVM);

        // ------ UI layer ------
        roomSwitchBoardVM = new RoomSwitchBoardVM(this);
        uiRootTable.add(roomSwitchBoardVM)
                .expand()
                //.grow()
                .left()
                .top()
                ;

        mainBoardVM = new MainBoardVM(this);
        uiRootTable.add(mainBoardVM)
                //.expand()
                .growY()
                .right()
                ;
        // ------ image previewer layer ------
        imageViewerVM = new ImageViewerVM(this);
        imagePreviewerStage.addActor(imageViewerVM);

        // ------ popup layer ------
        popupCloseButton = new PopupCloseButton(this);
        popupRootTable.add(popupCloseButton)
                .expand()
                .right()
                .top()
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
        multiplexer.addProcessor(imagePreviewerStage);
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(deskStage);
        Gdx.input.setInputProcessor(multiplexer);

        //Gdx.input.setInputProcessor(uiStage);
        //game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

        updateUIForShow();


        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }

    private void updateUIForShow() {
        popupCloseButton.hide();
        imageViewerVM.hide();
        roomSwitchBoardVM.intoSmallMode();

        deskAreaVM.getCameraDataPackage().forceSet(null, null, 0);

        updateUIAfterRoomChanged();
    }

    public void updateUIAfterRoomChanged() {
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        // for newest DeskDatas
        RoomRuntimeData currentRoomData = crossScreenDataPackage.getCurrentRoomData();
        deskAreaVM.updateDeskDatas(
                currentRoomData.getRoomWidth(),
                currentRoomData.getRoomHeight(),
                currentRoomData.getDeskDatas()
        );
        roomSwitchBoardVM.intoFullMode();
        // for newest cart
        cartBoardVMDirty = true;
        checkCartBoardVMDirty();
    }

    private void checkCartBoardVMDirty() {
        if (!cartBoardVMDirty) {
            return;
        }
        cartBoardVMDirty = false;
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        mainBoardVM.updateData(crossScreenDataPackage.getDetailingDeskData(), crossScreenDataPackage.getCartGoods());
        deskAreaVM.updateCartData(crossScreenDataPackage.getCartGoods());
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

    private void logicOnDraw() {
        checkCartBoardVMDirty();
    }


    @Override
    protected void belowUiStageDraw(float delta) {
        logicOnDraw();

        deskStage.act();
        deskStage.getViewport().getCamera().position.set(
                deskAreaVM.getCameraDataPackage().getCurrentCameraX(),
                deskAreaVM.getCameraDataPackage().getCurrentCameraY(),
                0);
        if (deskAreaVM.getCameraDataPackage().getAndClearCameraZoomDirty()) {
            float weight = deskAreaVM.getCameraDataPackage().getCurrentCameraZoomWeight();
            float value = weight <= 0 ? (float)Math.pow(2, weight) : (float)Math.log(weight + 2);
            deskCamera.zoom = value;
            game.getFrontend().log(this.getClass().getSimpleName(), "deskCamera.zoom = %s", deskCamera.zoom);
        }
        deskStage.getViewport().apply();
        deskStage.draw();
    }


    @Override
    protected void aboveUiStageDraw(float delta) {
        imagePreviewerStage.act();
        imagePreviewerStage.getViewport().getCamera().position.set(
                imageViewerVM.getCameraDataPackage().getCurrentCameraX(),
                imageViewerVM.getCameraDataPackage().getCurrentCameraY(),
                0);
        if (imageViewerVM.getCameraDataPackage().getAndClearCameraZoomDirty()) {
            float weight = imageViewerVM.getCameraDataPackage().getCurrentCameraZoomWeight();
            float value = weight <= 0 ? (float)Math.pow(2, weight) : (float)Math.log(weight + 2);
            imagePreviewerCamera.zoom = value;
            game.getFrontend().log(this.getClass().getSimpleName(), "imagePreviewerCamera.zoom = %s", deskCamera.zoom);
        }
        imagePreviewerStage.getViewport().apply();
        imagePreviewerStage.draw();
    }
}

package hundun.tool.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.screen.shared.DeskVM;
import hundun.tool.libgdx.screen.market.mainboard.MarketMainBoardVM;
import hundun.tool.libgdx.screen.market.mainboard.MarketMainBoardVM.MainBoardState;
import hundun.tool.libgdx.screen.shared.DeskAreaVM;
import hundun.tool.libgdx.screen.market.PopupCloseButton;
import hundun.tool.libgdx.screen.market.ImageViewerVM;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.LogicContext.IModifyGoodTagListener;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.RoomRuntimeData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class MarketScreen extends AbstractComikeScreen implements IModifyGoodTagListener {


    // ------ UI layer ------
    @Getter
    private MarketMainBoardVM mainBoardVM;

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
        deskStage.setScrollFocus(deskAreaVM);
        
        // ------ UI layer ------

        mainBoardVM = new MarketMainBoardVM(this);
        uiRootTable.add(mainBoardVM)
                .expandX()
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
    public void hide() {
        super.hide();
        
        game.getLogicContext().getModifyGoodTagListeners().remove(this);
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

        game.getLogicContext().getModifyGoodTagListeners().add(this);
        updateUIForShow();


        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }

    private void updateUIForShow() {
        popupCloseButton.hide();
        imageViewerVM.hide();

        deskAreaVM.getCameraDataPackage().forceSet(null, null, CameraDataPackage.DEFAULT_CAMERA_ZOOM_WEIGHT);
        mainBoardVM.setState(MainBoardState.CART);
        mainBoardVM.updateByState(true);

        updateUIAfterRoomChanged();
    }



    @Override
    protected void updateUIAfterRoomChanged() {
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        // for newest DeskDatas
        RoomRuntimeData currentRoomData = crossScreenDataPackage.getCurrentRoomData();
        deskAreaVM.updateDeskDatas(
                currentRoomData.getDeskAreaInfo(),
                currentRoomData.getDeskDatas(),
                currentRoomData.getRoomImage());
        // for newest cart
        onAnyTagChanged(null);
    }

    private void onAnyTagChanged(@Null GoodRuntimeData changed) {
        mainBoardVM.updateByState(false);
        deskAreaVM.updateCartData(changed);
    }


    public static final TextureRegion RED_POINT = new TextureRegion(TextureFactory.createAlphaBoard(3, 3, Color.RED, 1.0f));

    @Override
    protected void logicOnDraw() {
        
    }

    @Override
    public void onRoomSwitchClicked(RoomRuntimeData newRoom) {
        this.getGame().getLogicContext().getCrossScreenDataPackage().setCurrentRoomData(newRoom);
        updateUIAfterRoomChanged();
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
            imagePreviewerCamera.zoom = CameraDataPackage.cameraZoomWeightToZoomValue(weight);
            game.getFrontend().log(this.getClass().getSimpleName(), "imagePreviewerCamera.zoom = %s", deskCamera.zoom);
        }
        imagePreviewerStage.getViewport().apply();
        imagePreviewerStage.draw();
    }

    @Override
    public void onDeskClicked(DeskVM vm) {
        mainBoardVM.setState(MainBoardState.DESK);
        mainBoardVM.setDetailingDeskData(vm.getDeskData());
        mainBoardVM.updateByState(true);
    }



    @Override
    public void onModifyGoodTag(GoodRuntimeData thiz, GoodRuntimeTag tag, boolean setToOn) {
        onAnyTagChanged(thiz);
    }
}

package hundun.tool.libgdx.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.screen.shared.DeskAreaVM;
import hundun.tool.libgdx.screen.shared.DeskVM;
import hundun.tool.logic.data.RoomRuntimeData;
import hundun.tool.logic.data.save.RootSaveData;

public abstract class AbstractComikeScreen extends BaseHundunScreen<ComikeHelperGame, RootSaveData> {

    // ------ desk layer ------
    protected OrthographicCamera deskCamera;
    protected Stage deskStage;
    protected DeskAreaVM deskAreaVM;

    public AbstractComikeScreen(ComikeHelperGame game, Viewport sharedViewport) {
        super(game, sharedViewport);
    }
    public abstract void onDeskClicked(DeskVM vm);
    protected abstract void logicOnDraw();
    protected abstract void updateUIAfterRoomChanged();
    public void onRoomSwitchClicked(RoomRuntimeData newRoom) {
        this.getGame().getLogicContext().getCrossScreenDataPackage().setCurrentRoomData(newRoom);
        updateUIAfterRoomChanged();
    };

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
            deskCamera.zoom = CameraDataPackage.cameraZoomWeightToZoomValue(weight);;
            game.getFrontend().log(this.getClass().getSimpleName(), "deskCamera.zoom = %s", deskCamera.zoom);
        }
        deskStage.getViewport().apply();
        deskStage.draw();
    }


}

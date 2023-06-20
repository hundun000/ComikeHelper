package hundun.tool.libgdx.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.builder.BuilderMainBoardVM;
import hundun.tool.libgdx.screen.shared.DeskAreaVM;
import hundun.tool.libgdx.screen.shared.DeskVM;
import hundun.tool.logic.ExternalResourceManager.MergeWorkInProgressModel;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.RoomRuntimeData;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class BuilderScreen extends AbstractComikeScreen {




    // ------ UI layer ------
    private BuilderMainBoardVM mainBoardVM;
    
    // ------ image previewer layer ------


    // ------ popup layer ------


    public BuilderScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());

        this.deskCamera = new OrthographicCamera();
        this.deskStage = new Stage(new ScreenViewport(deskCamera), game.getBatch());

    }

    public void startDialog(MergeWorkInProgressModel model, String title) {
        final Dialog dialog = new Dialog(title, game.getMainSkin(), "dialog") {
            public void result(Object obj) {
                boolean action = (boolean) obj;
                if (action) {
                    model.apply();
                    game.getLogicContext().updateCrossScreenDataPackage();
                    updateUIAfterRoomChanged();
                }
            }
        };
        dialog.text(model.toDiaglogMessage());
        dialog.button("Yes", true);
        dialog.button("No", false);
        Gdx.app.postRunnable(new Runnable() {
           @Override
           public void run() {
               dialog.show(popupUiStage);
           }
        });
    }

    @Override
    protected void create() {


        // ------ desk layer ------
        deskAreaVM = new DeskAreaVM(this);
        deskStage.addActor(deskAreaVM);
        deskStage.setScrollFocus(deskAreaVM);
        
        // ------ UI layer ------
        mainBoardVM = new BuilderMainBoardVM(this);
        uiRootTable.add(mainBoardVM)
                .expandX()
                .growY()
                .right()
                ;

        // ------ image previewer layer ------


        // ------ popup layer ------

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


        deskAreaVM.getCameraDataPackage().forceSet(null, null, 0);

        updateUIAfterRoomChanged();
    }

    @Override
    protected void updateUIAfterRoomChanged() {
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        // for newest DeskDatas
        RoomRuntimeData currentRoomData = crossScreenDataPackage.getCurrentRoomData();
        if (currentRoomData != null) {
            deskAreaVM.updateDeskDatas(
                    currentRoomData.getRoomWidth(),
                    currentRoomData.getRoomHeight(),
                    currentRoomData.getDeskDatas()
            );
        } else {
            deskAreaVM.updateDeskDatas(
                    100,
                    100,
                    new ArrayList<>(0)
            );
        }

        mainBoardVM.updateForShow();
    }

    @Override
    protected void logicOnDraw() {

    }

    @Override
    public void onDeskClicked(DeskVM vm) {

    }
}

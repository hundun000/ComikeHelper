package hundun.tool.libgdx.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.shared.DeskAreaVM;
import hundun.tool.libgdx.screen.shared.DeskVM;
import hundun.tool.libgdx.screen.shared.RoomSwitchBoardVM;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.RoomRuntimeData;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class BuilderScreen extends AbstractComikeScreen {




    // ------ UI layer ------
    private RoomSwitchBoardVM roomSwitchBoardVM;
    
    
    // ------ image previewer layer ------


    // ------ popup layer ------


    public BuilderScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());

        this.deskCamera = new OrthographicCamera();
        this.deskStage = new Stage(new ScreenViewport(deskCamera), game.getBatch());

    }



    @Override
    protected void create() {


        // ------ desk layer ------
        deskAreaVM = new DeskAreaVM(this);
        deskStage.addActor(deskAreaVM);
        deskStage.setScrollFocus(deskAreaVM);
        
        // ------ UI layer ------
        roomSwitchBoardVM = new RoomSwitchBoardVM(this);
        uiRootTable.add(roomSwitchBoardVM)
                .expand()
                //.grow()
                .left()
                .top()
                ;
        
        Table allButtonTable = new Table();
        uiRootTable.add(allButtonTable);
        
        TextButton loadExcelButton = new TextButton("append excel", game.getMainSkin());
        loadExcelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getLogicContext().appendExcelData();
            }
        });
        allButtonTable.add(loadExcelButton).row();
        
        TextButton handleFinalDataButton = new TextButton("handle current", game.getMainSkin());
        handleFinalDataButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getLogicContext().updateCrossScreenDataPackage();
                updateUIAfterRoomChanged();
            }
        });
        allButtonTable.add(handleFinalDataButton).row();
        
        TextButton saveButton = new TextButton("save", game.getMainSkin());
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getLogicContext().saveCurrentSharedData();
            }
        });
        allButtonTable.add(saveButton);
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
        roomSwitchBoardVM.intoSmallMode();

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
        
        roomSwitchBoardVM.intoFullMode();
    }

    @Override
    protected void logicOnDraw() {

    }

    @Override
    public void onDeskClicked(DeskVM vm) {

    }
}

package hundun.tool.libgdx.screen;

import java.util.ArrayList;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.builder.BuilderMainBoardVM;
import hundun.tool.libgdx.screen.shared.DeskAreaVM;
import hundun.tool.libgdx.screen.shared.DeskVM;
import hundun.tool.libgdx.screen.shared.ScrollDialog;
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

        ScrollDialog scrollDialog = new ScrollDialog(game);
        scrollDialog.getContent().setText(title + "\n\n" + model.toDiaglogMessage());
        scrollDialog.addButton("Yes", new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                model.apply();
                game.getLogicContext().updateCrossScreenDataPackage();
                updateUIAfterRoomChanged();
                popupRootTable.clear();
            }
        });
        scrollDialog.addButton("No", new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                popupRootTable.clear();
            }
        });
        popupRootTable.add(scrollDialog).grow();
    }

    
    public void startDialog(String message, String title, @Null Runnable callback) {

        ScrollDialog scrollDialog = new ScrollDialog(game);
        scrollDialog.getContent().setText(title + "\n\n" + message);
        scrollDialog.addButton("OK", new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callback.run();
                popupRootTable.clear();
            }
        });

        popupRootTable.add(scrollDialog).grow();
    }
    

    public void startInputBox(String title, Function<String, String> callback) {

        Table inputTable = new Table();
        inputTable.setBackground(DrawableFactory.createAlphaBoard(1, 1, Color.WHITE, 1f));

        TextField textField = new TextField("test", game.getMainSkin());
        Button okButton = new TextButton("ok", game.getMainSkin());
        Label hintLabel = new Label("hint", game.getMainSkin());
        okButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String failMessage = callback.apply(textField.getText());
                if (failMessage == null) {
                    popupRootTable.clear();
                } else {
                    hintLabel.setText("callback返回处理错误：" + failMessage);
                }
            }
        });


        inputTable.add(textField).pad(50).grow();
        inputTable.row();
        inputTable.add(okButton);
        inputTable.add(hintLabel).padLeft(20);
        popupRootTable.add(inputTable).grow();
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
    public void updateUIAfterRoomChanged() {
        CrossScreenDataPackage crossScreenDataPackage = game.getLogicContext().getCrossScreenDataPackage();
        // for newest DeskDatas
        RoomRuntimeData currentRoomData = crossScreenDataPackage.getCurrentRoomData();
        if (currentRoomData != null) {
            deskAreaVM.updateDeskDatas(
                    currentRoomData.getRoomWidth(),
                    currentRoomData.getRoomHeight(),
                    currentRoomData.getDeskDatas(),
                    currentRoomData.getRoomImage()
            );
        } else {
            deskAreaVM.updateDeskDatas(
                    100,
                    100,
                    new ArrayList<>(0),
                    null);
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

package hundun.tool.libgdx.screen.mainscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
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
import hundun.tool.logic.data.DeskData;
import hundun.tool.logic.data.RootSaveData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class MainScreen extends BaseHundunScreen<ComikeHelperGame, RootSaveData> {

    @Getter
    protected final PlayScreenLayoutConst layoutConst;
    
    @Getter
    @Setter
    private float currentCameraX;
    @Getter
    @Setter
    private float currentCameraY;

    private final Stage deskStage;
    
    
    private DeskAreaVM deskAreaVM;
    private CameraControlBoardVM cameraControlBoardVM;
    
    public MainScreen(ComikeHelperGame game, PlayScreenLayoutConst layoutConst) {
        super(game, game.getSharedViewport());
        this.layoutConst = layoutConst;
        
        this.deskStage = new Stage(new ScreenViewport(), game.getBatch());
    }

    @Override
    protected void create() {
        
        
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

        backUiStage.clear();
        popupRootTable.clear();
        lazyInitBackUiAndPopupUiContent();

        uiRootTable.clear();
        lazyInitUiRootContext();
        
        deskStage.clear();
        lazyInitDeskContext();
        
        lazyInitLogicContext();

        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }

    private void lazyInitBackUiAndPopupUiContent() {
        // TODO Auto-generated method stub
        cameraControlBoardVM = new CameraControlBoardVM(this);
        popupRootTable.add(cameraControlBoardVM).left();
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

    private void lazyInitLogicContext() {

        
        
        DeskVM centerEntity = deskAreaVM.nodes.values().iterator().next();
        currentCameraX = centerEntity.getX();
        currentCameraY = centerEntity.getY();
    }

    private void lazyInitDeskContext() {
        List<DeskData> deskDatas = IntStream.range(0, 100)
                .mapToObj(it -> DeskData.builder()
                            .name("A" + it)
                            .roomIndex(it)
                            .build()
                            )
                .collect(Collectors.toList());
        
        deskAreaVM = new DeskAreaVM(this, deskDatas);
        deskStage.addActor(deskAreaVM);
    }
    
    private void lazyInitUiRootContext() {
        
    }

    public static final TextureRegion RED_POINT = new TextureRegion(TextureFactory.createAlphaBoard(3, 3, Color.RED, 1.0f));
    
    @Override
    protected void gameObjectDraw(float delta) {
        deskStage.act();
        deskStage.getViewport().getCamera().position.set(currentCameraX, currentCameraY, 0);
        deskStage.getViewport().apply();
        deskStage.draw();
    }

}

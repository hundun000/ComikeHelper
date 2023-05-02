package hundun.tool.libgdx.screen.mainscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    
    private Integer defaultCenterIndex;
    private float currentCameraX;
    private float currentCameraY;
    List<DeskVM> deskVMMap = new ArrayList<>();


    public MainScreen(ComikeHelperGame game, PlayScreenLayoutConst layoutConst) {
        super(game, game.getSharedViewport());
        this.layoutConst = layoutConst;
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
        
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

        backUiStage.clear();
        popupRootTable.clear();
        lazyInitBackUiAndPopupUiContent();

        uiRootTable.clear();
        lazyInitUiRootContext();

        lazyInitLogicContext();

        Gdx.app.log(this.getClass().getSimpleName(), "show done");
    }

    private void lazyInitBackUiAndPopupUiContent() {
        // TODO Auto-generated method stub
        
    }
    
    public static class TiledMapClickListener extends ClickListener {

        private DeskVM vm;

        public TiledMapClickListener(DeskVM vm) {
            this.vm = vm;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println(vm.getIndex() + " has been clicked.");
        }
    }

    private void lazyInitLogicContext() {
        deskVMMap.clear();
        
        IntStream.range(0, 100)
                .forEach(it -> {
                    DeskData deskData = DeskData.builder()
                            .name("A" + it)
                            .build();
                    DeskVM actor = new DeskVM(this, deskData, it);
                    deskVMMap.add(actor);
                    
                    actor.setBounds(actor.getPos().x, actor.getPos().y, DeskVM.WIDTH, DeskVM.HEIGHT);
                    EventListener eventListener = new TiledMapClickListener(actor);
                    actor.addListener(eventListener);
                    uiStage.addActor(actor);
                });
        
        defaultCenterIndex = 0;
        DeskVM centerEntity = deskVMMap.get(defaultCenterIndex);
        currentCameraX = centerEntity.getPos().x;
        currentCameraY = centerEntity.getPos().y;
    }

    private void lazyInitUiRootContext() {

        
    }

    public static final TextureRegion RED_POINT = new TextureRegion(TextureFactory.createAlphaBoard(3, 3, Color.RED, 1.0f));
    
    

}

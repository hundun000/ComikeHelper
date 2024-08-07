package hundun.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.stripe.FreeTypeSkin;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.gdxgame.corelib.base.BaseHundunGame;
import hundun.gdxgame.gamelib.base.save.ISaveTool;
import hundun.tool.libgdx.screen.MyMenuScreen;
import hundun.tool.libgdx.screen.ScreenContext;
import hundun.tool.logic.LogicContext;
import hundun.tool.logic.TextureManager;
import hundun.tool.logic.data.save.RootSaveData;
import lombok.Getter;


public class ComikeHelperGame extends BaseHundunGame<RootSaveData> {

    public static final String GAME_WORD_SKIN_KEY = "default";

    @Getter
    private ScreenContext screenContext;
    @Getter
    private LogicContext logicContext;

    @Getter
    private Viewport sharedViewport;
    @Getter
    private TextureManager textureManager;
    
    public ComikeHelperGame(ISaveTool<RootSaveData> saveTool) {
        super(640, 480);
        //this.skinFilePath = "skins/orange/skin/uiskin.json";
        debugMode = true;

        this.sharedViewport = new ScreenViewport();
        // this project use external files, not saveHandler 
        this.saveHandler = null;
        this.mainSkinFilePath = null;
        this.screenContext = new ScreenContext(this);
        this.logicContext = new LogicContext(this);
        this.textureManager = new TextureManager(this);
    }


    @Override
    protected void createStage1() {
        super.createStage1();
        this.mainSkin = new FreeTypeSkin(Gdx.files.internal("skins/freetype/skin.json"));
        this.logicContext.lazyInitOnCreateStage1();
        this.textureManager.lazyInitOnCreateStage1();
    }

    /**
     * 此时各Manager不可调用Context。
     */
    @Override
    protected void createStage2() {
        screenContext.lazyInit(this);

    }

    /**
     * 此时各Manager可调用Context。
     */
    @Override
    protected void createStage3() {
        screenManager.pushScreen(MyMenuScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
    }




}

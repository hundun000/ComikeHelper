package hundun.tool.libgdx.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.gdxgame.corelib.starter.StarterMenuScreen;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.RootSaveData;

/**
 * @author hundun
 * Created on 2023/01/13
 */
public class MyMenuScreen extends StarterMenuScreen<ComikeHelperGame, RootSaveData> {

    public MyMenuScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());
        
        StarterMenuScreen.Factory.simpleFill(this, game, "ComikeHelper", 
                new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground())), 
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.getSaveHandler().gameplayLoadOrStarter(true);
                        game.getScreenManager().pushScreen(MarketScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                }, 
                new InputListener(){
                    @Override
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                        game.getSaveHandler().gameplayLoadOrStarter(false);
                        game.getScreenManager().pushScreen(MarketScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
                    }
                    @Override
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
    }

}

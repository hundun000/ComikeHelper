package hundun.tool.libgdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.gdxgame.corelib.starter.StarterMenuScreen;
import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.RootSaveData;

/**
 * @author hundun
 * Created on 2023/01/13
 */
public class MyMenuScreen extends BaseHundunScreen<ComikeHelperGame, RootSaveData> {

    int BUTTON_WIDTH = 100;
    int BUTTON_BIG_HEIGHT = 100;
    int BUTTON_SMALL_HEIGHT = 75;

    Actor title;

    Actor buttonNewGame;
    Actor buttonIntoPrepareScreen;
    Image backImage;

    public MyMenuScreen(ComikeHelperGame game) {
        super(game, game.getSharedViewport());

        Label titleLabel = new Label(
            JavaFeatureForGwt.stringFormat("     %s     ", "ComikeHelper"),
            game.getMainSkin());
        titleLabel.setFontScale(1.5f);

        Image backImage = new Image(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground())));

        this.buttonNewGame = new TextButton("New game", game.getMainSkin());
        buttonNewGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getSaveHandler().gameplayLoadOrStarter(false);
                game.getScreenManager().pushScreen(MarketScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
            }
        });


        this.buttonIntoPrepareScreen = new TextButton("prepare", game.getMainSkin());
        buttonIntoPrepareScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.getLogicContext().setSkipApplyExternalGameplayData(true);
                game.getSaveHandler().gameplayLoadOrStarter(false);
                game.getScreenManager().pushScreen(BuilderScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
            }
        });

        this.title = titleLabel;
        this.backImage = backImage;

    }


    private void initScene2d() {
        backImage.setFillParent(true);
        backUiStage.addActor(backImage);




        uiRootTable.add(title)
            .row();
        uiRootTable.add(new Label("ExtRoot: " + game.getLogicContext().getExtRoot(), game.getMainSkin()))
            .row();


        uiRootTable.add(buttonNewGame)
            .height(game.getSaveHandler().hasContinuedGameplaySave() ? BUTTON_SMALL_HEIGHT : BUTTON_BIG_HEIGHT)
            .fillY()
            .padTop(10)
            .row();
        uiRootTable.add(buttonIntoPrepareScreen)
                .height(game.getSaveHandler().hasContinuedGameplaySave() ? BUTTON_SMALL_HEIGHT : BUTTON_BIG_HEIGHT)
                .fillY()
                .padTop(10)
                .row();

    }

    @Override
    public void show() {
        super.show();
        //addInputProcessor(uiStage);
        Gdx.input.setInputProcessor(uiStage);
        game.getBatch().setProjectionMatrix(uiStage.getViewport().getCamera().combined);

        initScene2d();
    }

    @Override
    public void dispose() {

    }


    @Override
    protected void create() {

    }
}

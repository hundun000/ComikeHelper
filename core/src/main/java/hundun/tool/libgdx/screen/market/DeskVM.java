package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.DeskRuntimeData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskVM extends Table {

    ComikeHelperGame game;
    
    DeskAreaVM deskAreaVM;
    @Getter
    DeskRuntimeData deskData;

    Image starImage;
    
    public DeskVM(DeskAreaVM deskAreaVM, DeskRuntimeData deskData) {
        this.game = deskAreaVM.parent.getGame();
        this.deskAreaVM = deskAreaVM;
        this.deskData = deskData;

        
        starImage = new Image();
        
        this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(game.getScreenContext().getLayoutConst().DESK_WIDTH, game.getScreenContext().getLayoutConst().DESK_HEIGHT))));
        this.add(new Label(
                deskData.getName() + " " + deskData.getLocation().getArea() + deskData.getLocation().getAreaIndex(), 
                game.getMainSkin()));
        this.add(starImage)
            .width(game.getScreenContext().getLayoutConst().DESK_STAR_SIZE)
            .height(game.getScreenContext().getLayoutConst().DESK_STAR_SIZE)
            ;
       

    }
    
    public void update(boolean starState) {
        if (starState) {
            starImage.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("star.png"))));
        } else {
            starImage.setDrawable(null);
        }
    }

}

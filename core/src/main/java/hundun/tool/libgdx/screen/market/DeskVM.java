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
import hundun.tool.logic.data.DeskRuntimeData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskVM extends Table {

    
    
    DeskAreaVM deskAreaVM;
    @Getter
    DeskRuntimeData deskData;

    
    public DeskVM(DeskAreaVM deskAreaVM, DeskRuntimeData deskData) {
        this.deskAreaVM = deskAreaVM;
        this.deskData = deskData;

        
        this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(DeskRuntimeData.WIDTH, DeskRuntimeData.HEIGHT))));
        this.add(new Label(
                deskData.getName() + " " + deskData.getLocation().getArea() + deskData.getLocation().getAreaIndex(), 
                deskAreaVM.parent.getGame().getMainSkin()));
        
       

    }

}

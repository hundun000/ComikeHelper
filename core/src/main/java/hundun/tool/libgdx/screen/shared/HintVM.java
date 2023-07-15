package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashSet;
import java.util.Set;

import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.generic.GenericPosData;
import hundun.tool.logic.data.save.HintSaveData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class HintVM extends Table {

    ComikeHelperGame game;

    DeskAreaVM deskAreaVM;


    private HintVM(DeskAreaVM deskAreaVM) {
        this.game = deskAreaVM.screen.getGame();
        this.deskAreaVM = deskAreaVM;


        this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(
                this.game.getScreenContext().getLayoutConst().DESK_WIDTH,
                this.game.getScreenContext().getLayoutConst().DESK_HEIGHT
        ))));
    }


    public static HintVM typeHint(DeskAreaVM deskAreaVM, HintSaveData hintSaveData) {
        HintVM thiz = new HintVM(deskAreaVM);

        thiz.add(new Label(
                hintSaveData.getText(),
                thiz.game.getMainSkin()));
        ;
       
        return thiz;
    }

}

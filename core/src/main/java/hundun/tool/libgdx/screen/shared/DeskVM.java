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

    Table tagImageTable;;



    public DeskVM(DeskAreaVM deskAreaVM, DeskRuntimeData deskData) {
        this.game = deskAreaVM.screen.getGame();
        this.deskAreaVM = deskAreaVM;
        this.deskData = deskData;


        tagImageTable = new Table();
        
        this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(game.getScreenContext().getLayoutConst().DESK_WIDTH, game.getScreenContext().getLayoutConst().DESK_HEIGHT))));
        this.add(new Label(
                deskData.getShowName() + " " + deskData.getLocation().getArea() + deskData.getLocation().getAreaIndex(),
                game.getMainSkin()));
        this.add(tagImageTable)
                ;
       

    }
    
    public void updateTagTable() {


        tagImageTable.clear();
        Set<GoodRuntimeTag> allGoodTags = new HashSet<>();
        deskData.getGoodSaveDatas().forEach(it -> {
            it.getTagStateMap().forEach((tag, state) -> {
                if (state) {
                    allGoodTags.add(tag);
                }
            });
        });
        allGoodTags.forEach(it -> {
            Texture texture = game.getTextureManager().getTagImageMap().get(it);
            if (texture != null) {
                tagImageTable
                        .add(new Image(texture))
                        .width(game.getScreenContext().getLayoutConst().DESK_STAR_SIZE)
                        .height(game.getScreenContext().getLayoutConst().DESK_STAR_SIZE)
                        ;
            }
        });
    }

}

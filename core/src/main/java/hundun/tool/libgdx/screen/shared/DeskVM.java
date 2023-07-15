package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashSet;
import java.util.Set;

import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.generic.GenericPosData;
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

    Table tagImageTable;

    private DeskVM(DeskAreaVM deskAreaVM, DeskRuntimeData deskData) {
        this.game = deskAreaVM.screen.getGame();
        this.deskAreaVM = deskAreaVM;
        this.deskData = deskData;


        Image image = new Image(game.getTextureManager().getDeskBackground());
        image.setBounds(0, 0, this.game.getScreenContext().getLayoutConst().DESK_WIDTH, this.game.getScreenContext().getLayoutConst().DESK_HEIGHT);
        this.addActor(image);
        /*this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(
                this.game.getScreenContext().getLayoutConst().DESK_WIDTH,
                this.game.getScreenContext().getLayoutConst().DESK_HEIGHT
        ))));*/
    }


    public static DeskVM typeMain(DeskAreaVM deskAreaVM, DeskRuntimeData deskData, GenericPosData location) {
        DeskVM thiz = new DeskVM(deskAreaVM, deskData);

        thiz.tagImageTable = new Table();
        thiz.add(new Label(
                deskData.getUiName() + " " + location.getArea() + location.getAreaIndex(),
                thiz.game.getMainSkin()));
        thiz.add(thiz.tagImageTable)
                ;
       
        return thiz;
    }

    public static DeskVM typeCompanion(DeskAreaVM deskAreaVM, DeskRuntimeData deskData, GenericPosData location) {
        DeskVM thiz = new DeskVM(deskAreaVM, deskData);

        thiz.add(new Label(
                "SUB " + location.getArea() + location.getAreaIndex(),
                thiz.game.getMainSkin()));
        ;

        return thiz;
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

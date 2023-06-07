package hundun.tool.libgdx.screen.market;

import java.util.stream.Stream;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;

public class GoodExtraVM extends Table {
    MarketScreen screen;
    Table childrenTable;

    public GoodExtraVM(MarketScreen screen) {
        this.screen = screen;

        this.childrenTable = new Table();
/*        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);

        this.add(scrollPane)
                .maxHeight(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE * 2.5f)
                ;*/

        this.add(childrenTable);
        //this.debugTable();
    }


    public void updateData(GoodRuntimeData detailingGood) {
        screen.getGame().getFrontend().log(this.getClass().getSimpleName(), 
                "updateData called");
        childrenTable.clear();
        
        GoodTagEditorNodeVM node = new GoodTagEditorNodeVM(screen, detailingGood, GoodRuntimeTag.IN_CART, GoodRuntimeTag.DONE);
        childrenTable.add(node).row();
        
        Stream.of(GoodRuntimeTag.values())
                .filter(it -> it != GoodRuntimeTag.IN_CART && it != GoodRuntimeTag.DONE)
                .forEach(it -> {
                    GoodTagEditorNodeVM itnode = new GoodTagEditorNodeVM(screen, detailingGood, it);
                    childrenTable.add(itnode).row();
                });

    }
}

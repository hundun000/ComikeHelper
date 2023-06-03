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
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);

        this.add(scrollPane)
                .maxHeight(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE * 2.5f)
                ;
        this.debugTable();
    }


    public void updateData(GoodRuntimeData detailingGood) {

        childrenTable.clear();
        Stream.of(GoodRuntimeTag.values()).forEach(it -> {
            GoodTagEditorVM node = new GoodTagEditorVM(screen, detailingGood, it);
            childrenTable.add(node).row();
        });

    }
}

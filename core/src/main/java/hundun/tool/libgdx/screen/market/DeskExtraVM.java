package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.DeskRuntimeData;

public class DeskExtraVM extends Table {
    MarketScreen screen;
    Table childrenTable;

    public DeskExtraVM(MarketScreen screen) {
        this.screen = screen;

        this.childrenTable = new Table();
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);

        this.add(scrollPane)
                .maxHeight(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE * 2.5f)
                ;
        this.debugTable();
    }


    public void updateData(DeskRuntimeData detailingDeskData) {

        childrenTable.clear();
        detailingDeskData.getDetailFileHandles().forEach(it -> {
            Texture texture = new Texture(it);
            Image image = new Image(texture);
            image.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    screen.getImageViewerVM().updateImageAndShow(texture);
                    screen.getPopupCloseButton().updateCallbackAndShow(() -> {
                        screen.getImageViewerVM().hide();
                    });
                }
            });
            childrenTable.add(image)
                    .width(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE)
                    .height(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE)
                    .padBottom(10)
                    .row();
        });

    }
}

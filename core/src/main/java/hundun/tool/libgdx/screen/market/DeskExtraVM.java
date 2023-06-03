package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.MainBoardVM.MainBoardState;
import hundun.tool.logic.data.DeskRuntimeData;

public class DeskExtraVM extends Table {
    
    public static int WINDOW_PAD_TOP = 0;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    Table childrenTable;
    Table extraTextTable;
    TextButton backButton;
    
    public DeskExtraVM(MarketScreen screen) {
        super();
        this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.screen = screen;

        this.backButton = new TextButton("返回", screen.getGame().getMainSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getMainBoardVM().back();
            }
        });
        this.add(backButton).growX().row();
        
        
        this.childrenTable = new Table();
        //childrenTable.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //childrenTable.getTitleTable().setHeight(200);
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(false, true);
        this.add(scrollPane)
                .height(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE * 1.0f)
                .growX()
                .row()
                ;
        
        this.extraTextTable = new Table();
        extraTextTable.add(new Label("test", screen.getGame().getMainSkin()));
        //extraTextTable.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        this.add(extraTextTable)
                //.grow()
                ;
        
    }


    public void updateData(String title, DeskRuntimeData detailingDeskData) {
        
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
                    .pad(10)
                    //.row()
                    ;
        });

    }
    

}

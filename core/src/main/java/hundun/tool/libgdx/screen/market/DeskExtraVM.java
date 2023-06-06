package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.MainBoardVM.MainBoardState;
import hundun.tool.libgdx.screen.shared.ImageBoxVM;
import hundun.tool.libgdx.screen.shared.MyWindow;
import hundun.tool.logic.data.DeskRuntimeData;

public class DeskExtraVM extends MyWindow {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    Table childrenTable;
    MyWindow extraTextTable;
    TextButton backButton;
    
    public DeskExtraVM(MarketScreen screen) {
        super("title", screen.getGame().getMainSkin());
        this.setTouchable(Touchable.childrenOnly);
        this.setFillParent(true);
        this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
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
        this.add(backButton)
                .pad(WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER)
                .growX()
                .row()
                ;

        MyWindow childrenTableContainer = new MyWindow("images", screen.getGame().getMainSkin());
        this.childrenTable = new Table();
        //childrenTable.getTitleTable().setHeight(200);
        ScrollPane scrollPane = new ScrollPane(childrenTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);
        childrenTableContainer.add(scrollPane);
        this.add(childrenTableContainer)
                .pad(WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER)
                .height(screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE * 1.1f
                        + childrenTableContainer.getTitleHeight()
                        + WINDOW_PAD_OTHER * 3)
                .growX()
                .row()
                ;
        
        this.extraTextTable = new MyWindow("extra", screen.getGame().getMainSkin());
        extraTextTable.add(new Label("test", screen.getGame().getMainSkin()));
        this.add(extraTextTable)
                .pad(WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER)
                .minHeight(200)
                .grow()
                ;
        
    }


    public void updateData(String title, DeskRuntimeData detailingDeskData) {

        this.getTitleLabel().setText(title);

        childrenTable.clear();
        detailingDeskData.getDetailFileHandles().forEach(it -> {
            Texture texture = new Texture(it);
            
            ImageBoxVM image = new ImageBoxVM(
                    screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE, 
                    screen.getGame().getScreenContext().getLayoutConst().CART_BOARD_EXTRA_IMAGE_SIZE);
            image.getImage().setDrawable(new TextureRegionDrawable(texture));
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
                    .pad(10)
                    //.row()
                    ;
        });

    }
    

}

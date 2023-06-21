package hundun.tool.libgdx.screen.market.mainboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.market.CartGoodVM;
import hundun.tool.libgdx.screen.shared.BasePageableTable;
import hundun.tool.libgdx.screen.shared.GoodListPageVM;
import hundun.tool.libgdx.screen.shared.ImageBoxVM;
import hundun.tool.libgdx.screen.shared.MyWindow;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import lombok.Getter;

public class DeskExtraVM extends BasePageableTable {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen marketScreen;
    

    //Table page1RootTable;
    @Getter
    GoodListPageVM goodListPageVM;
    //Table page2RootTable;
    HorizontalGroup imagesTable;
    
    MyWindow extraTextTable;
    TextButton backButton;
    
    private enum DeskExtraState {
        PAGE1,
        PAGE2,
        ;
    }
    
    public DeskExtraVM(MarketScreen screen) {
        super(screen);
        this.backButton = new TextButton("返回", screen.getGame().getMainSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getMainBoardVM().back();
            }
        });
        init("", screen.getGame(), backButton);
        //this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.marketScreen = screen;


        // ----- candidates ------
        {
            goodListPageVM = new GoodListPageVM(screen);


            addPage(DeskExtraState.PAGE1.name(),
                    "作品列表",
                    goodListPageVM
            );
        }
        {
            Table pageRootTable = new Table();
            MyWindow container = new MyWindow("图片列表", screen.getGame());
            this.imagesTable = new HorizontalGroup();
            imagesTable.padRight(screen.getGame().getScreenContext().getLayoutConst().DESK_EXTRA_IMAGE_SIZE * 0.1f);
            ScrollPane imagesScrollPane = new ScrollPane(imagesTable, screen.getGame().getMainSkin());
            imagesScrollPane.setScrollingDisabled(false, true);
            imagesScrollPane.setFadeScrollBars(false);
            imagesScrollPane.setForceScroll(true, false);
            container.addToMain(imagesScrollPane);
            pageRootTable.add(container)
                    .height(screen.getGame().getScreenContext().getLayoutConst().DESK_EXTRA_IMAGE_SIZE * 1.1f
                            + container.getTitleHeight()
                            + WINDOW_PAD_OTHER * 3)
                    .growX()
                    .row()
            ;

            this.extraTextTable = new MyWindow("备注文本", screen.getGame());
            extraTextTable.addToMain(new Label("test", screen.getGame().getMainSkin()));
            pageRootTable.add(extraTextTable)
                    .padBottom(WINDOW_PAD_OTHER)
                    .grow()
            ;

            addPage(DeskExtraState.PAGE2.name(),
                    "社团详情",
                    pageRootTable
            );
        }
    }

    
    public void updateForShow(String title, DeskRuntimeData detailingDeskData) {

        this.getTitleLabel().setText(title);

        imagesTable.clear();
        detailingDeskData.getDetailFileHandles().forEach(it -> {
            Texture texture = new Texture(it);
            
            ImageBoxVM image = new ImageBoxVM(
                    screen.getGame().getScreenContext().getLayoutConst().DESK_EXTRA_IMAGE_SIZE, 
                    screen.getGame().getScreenContext().getLayoutConst().DESK_EXTRA_IMAGE_SIZE);
            image.getImage().setDrawable(new TextureRegionDrawable(texture));
            image.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    marketScreen.getImageViewerVM().updateImageAndShow(texture);
                    marketScreen.getPopupCloseButton().updateCallbackAndShow(() -> {
                        marketScreen.getImageViewerVM().hide();
                    });
                }
            });
            imagesTable.addActor(image);
        });

        updateByState(DeskExtraState.PAGE1.name());
    }

}

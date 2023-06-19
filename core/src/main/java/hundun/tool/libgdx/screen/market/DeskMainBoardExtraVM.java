package hundun.tool.libgdx.screen.market;

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
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.shared.ImageBoxVM;
import hundun.tool.libgdx.screen.shared.MyWindow;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;

public class DeskMainBoardExtraVM extends MyWindow {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    
    
    Container<Table> currentTableContainer;
    Map<DeskExtraState, Table> pageRootTableMap = new HashMap<>();
    //Table page1RootTable;
    Table goodsTable;
    //Table page2RootTable;
    HorizontalGroup imagesTable;
    
    MyWindow extraTextTable;
    TextButton backButton;
    
    public enum DeskExtraState {
        PAGE1,
        PAGE2,
        ;
    }
    
    public DeskMainBoardExtraVM(MarketScreen screen) {
        this.backButton = new TextButton("返回", screen.getGame().getMainSkin());
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getMainBoardVM().back();
            }
        });
        init("DeskExtra", screen.getGame(), backButton);
        //this.setFillParent(true);
        //this.pad(WINDOW_PAD_TOP, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER, WINDOW_PAD_OTHER);
        //this.getTitleTable().center();
        //this.getTitleTable().setHeight(200);
        this.screen = screen;

        
        this.currentTableContainer = new Container<>();
        currentTableContainer.fill();
        currentTableContainer.pad(25);
        
        TextButton button;
        HorizontalGroup horizontalGroup = new HorizontalGroup();

        

        button = new TextButton("page1", screen.getGame().getMainSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                updateByState(DeskExtraState.PAGE1);
            }
        });
        horizontalGroup.addActor(button);
        
        button = new TextButton("page2", screen.getGame().getMainSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                updateByState(DeskExtraState.PAGE2);
            }
        });
        horizontalGroup.addActor(button);

        this.addToMain(horizontalGroup);

        this.rowToMain();
        
        this.addToMain(currentTableContainer)
                .width(screen.getGame().getScreenContext().getLayoutConst().ANY_EXTRA_TOTAL_WIDTH)
                .growY()
                ;
        
        // ----- candidates ------
        {
            Table pageRootTable = new Table();
            pageRootTableMap.put(DeskExtraState.PAGE1, pageRootTable);
            MyWindow container = new MyWindow("goods", screen.getGame());
            this.goodsTable = new Table();
            ScrollPane scrollPane = new ScrollPane(goodsTable, screen.getGame().getMainSkin());
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false, true);
            container.addToMain(scrollPane);
            pageRootTable.add(container)
                    .grow();
        }

        {
            Table pageRootTable = new Table();
            pageRootTableMap.put(DeskExtraState.PAGE2, pageRootTable);
            MyWindow container = new MyWindow("images", screen.getGame());
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

            this.extraTextTable = new MyWindow("extra", screen.getGame());
            extraTextTable.addToMain(new Label("test", screen.getGame().getMainSkin()));
            pageRootTable.add(extraTextTable)
                    .padBottom(WINDOW_PAD_OTHER)
                    .grow()
            ;
        }
    }


    private void updateByState(DeskExtraState state) {
        currentTableContainer.setActor(pageRootTableMap.get(state));
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
                    screen.getImageViewerVM().updateImageAndShow(texture);
                    screen.getPopupCloseButton().updateCallbackAndShow(() -> {
                        screen.getImageViewerVM().hide();
                    });
                }
            });
            imagesTable.addActor(image);
        });

        updateByState(DeskExtraState.PAGE1);
    }
    
    public void updateGoods(List<GoodRuntimeData> needShowList) {
        goodsTable.clear();
        needShowList.forEach(it -> {
            CartGoodVM node = new CartGoodVM(screen, it);
            goodsTable.add(node)
                    .width(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_WIDTH)
                    .height(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_HEIGHT)
                    .padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD)
                    .row();
        });

        
    }
}

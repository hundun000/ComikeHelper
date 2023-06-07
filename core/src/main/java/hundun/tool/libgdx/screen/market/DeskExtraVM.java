package hundun.tool.libgdx.screen.market;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
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
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;

public class DeskExtraVM extends MyWindow {
    
    public static int WINDOW_PAD_TOP = 80;
    public static int WINDOW_PAD_OTHER = 10;
    
    MarketScreen screen;
    
    
    Container<Table> currentTable;
    Table page1Table;
    Table goodsTable;
    Table page2Table;
    Table imagesTable;
    
    MyWindow extraTextTable;
    TextButton backButton;
    
    public enum DeskExtraState {
        PAGE1,
        PAGE2,
        ;
    }
    
    public DeskExtraVM(MarketScreen screen) {
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

        
        this.currentTable = new Container<>();
        currentTable.fill();
        currentTable.pad(25);
        
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
        
        this.addToMain(currentTable)
                .width(screen.getGame().getScreenContext().getLayoutConst().ANY_EXTRA_TOTAL_WIDTH)
                .growY()
                ;
        
        // ----- candidates ------
        this.page1Table = new Table();
        //page1Table.setBackground(screen.getGame().getTextureManager().getMcStyleTable());
        MyWindow goodsTableContainer = new MyWindow("goods", screen.getGame());
        this.goodsTable = new Table();
        ScrollPane goodsScrollPane = new ScrollPane(goodsTable, screen.getGame().getMainSkin());
        goodsScrollPane.setScrollingDisabled(true, false);
        goodsScrollPane.setFadeScrollBars(false);
        goodsScrollPane.setForceScroll(true, false);
        goodsTableContainer.addToMain(goodsScrollPane);
        page1Table.add(goodsTableContainer)
                .grow();

        this.page2Table = new Table();
        //page2Table.setBackground(screen.getGame().getTextureManager().getMcStyleTable());
        MyWindow imagesTableContainer = new MyWindow("images", screen.getGame());
        this.imagesTable = new Table();
        //childrenTable.getTitleTable().setHeight(200);
        ScrollPane imagesScrollPane = new ScrollPane(imagesTable, screen.getGame().getMainSkin());
        imagesScrollPane.setScrollingDisabled(false, true);
        imagesScrollPane.setFadeScrollBars(false);
        goodsScrollPane.setForceScroll(false, true);
        imagesTableContainer.addToMain(imagesScrollPane);
        page2Table.add(imagesTableContainer)
                .height(screen.getGame().getScreenContext().getLayoutConst().DESK_EXTRA_IMAGE_SIZE * 1.1f
                        + imagesTableContainer.getTitleHeight()
                        + WINDOW_PAD_OTHER * 3)
                .growX()
                .row()
                ;
        
        this.extraTextTable = new MyWindow("extra", screen.getGame());
        extraTextTable.addToMain(new Label("test", screen.getGame().getMainSkin()));
        page2Table.add(extraTextTable)
                .padBottom(WINDOW_PAD_OTHER)
                .grow()
                ;
        
    }


    public void updateByState(DeskExtraState state) {
        switch (state) {
            case PAGE1:
                currentTable.setActor(page1Table);
                break;
            case PAGE2:
                currentTable.setActor(page2Table);
                break;
            default:
                break;
        }
        
    }
    
    public void updateData(String title, DeskRuntimeData detailingDeskData) {

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
            imagesTable.add(image)
                    .pad(10)
                    //.row()
                    ;
        });

        updateByState(DeskExtraState.PAGE1);
    }
    
    public void updateCore(List<GoodRuntimeData> needShowList) {
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

package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.MyMenuScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import lombok.Getter;

public class RoomSwitchPageVM extends Table {
    AbstractComikeScreen screen;
    VerticalGroup roomsTable;

    public RoomSwitchPageVM(AbstractComikeScreen screen) {
        this.screen = screen;

        this.roomsTable = new VerticalGroup();
        roomsTable.padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD);

        MyWindow container = new MyWindow("展馆列表", screen.getGame());
        ScrollPane scrollPane = new ScrollPane(roomsTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        container.addToMain(scrollPane);
        this.add(container)
                .grow();

        TextButton exitButton = new TextButton("返回主菜单", screen.getGame().getMainSkin());
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getGame().getScreenManager().pushScreen(MyMenuScreen.class.getSimpleName(), BlendingTransition.class.getSimpleName());
            }
        });
        this.row();
        this.add(exitButton);
    }

    public void updateData() {
        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        this.roomsTable.clear();
        crossScreenDataPackage.getRoomMap().values().forEach(it -> {
            RoomSwitchNode node = new RoomSwitchNode(screen);
            node.update(it, it == crossScreenDataPackage.getCurrentRoomData());
            this.roomsTable.addActor(node);
        });
    }

}

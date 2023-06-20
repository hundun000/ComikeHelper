package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import hundun.gdxgame.corelib.base.BaseHundunScreen;
import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import lombok.Getter;

public class RoomSwitchPageVM extends Table {
    AbstractComikeScreen screen;
    VerticalGroup roomsTable;

    public RoomSwitchPageVM(AbstractComikeScreen screen) {
        this.screen = screen;

        this.roomsTable = new VerticalGroup();
        roomsTable.padBottom(screen.getGame().getScreenContext().getLayoutConst().GOOD_NODE_PAD);

        MyWindow container = new MyWindow("rooms", screen.getGame());
        ScrollPane scrollPane = new ScrollPane(roomsTable, screen.getGame().getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        container.addToMain(scrollPane);
        this.add(container)
                .grow();
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

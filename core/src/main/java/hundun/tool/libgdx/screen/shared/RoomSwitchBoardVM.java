package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;

/**
 * @author hundun
 * Created on 2023/05/17
 */
public class RoomSwitchBoardVM extends Container<Table> {
    AbstractComikeScreen screen;

    SmallModeTable smallModeTable;
    FullModeTable fullModeTable;

    public void afterRoomChanged() {

    }

    private class SmallModeTable extends Table {
        public SmallModeTable() {

            TextButton modeButton = new TextButton("+", screen.getGame().getMainSkin());
            modeButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    intoFullMode();
                }
            });
            this.add(modeButton);
        }

    }

    private class FullModeTable extends Table {

        VerticalGroup nodeGroup;

        public FullModeTable() {
            TextButton modeButton = new TextButton("-", screen.getGame().getMainSkin());
            modeButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    intoSmallMode();
                }
            });
            this.add(modeButton)
                    .left()
                    ;
            this.row();

            this.nodeGroup = new VerticalGroup();
            this.add(nodeGroup);
        }

        public void update(CrossScreenDataPackage crossScreenDataPackage) {
            nodeGroup.clear();
            crossScreenDataPackage.getRoomMap().values().forEach(it -> {
                RoomSwitchNode node = new RoomSwitchNode(screen);
                node.update(it, it == crossScreenDataPackage.getCurrentRoomData());
                nodeGroup.addActor(node);
            });
        }
    }

    public RoomSwitchBoardVM(AbstractComikeScreen screen) {
        this.screen = screen;

        // ------ candidates ------
        this.smallModeTable = new SmallModeTable();
        this.fullModeTable = new FullModeTable();

    }

    public void intoSmallMode() {
        this.setActor(smallModeTable);
    }

    public void intoFullMode() {
        CrossScreenDataPackage crossScreenDataPackage = screen.getGame().getLogicContext().getCrossScreenDataPackage();
        fullModeTable.update(crossScreenDataPackage);
        this.setActor(fullModeTable);
    }
}

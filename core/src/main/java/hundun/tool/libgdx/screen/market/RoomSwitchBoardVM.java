package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.LogicContext.CrossScreenDataPackage;
import hundun.tool.logic.data.RoomRuntimeData;

/**
 * @author hundun
 * Created on 2023/05/17
 */
public class RoomSwitchBoardVM extends Container<Table> {
    MarketScreen screen;

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

    private class RoomSwitchNode extends Table {

        TextureRegionDrawable signTexture;

        Image signImage;
        Label label;
        RoomRuntimeData roomRuntimeData;

        public RoomSwitchNode(MarketScreen screen) {
            this.signTexture = new TextureRegionDrawable(new Texture(Gdx.files.internal("sign.png")));

            this.signImage = new Image();
            this.add(signImage)
                    .width(screen.getGame().getScreenContext().getLayoutConst().ROOM_SWITCH_NODE_HEIGHT)
                    .height(screen.getGame().getScreenContext().getLayoutConst().ROOM_SWITCH_NODE_HEIGHT)
                    ;

            this.label = new Label("", screen.getGame().getMainSkin());
            this.add(label)
                    .height(screen.getGame().getScreenContext().getLayoutConst().ROOM_SWITCH_NODE_HEIGHT)
                    ;

            this.setBackground(DrawableFactory.getSimpleBoardBackground(
                    screen.getGame().getScreenContext().getLayoutConst().ROOM_SWITCH_NODE_WIDTH,
                    screen.getGame().getScreenContext().getLayoutConst().ROOM_SWITCH_NODE_HEIGHT
            ));

            this.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    screen.getGame().getLogicContext().getCrossScreenDataPackage().setCurrentRoomData(roomRuntimeData);
                    screen.updateUIAfterRoomChanged();
                }
            });
        }

        public void update(RoomRuntimeData roomRuntimeData, boolean isCurrent) {
            this.roomRuntimeData = roomRuntimeData;

            if (isCurrent) {
                signImage.setDrawable(signTexture);
            } else {
                signImage.setDrawable(null);
            }
            label.setText(roomRuntimeData.getName());
        }
    }

    public RoomSwitchBoardVM(MarketScreen screen) {
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

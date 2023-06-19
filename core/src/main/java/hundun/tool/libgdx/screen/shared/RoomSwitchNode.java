package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.logic.data.RoomRuntimeData;

public class RoomSwitchNode extends Table {

    TextureRegionDrawable signTexture;

    Image signImage;
    Label label;
    RoomRuntimeData roomRuntimeData;

    public RoomSwitchNode(AbstractComikeScreen screen) {
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

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.onRoomSwitchClicked(roomRuntimeData);
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

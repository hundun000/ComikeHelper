package hundun.tool.libgdx.screen.shared;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.other.CameraGestureListener;
import hundun.tool.libgdx.other.CameraMouseListener;
import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.save.RoomSaveData.DeskAreaInfo;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskAreaVM extends Table {
    public AbstractComikeScreen screen;
    @Getter
    Map<String, DeskVM> nodes = new LinkedHashMap<>();
    @Getter
    CameraDataPackage cameraDataPackage;

    public DeskAreaVM(AbstractComikeScreen screen) {
        this.screen = screen;
        this.cameraDataPackage = new CameraDataPackage();

        if (screen.getGame().debugMode) {
            this.debugAll();
        }
    }

    public void updateDeskDatas(
            DeskAreaInfo deskAreaInfo,
            List<DeskRuntimeData> deskDatas,
            @Null FileHandle roomImage) {
        this.clear();
        nodes.clear();

        Image background = new Image();
        if (roomImage != null) {
            background.setDrawable(new TextureRegionDrawable(new Texture(roomImage)));
        } else {
            background.setDrawable(DrawableFactory.getSimpleBoardBackground());
        }
        int roomWidth = deskAreaInfo.getDeskAreaWidth() + deskAreaInfo.getDeskAreaPadLeft() + deskAreaInfo.getDeskAreaPadRight();
        int roomHeight = deskAreaInfo.getDeskAreaHeight() + deskAreaInfo.getDeskAreaPadTop() + deskAreaInfo.getDeskAreaPadBottom();
        
        background.setBounds(0, 0, roomWidth, roomHeight);
        
        this.addActor(background);
        this.addListener(new CameraGestureListener(cameraDataPackage));
        this.addListener(new CameraMouseListener(cameraDataPackage));
        this.getCameraDataPackage().forceSet(roomWidth / 2.0f, roomHeight/ 2.0f, null);

        deskDatas.forEach(deskData -> {
            DeskVM actor = new DeskVM(this, deskData);
            nodes.put(deskData.getIdName(), actor);

            Vector2 roomPos = deskData.getLocation().getPos();
            actor.setBounds(
                    deskAreaInfo.getDeskAreaPadLeft() + roomPos.x, 
                    deskAreaInfo.getDeskAreaPadBottom() + roomPos.y, 
                    screen.getGame().getScreenContext().getLayoutConst().DESK_WIDTH, 
                    screen.getGame().getScreenContext().getLayoutConst().DESK_HEIGHT
                    );
            actor.addListener(new DeskClickListener(screen, actor));
            this.addActor(actor);

        });
    }

    public void updateCartData(@Null GoodRuntimeData changed) {

        Collection<DeskVM> needUpdateNodes;
        if (changed != null) {
            needUpdateNodes = new HashSet<>(1);
            needUpdateNodes.add(nodes.get(changed.getOwnerRef().getIdName()));
        } else {
            needUpdateNodes = nodes.values();
        }

        needUpdateNodes.forEach(it -> {
            it.updateTagTable();
        });
    }

}

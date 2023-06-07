package hundun.tool.libgdx.screen.shared;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Null;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.other.CameraGestureListener;
import hundun.tool.libgdx.other.CameraMouseListener;
import hundun.tool.libgdx.screen.AbstractComikeScreen;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
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

    public void updateDeskDatas(int roomWidth, int roomHeight, List<DeskRuntimeData> deskDatas) {
        this.clear();
        nodes.clear();

        Image background = new Image();
        background.setDrawable(DrawableFactory.getSimpleBoardBackground());
        background.setBounds(0, 0, roomWidth, roomHeight);
        this.addActor(background);
        this.addListener(new CameraGestureListener(cameraDataPackage));
        this.addListener(new CameraMouseListener(cameraDataPackage));
        this.getCameraDataPackage().forceSet(roomWidth / 2.0f, roomHeight/ 2.0f, null);

        deskDatas.forEach(deskData -> {
            DeskVM actor = new DeskVM(this, deskData);
            nodes.put(deskData.getName(), actor);

            Vector2 roomPos = deskData.getLocation().getPos();
            actor.setBounds(roomPos.x, roomPos.y, screen.getGame().getScreenContext().getLayoutConst().DESK_WIDTH, screen.getGame().getScreenContext().getLayoutConst().DESK_HEIGHT);
            actor.addListener(new DeskClickListener(screen, actor));
            this.addActor(actor);

        });
    }

    public void updateCartData(@Null GoodRuntimeData changed) {

        Collection<DeskVM> needUpdateNodes;
        if (changed != null) {
            needUpdateNodes = new HashSet<>(1);
            needUpdateNodes.add(nodes.get(changed.getOwnerRef().getName()));
        } else {
            needUpdateNodes = nodes.values();
        }

        needUpdateNodes.forEach(it -> {
            it.updateTagTable();
        });
    }

}

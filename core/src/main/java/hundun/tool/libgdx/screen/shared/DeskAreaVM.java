package hundun.tool.libgdx.screen.shared;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.other.CameraGestureListener;
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

    public void updateCartData(List<GoodRuntimeData> list) {
        Set<DeskVM> staredNodes = list.stream()
            .filter(it -> nodes.containsKey(it.getOwnerRef().getName()))
            .map(it -> nodes.get(it.getOwnerRef().getName()))
            .collect(Collectors.toSet());
            ;
        nodes.values().forEach(it -> {
            it.update(staredNodes.contains(it));
        });
    }

}

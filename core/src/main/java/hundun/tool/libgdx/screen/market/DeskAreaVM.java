package hundun.tool.libgdx.screen.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.MarketScreen.TiledMapClickListener;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskAreaVM extends Table {
    public MarketScreen parent;
    @Getter
    Map<String, DeskVM> nodes = new LinkedHashMap<>();



    public DeskAreaVM(MarketScreen parent) {
        this.parent = parent;

        if (parent.getGame().debugMode) {
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
        this.addListener(parent.new MyGestureListener());

        deskDatas.forEach(deskData -> {
            DeskVM actor = new DeskVM(this, deskData);
            nodes.put(deskData.getName(), actor);

            Vector2 roomPos = deskData.getLocation().getPos();
            actor.setBounds(roomPos.x, roomPos.y, parent.getGame().getScreenContext().getLayoutConst().DESK_WIDTH, parent.getGame().getScreenContext().getLayoutConst().DESK_HEIGHT);
            EventListener eventListener = new TiledMapClickListener(parent.getGame(), actor);
            actor.addListener(eventListener);
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

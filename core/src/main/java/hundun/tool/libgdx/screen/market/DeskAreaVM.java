package hundun.tool.libgdx.screen.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.libgdx.screen.MarketScreen.TiledMapClickListener;
import hundun.tool.logic.data.DeskRuntimeData;
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
    
    public void upodateData(List<DeskRuntimeData> deskDatas) {
        nodes.clear();
        
        deskDatas.forEach(deskData -> {
            DeskVM actor = new DeskVM(this, deskData);
            nodes.put(actor.getName(), actor);
            
            Vector2 roomPos = deskData.getLocation().getPos();
            actor.setBounds(roomPos.x, roomPos.y, DeskRuntimeData.WIDTH, DeskRuntimeData.HEIGHT);
            EventListener eventListener = new TiledMapClickListener(parent.getGame(), actor);
            actor.addListener(eventListener);
            this.addActor(actor);
            
        });
    }
}

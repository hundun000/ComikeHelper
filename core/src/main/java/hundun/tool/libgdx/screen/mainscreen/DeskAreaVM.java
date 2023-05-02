package hundun.tool.libgdx.screen.mainscreen;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.EventListener;import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import hundun.tool.libgdx.screen.mainscreen.MainScreen.TiledMapClickListener;
import hundun.tool.logic.data.DeskData;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskAreaVM extends Table {
    public MainScreen parent;
    Map<String, DeskVM> nodes = new LinkedHashMap<>();
    
    private Vector2 roomPos(int index) {
        return new Vector2(
                10 + (DeskVM.WIDTH + 25) * index, 
                10 + 25
                );
    }
    
    public DeskAreaVM(MainScreen parent, List<DeskData> deskDatas) {
        this.parent = parent;
        
        deskDatas.forEach(deskData -> {
            DeskVM actor = new DeskVM(this, deskData);
            nodes.put(actor.getName(), actor);
            
            Vector2 roomPos = roomPos(deskData.getRoomIndex());
            actor.setBounds(roomPos.x, roomPos.y, DeskVM.WIDTH, DeskVM.HEIGHT);
            EventListener eventListener = new TiledMapClickListener(parent.getGame(), actor);
            actor.addListener(eventListener);
            this.addActor(actor);
            
        });
        
        if (parent.getGame().debugMode) {
            this.debugAll();
        }
    }
}

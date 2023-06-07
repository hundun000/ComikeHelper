package hundun.tool.logic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/06/07
 */
public class TextureManager {
    ComikeHelperGame game;
    @Getter
    Map<GoodRuntimeTag, Texture> tagImageMap = new HashMap<>();
    
    public TextureManager(ComikeHelperGame game) {
        this.game = game;
    }
    
    public void lazyInitOnCreateStage1() {
        tagImageMap.put(GoodRuntimeTag.IN_CART, new Texture(Gdx.files.internal("tags/1F6D2.png")));
        tagImageMap.put(GoodRuntimeTag.STAR, new Texture(Gdx.files.internal("tags/2B50.png")));
        tagImageMap.put(GoodRuntimeTag.WAITING, new Texture(Gdx.files.internal("tags/23F3.png")));
        tagImageMap.put(GoodRuntimeTag.HEART, new Texture(Gdx.files.internal("tags/2665.png")));
        tagImageMap.put(GoodRuntimeTag.DONE, new Texture(Gdx.files.internal("tags/2705.png")));
    }

}

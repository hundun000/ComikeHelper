package hundun.tool.logic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
    @Getter
    Drawable mcStyleTable;
    @Getter
    Drawable mcStyleTableTop;
    @Getter
    Drawable mcStyleTableBottom;
    
    public TextureManager(ComikeHelperGame game) {
        this.game = game;
    }
    
    public void lazyInitOnCreateStage1() {
        tagImageMap.put(GoodRuntimeTag.IN_CART, new Texture(Gdx.files.internal("tags/1F6D2.png")));
        tagImageMap.put(GoodRuntimeTag.STAR, new Texture(Gdx.files.internal("tags/2B50.png")));
        tagImageMap.put(GoodRuntimeTag.WAITING, new Texture(Gdx.files.internal("tags/23F3.png")));
        tagImageMap.put(GoodRuntimeTag.HEART, new Texture(Gdx.files.internal("tags/2665.png")));
        tagImageMap.put(GoodRuntimeTag.DONE, new Texture(Gdx.files.internal("tags/2705.png")));
    
        NinePatch tempNinePatch;
        
        tempNinePatch = new NinePatch(
                ignoreFirstLineTexture("McStyleTable.9.png"),
                20, 20, 20, 20
                ); 
        mcStyleTable = new NinePatchDrawable(tempNinePatch);
        tempNinePatch = new NinePatch(
                ignoreFirstLineTexture("McStyleTable-top.9.png"),
                20, 20, 20, 0
                ); 
        mcStyleTableTop = new NinePatchDrawable(tempNinePatch);
        tempNinePatch = new NinePatch(
                ignoreFirstLineTexture("McStyleTable-bottom.9.png"),
                20, 20, 0, 20
                ); 
        mcStyleTableBottom = new NinePatchDrawable(tempNinePatch);
    }
    
    private TextureRegion ignoreFirstLineTexture(String file) {
        Texture texture = new Texture(Gdx.files.internal(file));
        return new TextureRegion(texture,
                1, 1, texture.getWidth() -1, texture.getHeight() -1
                );
    }

}

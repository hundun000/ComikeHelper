package hundun.tool.libgdx.screen.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.logic.data.DeskData;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class DeskVM extends Table {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 150;
    
    MainScreen parent;
    DeskData deskData;
    
    @Getter
    Vector2 pos;

    
    @Getter
    int index;
    
    static TextureRegion testTexture;
    
    static {
        Pixmap pixmap200 = new Pixmap(Gdx.files.internal("badlogic.jpg"));
        Pixmap pixmap100 = new Pixmap(80, 80, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        testTexture = new TextureRegion(new Texture(pixmap100));
        pixmap200.dispose();
        pixmap100.dispose();
    }
    
    public DeskVM(MainScreen parent, DeskData deskData, int index) {
        this.parent = parent;
        this.deskData = deskData;
        this.index = index;

        
        this.setBackground(new TextureRegionDrawable(new TextureRegion(TextureFactory.getSimpleBoardBackground(WIDTH, HEIGHT))));
        this.add(new Image(testTexture));
        
        this.pos = new Vector2(
                10 + (WIDTH + 25) * index, 
                10 + 25
                );

    }

}

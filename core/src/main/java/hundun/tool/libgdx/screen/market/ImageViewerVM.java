package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.corelib.base.util.TextureFactory;
import hundun.tool.libgdx.other.CameraDataPackage;
import hundun.tool.libgdx.other.CameraGestureListener;
import hundun.tool.libgdx.screen.MarketScreen;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class ImageViewerVM extends Table {

    public MarketScreen screen;
    @Getter
    CameraDataPackage cameraDataPackage;


    Image content;
    Image background;
    public ImageViewerVM(MarketScreen screen) {
        this.screen = screen;
        this.cameraDataPackage = new CameraDataPackage();

        this.background = new Image();
        this.addActor(background);

        this.content = new Image();
        this.addActor(content);

        this.addListener(new CameraGestureListener(cameraDataPackage));
    }

    public void updateImageAndShow(Texture texture) {


        int backgroundWidth = (int) (1.5 * Math.max(Gdx.graphics.getWidth(), texture.getWidth()));
        int backgroundHeight = (int) (1.5 * Math.max(Gdx.graphics.getHeight(), texture.getHeight()));
        Texture backgroundUnit = new Texture(Gdx.files.internal("imagePreviewBackground.png"));
        backgroundUnit.setWrap(TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion backgroundTexture = new TextureRegion(backgroundUnit);
        backgroundTexture.setRegion(0, 0, backgroundWidth, backgroundHeight);
        background.setDrawable(new TextureRegionDrawable(backgroundTexture));
        background.setBounds(0, 0, backgroundWidth, backgroundHeight);

        content.setDrawable(new TextureRegionDrawable(texture));
        content.setBounds((backgroundWidth - texture.getWidth()) / 2.0f, (backgroundHeight - texture.getHeight()) / 2.0f, texture.getWidth(), texture.getHeight());

        cameraDataPackage.forceSet(backgroundWidth / 2.0f, backgroundHeight / 2.0f, 0);

        this.setVisible(true);
    }

    public void hide() {
        content.setDrawable(null);
        this.setVisible(false);
    }

}

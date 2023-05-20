package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.corelib.base.util.TextureFactory;
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
    private float currentCameraX;
    @Getter
    private float currentCameraY;
    @Getter
    private float currentCameraZoomWeight;
    @Getter
    @Setter
    private boolean currentCameraZoomDirty;

    Image content;
    Image background;
    public ImageViewerVM(MarketScreen screen) {
        this.screen = screen;


        this.background = new Image();
        this.addActor(background);

        this.content = new Image();
        this.addActor(content);

        this.addListener(new ImageViewerGestureListener());
    }

    public void updateImageAndShow(Texture texture) {


        int backgroundWidth = Math.max(Gdx.graphics.getWidth(), texture.getWidth());
        int backgroundHeight = Math.max(Gdx.graphics.getHeight(), texture.getHeight());
        Texture backgroundTexture = TextureFactory.getSimpleBoardBackground(backgroundWidth, backgroundHeight);
        background.setDrawable(new TextureRegionDrawable(backgroundTexture));
        background.setBounds(0, 0, backgroundWidth, backgroundHeight);

        content.setDrawable(new TextureRegionDrawable(texture));
        content.setBounds((backgroundWidth - texture.getWidth()) / 2.0f, (backgroundHeight - texture.getHeight()) / 2.0f, texture.getWidth(), texture.getHeight());

        currentCameraX = backgroundWidth / 2.0f;
        currentCameraY = backgroundHeight / 2.0f;
        currentCameraZoomWeight = 0;
        setCurrentCameraZoomDirty(true);

        this.setVisible(true);
    }

    public void hide() {
        content.setDrawable(null);
        this.setVisible(false);
    }

    private void modifyCurrentCamera(Float deltaX, Float deltaY) {
        if (deltaX != null) {
            currentCameraX += deltaX;
        }
        if (deltaY != null) {
            currentCameraY += deltaY;
        }
    }

    private void modifyCurrentCameraZoomWeight(Float delta) {
        currentCameraZoomWeight += delta;
    }

    private class ImageViewerGestureListener extends ActorGestureListener {

        @Override
        public void zoom(InputEvent event, float initialDistance, float distance) {
            super.zoom(event, initialDistance, distance);

            float deltaValue = (distance - initialDistance) < 0 ? 0.1f : -0.1f;
            ImageViewerVM.this.modifyCurrentCameraZoomWeight(deltaValue);
            ImageViewerVM.this.setCurrentCameraZoomDirty(true);
        }

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            super.pan(event, x, y, deltaX, deltaY);

            float cameraDeltaX = -deltaX;
            float cameraDeltaY = -deltaY;
            ImageViewerVM.this.modifyCurrentCamera(cameraDeltaX, cameraDeltaY);
        }
    }
}

package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;

public class PopupCloseButton extends TextButton {

    MarketScreen screen;

    public PopupCloseButton(MarketScreen screen) {
        super("x", screen.getGame().getMainSkin());
        this.screen = screen;
    }

    public void updateCallbackAndShow(Runnable callback){
        this.clearListeners();
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                callback.run();
                hide();
            }
        });

        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }
}

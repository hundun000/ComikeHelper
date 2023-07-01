package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.gdxgame.corelib.base.BaseHundunGame;
import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.ComikeHelperGame;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/07/01
 */
public class ScrollDialog extends Table {
    final ComikeHelperGame game;
    @Getter
    Label content;
    @Getter
    Label hintLabel;
    public ScrollDialog(ComikeHelperGame game) {
        this.game = game;
        
        this.setBackground(game.getTextureManager().getMcStyleTable());

        Table contentTable = new Table();
        ScrollPane scrollPane = new ScrollPane(contentTable, game.getMainSkin());
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        this.content = new Label("test", game.getMainSkin());
        contentTable.add(content).grow();
        this.add(scrollPane)
                .pad(50)
                .grow();
        
        
        
        this.hintLabel = new Label("hint", game.getMainSkin());
        this.row();
        this.add(hintLabel).row();
    }
    
    public void addButton(String text, ClickListener clickListener) {
        TextButton button = new TextButton(text, game.getMainSkin());
        button.addListener(clickListener);
        this.add(button).padRight(20);
    }

}

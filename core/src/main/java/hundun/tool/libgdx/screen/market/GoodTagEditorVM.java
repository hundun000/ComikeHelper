package hundun.tool.libgdx.screen.market;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import java.util.HashMap;
import java.util.Map;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;

/**
 * @author hundun
 * Created on 2023/05/10
 */
public class GoodTagEditorVM extends Table {
    
    MarketScreen screen;
    CheckBox onCheckBox;
    CheckBox offCheckBox;
    ButtonGroup<CheckBox> buttonGroup;


    public GoodTagEditorVM(MarketScreen screen, GoodRuntimeData goodRuntimeData, GoodRuntimeTag tag) {
        this.screen = screen;

        Texture texture = screen.getGame().getScreenContext().getTagImageMap().get(tag);
        Image image = new Image(texture);
        this.add(image);
        
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (onCheckBox.isChecked()) {
                    screen.getGame().getLogicContext().modifyGoodTag(goodRuntimeData, tag, true);
                } else if (offCheckBox.isChecked()) {
                    screen.getGame().getLogicContext().modifyGoodTag(goodRuntimeData, tag, false);
                }
            }
        };
        onCheckBox = new CheckBox("on", screen.getGame().getMainSkin());
        onCheckBox.addListener(changeListener);
        this.add(onCheckBox);
        
        offCheckBox = new CheckBox("off", screen.getGame().getMainSkin());
        offCheckBox.addListener(changeListener);
        this.add(offCheckBox);
        
        buttonGroup = new ButtonGroup<>(onCheckBox, offCheckBox);
    }


}

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
import java.util.concurrent.atomic.AtomicBoolean;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.tool.libgdx.screen.MarketScreen;
import hundun.tool.logic.data.GoodRuntimeData;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;

/**
 * @author hundun
 * Created on 2023/05/10
 */
public class GoodTagEditorNodeVM extends Table {
    
    MarketScreen screen;
    CheckBox offCheckBox;
    Map<GoodRuntimeTag, CheckBox> tagCheckBoxMap = new HashMap<>();
    ButtonGroup<CheckBox> buttonGroup;


    public GoodTagEditorNodeVM(MarketScreen screen, GoodRuntimeData goodRuntimeData, GoodRuntimeTag... tags) {
        this.screen = screen;

        buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setUncheckLast(true);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox checked = buttonGroup.getChecked();
                
                tagCheckBoxMap.forEach((k, v) -> {
                    if (v == checked) {
                        screen.getGame().getLogicContext().modifyGoodTag(goodRuntimeData, k, true);
                    } else {
                        screen.getGame().getLogicContext().modifyGoodTag(goodRuntimeData, k, false);
                    }
                });
                screen.getGame().getLogicContext().calculateAndSaveCurrentUserData();
            }
        };

        for (GoodRuntimeTag tag : tags) {
            CheckBox checkBox = new CheckBox("", screen.getGame().getMainSkin());
            this.add(checkBox);
            
            Texture texture = screen.getGame().getTextureManager().getTagImageMap().get(tag);
            Image image = new Image(texture);
            this.add(image);

            tagCheckBoxMap.put(tag, checkBox);
            buttonGroup.add(checkBox);
        }
        offCheckBox = new CheckBox("off", screen.getGame().getMainSkin());
        this.add(offCheckBox);
        buttonGroup.add(offCheckBox);

        // load current
        AtomicBoolean isAnyTagChecked = new AtomicBoolean(false);
        tagCheckBoxMap.forEach((k, v) -> {
            if (goodRuntimeData.getTagStateMap().get(k)) {
                v.setChecked(true);
                screen.getGame().getFrontend().log(this.getClass().getSimpleName(), 
                        "in {0} currentChecked = {1}", JavaFeatureForGwt.listOf(tags).toString(), k);
                isAnyTagChecked.set(true);
            }
        });
        if (!isAnyTagChecked.get()) {
            offCheckBox.setChecked(true);
            screen.getGame().getFrontend().log(this.getClass().getSimpleName(), 
                    "in {0} currentChecked = off", JavaFeatureForGwt.listOf(tags).toString());
        }

        // must add listener after add to buttonGroup;
        tagCheckBoxMap.values().forEach(it -> it.addListener(changeListener));
        offCheckBox.addListener(changeListener);
    }


}

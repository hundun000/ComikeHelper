package hundun.tool.libgdx.other;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

import hundun.gdxgame.corelib.base.util.DrawableFactory;
import lombok.Getter;

public class MyWindow extends Table {
    @Getter
    Label titleLabel;
    @Getter
    Table titleTable;
    @Getter
    Table mainTable;

    public MyWindow (String title, Skin skin) {
        this(title, skin.get(WindowStyle.class));
    }

    public MyWindow (String title, WindowStyle style) {

        titleLabel = newLabel(title, new LabelStyle(style.titleFont, style.titleFontColor));
        titleLabel.setEllipsis(true);

        titleTable = new Table();
        titleTable.setBackground(style.background);
        titleTable.add(titleLabel).grow();
        super.add(titleTable).height(80)
                .growX()
                .row();

        mainTable = new Table();
        mainTable.setBackground(style.stageBackground);
        super.add(mainTable)
                .grow();
    }

    protected Label newLabel (String text, LabelStyle style) {
        return new Label(text, style);
    }

    @Override
    public <T extends Actor> Cell<T> add(T actor) {
        return mainTable.add(actor);
    }
}

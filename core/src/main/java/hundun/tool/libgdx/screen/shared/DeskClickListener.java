package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.ComikeHelperGame;
import hundun.tool.libgdx.screen.AbstractComikeScreen;

public class DeskClickListener extends ClickListener {
    ComikeHelperGame game;
    AbstractComikeScreen screen;
    private DeskVM vm;

    public DeskClickListener(AbstractComikeScreen screen, DeskVM vm) {
        this.game = screen.getGame();
        this.screen = screen;
        this.vm = vm;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        screen.onDeskClicked(vm);
        game.getFrontend().log(this.getClass().getSimpleName(), vm.getDeskData().getUiName() + " has been clicked.");
    }
}

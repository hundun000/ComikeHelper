package hundun.tool.libgdx.screen.mainscreen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class CameraControlBoardVM extends Table {
    static int STEP = 50;
    MainScreen parent;
    
    public CameraControlBoardVM(MainScreen parent) {
        this.parent = parent;
        
        TextButton moveLeftButton = new TextButton("左", parent.getGame().getMainSkin());
        moveLeftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.setCurrentCameraX(parent.getCurrentCameraX() - STEP);
            }
        });
        this.add(moveLeftButton);
     
        TextButton moveRightButton = new TextButton("右", parent.getGame().getMainSkin());
        moveRightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.setCurrentCameraX(parent.getCurrentCameraX() + STEP);
            }
        });
        this.add(moveRightButton);
    }

}

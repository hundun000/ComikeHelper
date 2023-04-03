package hundun.tool.libgdx.screen.market;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import hundun.tool.libgdx.screen.MarketScreen;

/**
 * @author hundun
 * Created on 2023/05/09
 */
public class CameraControlBoardVM extends Table {
    static float STEP = 50.0f;
    MarketScreen parent;
    
    public CameraControlBoardVM(MarketScreen parent) {
        this.parent = parent;
        
        Table directionPart = new Table();
        
        TextButton moveUpButton = new TextButton("上", parent.getGame().getMainSkin());
        moveUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCamera(null, STEP);
            }
        });
        directionPart.add(new Image());
        directionPart.add(moveUpButton);
        directionPart.add(new Image());
        directionPart.row();
        
        TextButton moveLeftButton = new TextButton("左", parent.getGame().getMainSkin());
        moveLeftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCamera(-STEP, null);
            }
        });
        directionPart.add(moveLeftButton);
        directionPart.add(new Image());
        
        
        TextButton moveRightButton = new TextButton("右", parent.getGame().getMainSkin());
        moveRightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCamera(STEP, null);
            }
        });
        directionPart.add(moveRightButton);
        directionPart.row();
        
        TextButton moveDownButton = new TextButton("下", parent.getGame().getMainSkin());
        moveDownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCamera(null, -STEP);
            }
        });
        directionPart.add(new Image());
        directionPart.add(moveDownButton);
        directionPart.add(new Image());
        
        this.add(directionPart);
        
        
        TextButton zoomInButton = new TextButton("+", parent.getGame().getMainSkin());
        zoomInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCameraZoomPower(-1.0f);
                parent.setCurrentCameraZoomDirty(true);
            }
        });
        this.add(zoomInButton);
        
        TextButton zoomOutButton = new TextButton("-", parent.getGame().getMainSkin());
        zoomOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                parent.getGame().getManagerContext().getCrossScreenDataPackage().modifyCurrentCameraZoomPower(1.0f);
                parent.setCurrentCameraZoomDirty(true);
            }
        });
        this.add(zoomOutButton);
    }

}

package hundun.tool.libgdx.screen.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import lombok.Getter;

/**
 * @author hundun
 * Created on 2023/06/05
 */
public class ImageBoxVM extends Table {
    
    @Getter
    Image image;
    
    public ImageBoxVM(int imageCellWidth, int imageCellHeight) {
        NinePatch background = new NinePatch(
                new Texture(Gdx.files.internal("imageBox.png")),
                4, 5, 4, 5
                ); 
        this.setBackground(new NinePatchDrawable(background));
        
        
        this.image = new Image();
        this.add(image)
            .width(imageCellWidth)
            .height(imageCellHeight)
            .pad(imageCellHeight * 0.1f, 
                    imageCellWidth * 0.1f, 
                    imageCellHeight * 0.1f, 
                    imageCellWidth * 0.1f)
            ;
    }

}

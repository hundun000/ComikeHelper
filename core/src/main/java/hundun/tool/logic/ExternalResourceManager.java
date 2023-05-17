package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import lombok.Getter;

public class ExternalResourceManager {
    @Getter
    Texture testTexture;

    public void lazyInitOnGameCreate() {
        this.testTexture = new Texture(Gdx.files.external("defaultTarget.png"));
    }


    public String getExtRoot() {
        return Gdx.files.getExternalStoragePath();
    }
}

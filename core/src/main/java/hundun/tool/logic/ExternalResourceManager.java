package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.util.ExternalJsonSaveTool;
import lombok.Getter;

public class ExternalResourceManager {
    private static final String USER_ROOT_FOLDER = "user/";
    private static final String SHARED_ROOT_FOLDER = "shared/";
    @Getter
    Texture testTexture;

    ExternalJsonSaveTool<ExternalMainData> sharedMainDataSaveTool;
    //ExternalJsonSaveTool<ExternalMainData> userMainDataSaveTool;
    ExternalJsonSaveTool<ExternalUserPrivateData> userPrivateDataSaveTool;

    public ExternalResourceManager() {
        this.sharedMainDataSaveTool = new ExternalJsonSaveTool<>(SHARED_ROOT_FOLDER, "main.json", ExternalMainData.class);
        //this.userMainDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER + "main.json", ExternalMainData.class);
        this.userPrivateDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER, "private.json", ExternalUserPrivateData.class);
    }


    public void lazyInitOnGameCreate(ExternalMainData masterMainData, ExternalUserPrivateData masterUserPrivateData) {
        sharedMainDataSaveTool.lazyInitOnGameCreate();
        userPrivateDataSaveTool.lazyInitOnGameCreate();

        this.testTexture = new Texture(Gdx.files.external("defaultTarget.png"));
        ExternalMainData sharedMainData = sharedMainDataSaveTool.readRootSaveData();
        //ExternalMainData userMainData = userMainDataSaveTool.readRootSaveData();
        ExternalUserPrivateData userPrivateData = userPrivateDataSaveTool.readRootSaveData();

        merge(masterMainData, sharedMainData);
        merge(masterUserPrivateData, userPrivateData);
    }

    private void merge(ExternalUserPrivateData master, ExternalUserPrivateData other) {
        if (other == null) {
            return;
        }
        if (other.getCartGoodIds() != null) {
            master.getCartGoodIds().addAll(other.getCartGoodIds());
        }
    }

    private void merge(ExternalMainData master, ExternalMainData other) {
        if (other == null) {
            return;
        }
        if (other.getRoomSaveDatas() != null) {
            master.getRoomSaveDatas().addAll(other.getRoomSaveDatas());
        }
    }

    public String getExtRoot() {
        return Gdx.files.getExternalStoragePath();
    }

    public void saveAsSharedData(ExternalMainData externalMainData) {
        sharedMainDataSaveTool.writeRootSaveData(externalMainData);
    }

    public void saveAsUserData(ExternalUserPrivateData userPrivateData) {
        userPrivateDataSaveTool.writeRootSaveData(userPrivateData);
    }
}

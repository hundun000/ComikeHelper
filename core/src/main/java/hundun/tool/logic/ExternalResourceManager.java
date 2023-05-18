package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Map;

import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.external.ExternalAllData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool;
import hundun.tool.logic.util.SimpleExternalJsonSaveTool;
import lombok.Getter;

public class ExternalResourceManager {
    private static final String USER_ROOT_FOLDER = "user/";
    private static final String SHARED_ROOT_FOLDER = "shared/";
    @Getter
    Texture testTexture;

    ComplexExternalJsonSaveTool<ExternalMainData, DeskSaveData> sharedComplexSaveTool;
    //ExternalJsonSaveTool<ExternalMainData> userMainDataSaveTool;
    SimpleExternalJsonSaveTool<ExternalUserPrivateData> userPrivateDataSaveTool;

    public ExternalResourceManager() {
        this.sharedComplexSaveTool = new ComplexExternalJsonSaveTool<>(
            SHARED_ROOT_FOLDER,
            "main.json", ExternalMainData.class,
            "desk.json", DeskSaveData.class
            );
        //this.userMainDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER + "main.json", ExternalMainData.class);
        this.userPrivateDataSaveTool = new SimpleExternalJsonSaveTool<>(USER_ROOT_FOLDER, "private.json", ExternalUserPrivateData.class);
    }


    public void lazyInitOnGameCreate(ExternalAllData masterMainData, ExternalUserPrivateData masterUserPrivateData) {
        sharedComplexSaveTool.lazyInitOnGameCreate();
        userPrivateDataSaveTool.lazyInitOnGameCreate();

        this.testTexture = new Texture(Gdx.files.external("defaultTarget.png"));
        ExternalMainData sharedMainData = sharedComplexSaveTool.readMainData();
        Map<String, DeskSaveData> deskDataList = sharedComplexSaveTool.readAllSubFolderData();
        //ExternalMainData userMainData = userMainDataSaveTool.readRootSaveData();
        ExternalUserPrivateData userPrivateData = userPrivateDataSaveTool.readRootSaveData();

        merge(masterMainData, sharedMainData, deskDataList);
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

    private void merge(ExternalAllData master, ExternalMainData other, Map<String, DeskSaveData> deskDataList) {
        if (other != null) {
            master.setExternalMainData(other);
        }
        if (deskDataList != null) {
            master.setExternalMainData(other);
        }
    }

    public String getExtRoot() {
        return Gdx.files.getExternalStoragePath();
    }

    public void saveAsSharedData(ExternalMainData externalMainData) {
        sharedComplexSaveTool.writeMainData(externalMainData);
    }

    public void saveAsUserData(ExternalUserPrivateData userPrivateData) {
        userPrivateDataSaveTool.writeRootSaveData(userPrivateData);
    }

    public void saveAsSharedData(Map<String, DeskSaveData> deskDatas) {
        sharedComplexSaveTool.writeAllSubFolderData(deskDatas);
    }
}

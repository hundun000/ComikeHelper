package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Map;

import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.external.ExternalAllData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool;
import hundun.tool.logic.util.ExternalExcelSaveTool;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool.DeskExternalRuntimeData;
import hundun.tool.logic.util.SimpleExternalJsonSaveTool;
import lombok.Getter;

public class ExternalResourceManager {
    private static final String BUILDER_ROOT_FOLDER = "builder/";
    private static final String USER_ROOT_FOLDER = "user/";
    private static final String SHARED_ROOT_FOLDER = "shared/";
    @Getter
    FileHandle defaultCoverFileHandle;

    ComplexExternalJsonSaveTool sharedComplexSaveTool;
    //ExternalJsonSaveTool<ExternalMainData> userMainDataSaveTool;
    SimpleExternalJsonSaveTool<ExternalUserPrivateData> userPrivateDataSaveTool;
    ExternalExcelSaveTool builderExcelSaveTool;
    
    public ExternalResourceManager() {
        this.sharedComplexSaveTool = new ComplexExternalJsonSaveTool(
            SHARED_ROOT_FOLDER
            );
        //this.userMainDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER + "main.json", ExternalMainData.class);
        this.userPrivateDataSaveTool = new SimpleExternalJsonSaveTool<>(USER_ROOT_FOLDER, "private.json", ExternalUserPrivateData.class);
        this.builderExcelSaveTool = new ExternalExcelSaveTool(BUILDER_ROOT_FOLDER);
    }


    public void providerExternalGameplayData(ExternalAllData masterMainData, ExternalUserPrivateData masterUserPrivateData) {
        this.defaultCoverFileHandle = Gdx.files.internal("defaultCover.png");

        sharedComplexSaveTool.lazyInitOnGameCreate(defaultCoverFileHandle);
        userPrivateDataSaveTool.lazyInitOnGameCreate();
        


        // ExternalMainData userMainData = userMainDataSaveTool.readRootSaveData();
        ExternalUserPrivateData userPrivateData = userPrivateDataSaveTool.readRootSaveData();

        merge(masterMainData,
            sharedComplexSaveTool.readMainData(),
            sharedComplexSaveTool.readAllSubFolderData()
        );
        merge(masterUserPrivateData, userPrivateData);
    }
    
    
    public void providerExcelGameplayData(ExternalAllData externalAllData, ExternalUserPrivateData userPrivateData) {
        builderExcelSaveTool.lazyInitOnGameCreate();
        
        builderExcelSaveTool.lazyInitOnRuntime("test.xlsx");
        
        List<Map<Integer, String>> data = builderExcelSaveTool.readRootSaveData();
        System.out.println("data = " + data);
    }

    private void merge(ExternalUserPrivateData master, ExternalUserPrivateData other) {
        if (other == null) {
            return;
        }
        if (other.getCartGoodIds() != null) {
            master.getCartGoodIds().addAll(other.getCartGoodIds());
        }
    }

    private void merge(ExternalAllData master,
                       ExternalMainData other,
                       Map<String, DeskExternalRuntimeData> deskExternalRuntimeDataMap
    ) {
        if (other != null) {
            master.setExternalMainData(other);
        }
        if (deskExternalRuntimeDataMap != null) {
            master.getDeskExternalRuntimeDataMap().putAll(deskExternalRuntimeDataMap);
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

    public void saveAsSharedData(Map<String, DeskExternalRuntimeData> deskDatas) {
        sharedComplexSaveTool.writeAllSubFolderData(deskDatas);
    }


    
}

package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Map;

import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalAllData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool;
import hundun.tool.logic.util.ExternalExcelSaveTool;
import hundun.tool.logic.util.SimpleExternalJsonSaveTool;
import lombok.Getter;

public class ExternalResourceManager {
    private static final String BUILDER_ROOT_FOLDER = "ComikeHelper/builder/";
    private static final String USER_ROOT_FOLDER = "ComikeHelper/user/";
    private static final String SHARED_ROOT_FOLDER = "ComikeHelper/shared/";
    @Getter
    FileHandle defaultCoverFileHandle;
    ComikeHelperGame game;
    ComplexExternalJsonSaveTool sharedComplexSaveTool;
    //ExternalJsonSaveTool<ExternalMainData> userMainDataSaveTool;
    SimpleExternalJsonSaveTool<ExternalUserPrivateData> userPrivateDataSaveTool;
    ExternalExcelSaveTool sharedExcelSaveTool;

    public ExternalResourceManager(ComikeHelperGame game) {
        this.game = game;
        this.sharedComplexSaveTool = new ComplexExternalJsonSaveTool(
            SHARED_ROOT_FOLDER
            );
        //this.userMainDataSaveTool = new ExternalJsonSaveTool<>(USER_ROOT_FOLDER + "main.json", ExternalMainData.class);
        this.userPrivateDataSaveTool = new SimpleExternalJsonSaveTool<>(USER_ROOT_FOLDER, "private.json", ExternalUserPrivateData.class);
        this.sharedExcelSaveTool = new ExternalExcelSaveTool(BUILDER_ROOT_FOLDER);
    }


    public void providerExternalGameplayData(ExternalAllData masterMainData, ExternalUserPrivateData masterUserPrivateData) {
        

        merge(masterMainData,
                sharedComplexSaveTool.readMainData(),
                sharedComplexSaveTool.readAllSubFolderData()
                );
        merge(masterUserPrivateData, 
                userPrivateDataSaveTool.readRootSaveData()
                );
    }
    
    
    public boolean providerExcelGameplayData(ExternalAllData externalAllData, ExternalUserPrivateData userPrivateData) {

        
        sharedExcelSaveTool.lazyInitOnRuntime("test.xlsx");
        List<Map<Integer, String>> roomRawExcelData = sharedExcelSaveTool.readRootSaveData();
        ExternalAllData externalAllDataFromExcel = ExternalAllData.Factory.fromExcelData(game.getScreenContext().getLayoutConst(), roomRawExcelData, defaultCoverFileHandle);
        merge(externalAllData,
                externalAllDataFromExcel.getExternalMainData(),
                externalAllDataFromExcel.getDeskExternalRuntimeDataMap()
            );
        
        sharedExcelSaveTool.lazyInitOnRuntime("cart.xlsx");
        List<Map<Integer, String>> cartRawExcelData = sharedExcelSaveTool.readRootSaveData();
        
        
        boolean success = true;
        return success;
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
                       Map<String, ExternalDeskData> deskExternalRuntimeDataMap
    ) {
        if (other != null && other.getRoomSaveDataMap() != null) {
            master.getExternalMainData().getRoomSaveDataMap().putAll(other.getRoomSaveDataMap());
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

    public void saveAsSharedData(Map<String, ExternalDeskData> deskDatas) {
        sharedComplexSaveTool.writeAllSubFolderData(deskDatas);
    }


    public void lazyInitOnCreateStage1() {
        this.defaultCoverFileHandle = Gdx.files.internal("defaultCover.png");

        sharedComplexSaveTool.lazyInitOnGameCreate(defaultCoverFileHandle);
        userPrivateDataSaveTool.lazyInitOnGameCreate();
        sharedExcelSaveTool.lazyInitOnGameCreate();
    }


    
}

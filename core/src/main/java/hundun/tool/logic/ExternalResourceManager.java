package hundun.tool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.external.ExternalAllData;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.save.RootSaveData.GoodSaveData;
import hundun.tool.logic.util.ComplexExternalJsonSaveTool;
import hundun.tool.logic.util.ExternalExcelSaveTool;
import hundun.tool.logic.util.SimpleExternalJsonSaveTool;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    
    @AllArgsConstructor
    @Getter
    @Builder
    public static class DeskExcelTempData {
        String deskName;
        List<GoodSaveData> goods;
    }

    
    
    
    public boolean providerExcelGameplayData(ExternalAllData externalAllData, ExternalUserPrivateData userPrivateData) {

        
        Map<String, List<Map<Integer, String>>> roomTempDataMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            boolean exists = sharedExcelSaveTool.lazyInitOnRuntime("room_" + i + ".xlsx");
            if (!exists) {
                break;
            }
            List<Map<Integer, String>> roomRawExcelData = sharedExcelSaveTool.readRootSaveData();
            Map<Integer, String> firstLine = roomRawExcelData.remove(0);
            String roomName = firstLine.get(0);
            roomTempDataMap.put(roomName, roomRawExcelData);
        }
        
        sharedExcelSaveTool.lazyInitOnRuntime("desk.xlsx");
        List<Map<Integer, String>> deskGoodsRawExcelData = sharedExcelSaveTool.readRootSaveData();
        Map<String, DeskExcelTempData> deskGoodsMap = new HashMap<>();
        deskGoodsRawExcelData.forEach(line -> {
            String deskPos = line.get(0);
            String deskName = line.get(1);
            String goodName = line.get(2);
            if (!deskGoodsMap.containsKey(deskPos)) {
                deskGoodsMap.put(deskPos, DeskExcelTempData.builder()
                        .deskName(deskName)
                        .goods(new ArrayList<>())
                        .build());
            }
            deskGoodsMap.get(deskPos).getGoods().add(GoodSaveData.builder()
                    .name(goodName)
                    .build());
        });

        ExternalAllData externalAllDataFromExcel = ExternalAllData.Factory.fromExcelData(
                game.getScreenContext().getLayoutConst(), 
                roomTempDataMap, 
                deskGoodsMap,
                defaultCoverFileHandle
                );
        
        merge(externalAllData,
                externalAllDataFromExcel.getExternalMainData(),
                externalAllDataFromExcel.getDeskExternalRuntimeDataMap()
            );
        

        
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

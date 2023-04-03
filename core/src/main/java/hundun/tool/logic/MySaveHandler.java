package hundun.tool.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hundun.gdxgame.gamelib.base.IFrontend;
import hundun.gdxgame.gamelib.base.save.ISaveTool;
import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler;
import hundun.tool.logic.data.DeskRuntimeData;
import hundun.tool.logic.data.RootSaveData;
import hundun.tool.logic.data.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.RootSaveData.GoodSaveData;
import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
import hundun.tool.logic.data.RootSaveData.MySystemSettingSaveData;
import hundun.tool.logic.data.RootSaveData.RoomSaveData;


/**
 * @author hundun
 * Created on 2023/01/13
 */
public class MySaveHandler extends PairChildrenSaveHandler<RootSaveData, MySystemSettingSaveData, MyGameplaySaveData>{

    public MySaveHandler(IFrontend frontend, ISaveTool<RootSaveData> saveTool) {
        super(frontend, RootSaveData.Factory.INSTANCE, saveTool);
    }
    
    @Override
    protected RootSaveData genereateStarterRootSaveData() {
        
        List<DeskSaveData> deskSaveDatas = new ArrayList<>();
        
        DeskRuntimeData.AREA_LIST.forEach(area -> {
            
            IntStream.range(1, 10).forEach(it -> deskSaveDatas.add(
                    DeskSaveData.builder()
                            .name("name")
                            .posDataLine(area + ";" + it)
                            .goodSaveDatas(JavaFeatureForGwt.listOf(
                                    GoodSaveData.builder().name(area + "_" + it + "_" + "本子1").build(),
                                    GoodSaveData.builder().name(area + "_" + it + "_" + "本子2").build()
                                    ))
                            .build())
                    );
            
        });

        MyGameplaySaveData saveData = new MyGameplaySaveData();
        saveData.setCartGoodIds(JavaFeatureForGwt.listOf(
                "A_1_本子1",
                "B_1_本子1",
                "C_1_本子1",
                "A_2_本子1",
                "B_2_本子1",
                "C_2_本子1"
                ));
        saveData.setRoomSaveDatas(JavaFeatureForGwt.arraysAsList(
                RoomSaveData.builder()
                        .name("1号馆")
                        .startX(0)
                        .startY(0)
                        .deskSaveDatas(deskSaveDatas)
                        .build()
                ));
        return new RootSaveData(null, saveData);
    }

}

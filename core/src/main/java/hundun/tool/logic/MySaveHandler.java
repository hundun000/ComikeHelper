package hundun.tool.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import hundun.tool.logic.data.external.ExternalMainData;


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


        Map<String, DeskSaveData> deskSaveDatas = new HashMap<>();
        String room = "1号馆";
        DeskRuntimeData.AREA_LIST.forEach(area -> {

            IntStream.range(1, 5).forEach(it -> {
                String name = room + "-" + area + "-" + it;
                deskSaveDatas.put(
                    name,
                    DeskSaveData.builder()
                        .name(name)
                        .posDataLine(room + ";" + area + ";" + it)
                        .goodSaveDatas(JavaFeatureForGwt.listOf(
                            GoodSaveData.builder().name(area + "_" + it + "_" + "本子1").build(),
                            GoodSaveData.builder().name(area + "_" + it + "_" + "本子2").build()
                        ))
                        .build());
            });

        });

        MyGameplaySaveData saveData = new MyGameplaySaveData();
        saveData.setDefaultCartGoodIds(JavaFeatureForGwt.listOf(
                "A_1_本子1",
                "A_2_本子1",
                "A_3_本子1",
                "A_4_本子1"
                ));
        saveData.setDefaultDeskSaveDatas(deskSaveDatas);
        saveData.setDefaultExternalMainData(
                ExternalMainData.builder()
                    .roomSaveDataMap(JavaFeatureForGwt.mapOf(
                        room,
                        RoomSaveData.builder()
                            .name(room)
                            .startX(0)
                            .startY(0)
                            .roomWidth(5000)
                            .roomHeight(3000)
                            .build()
                        )
                    )
                    .build()
            );
        return new RootSaveData(new MySystemSettingSaveData(), saveData);
    }

}

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
        List<String> AREA_LIST = JavaFeatureForGwt.arraysAsList("A");

        AREA_LIST.forEach(area -> {

            IntStream.range(1, 5).forEach(it -> {
                String name = "Foo-" + area + "-" + it;
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

        String specialRoom = "2号馆";
        deskSaveDatas.put(
            "砍口垒同好组",
            DeskSaveData.builder()
                .name("砍口垒同好组")
                .posDataLine(specialRoom + ";特;0")
                .goodSaveDatas(JavaFeatureForGwt.listOf(
                    GoodSaveData.builder().name("砍口垒本子1").build(),
                    GoodSaveData.builder().name("砍口垒本子2").build()
                ))
                .build());
        deskSaveDatas.put(
            "少女前线同好组",
            DeskSaveData.builder()
                .name("少女前线同好组")
                .posDataLine(specialRoom + ";特;1")
                .goodSaveDatas(JavaFeatureForGwt.listOf(
                    GoodSaveData.builder().name("少女前线本子1").build(),
                    GoodSaveData.builder().name("少女前线本子2").build()
                ))
                .build());

        MyGameplaySaveData saveData = new MyGameplaySaveData();
        saveData.setDefaultCartGoodIds(JavaFeatureForGwt.listOf(
                "A_1_本子1",
                "A_2_本子1",
                "A_1_本子2",
                "A_2_本子2",
                "砍口垒本子1",
                "少女前线本子1"
                ));
        saveData.setDefaultDeskSaveDatas(deskSaveDatas);
        saveData.setDefaultExternalMainData(
                ExternalMainData.builder()
                    .roomSaveDataMap(JavaFeatureForGwt.mapOf(
                        room,
                        RoomSaveData.builder()
                            .name(room)
                            .roomWidth(5000)
                            .roomHeight(3000)
                            .build()
                        ,
                        specialRoom,
                        RoomSaveData.builder()
                                .name(specialRoom)
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

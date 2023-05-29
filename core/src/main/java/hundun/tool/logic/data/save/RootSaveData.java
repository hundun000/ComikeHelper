package hundun.tool.logic.data.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.gamelib.starter.save.IRootSaveExtension;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.save.RootSaveData.DeskSaveData;
import hundun.tool.logic.data.save.RootSaveData.GoodSaveData;
import hundun.tool.logic.data.save.RootSaveData.MyGameplaySaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/11/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootSaveData {
    MySystemSettingSaveData systemSettingSaveData;
    MyGameplaySaveData gameplaySaveData;

    @Data
    public static class MySystemSettingSaveData {

    }

    @Data
    public static class MyGameplaySaveData {
        ExternalMainData defaultExternalMainData;
        Map<String, DeskSaveData> defaultDeskSaveDatas;
        List<String> defaultCartGoodIds;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GoodSaveData {
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeskSaveData {
        String name;
        String posDataLine;
        List<GoodSaveData> goodSaveDatas;
    }
    
    

    public static final class Extension implements IRootSaveExtension<RootSaveData, MySystemSettingSaveData, MyGameplaySaveData> {

        public static final Extension INSTANCE = new Extension();
        
        public static MyGameplaySaveData genereateStarterGameplaySaveData() {
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
            return saveData;
        }

        @Override
        public MySystemSettingSaveData getSystemSave(RootSaveData rootSaveData) {
            return rootSaveData.getSystemSettingSaveData();
        }

        @Override
        public MyGameplaySaveData getGameplaySave(RootSaveData rootSaveData) {
            return rootSaveData.getGameplaySaveData();
        }

        @Override
        public RootSaveData newRootSave(MyGameplaySaveData gameplaySave, MySystemSettingSaveData systemSettingSave) {
            return new RootSaveData(systemSettingSave, gameplaySave);
        }

        @Override
        public MyGameplaySaveData newGameplaySave() {
            return new MyGameplaySaveData();
        }

        @Override
        public MySystemSettingSaveData newSystemSave() {
            return new MySystemSettingSaveData();
        }



    }
}

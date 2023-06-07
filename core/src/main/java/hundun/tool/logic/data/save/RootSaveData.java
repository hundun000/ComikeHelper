package hundun.tool.logic.data.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.gamelib.starter.save.IRootSaveExtension;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.external.ExternalMainData;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.external.ExternalUserPrivateData.GoodPrivateData;
import hundun.tool.logic.data.save.RootSaveData.MyGameplaySaveData;
import lombok.AllArgsConstructor;
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
        ExternalUserPrivateData defaultUserPrivateData;
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
                            .room(room)
                            .area(area)
                            .areaIndex(it)
                            .goodSaveDatas(JavaFeatureForGwt.listOf(
                                GoodSaveData.builder().name(area + "_" + it + "_" + "本子1").build(),
                                GoodSaveData.builder().name(area + "_" + it + "_" + "本子2").build()
                            ))
                            .build());
                });

            });

            final String specialRoom = "2号馆";
            final String specialGood1 = "砍口垒本子1";
            final String specialGood2 = "少女前线本子1";
            deskSaveDatas.put(
                "砍口垒同好组",
                DeskSaveData.builder()
                    .name("砍口垒同好组")
                    .room(specialRoom)
                    .area("特")
                    .areaIndex(0)
                    .x(100)
                    .y(100)
                    .goodSaveDatas(JavaFeatureForGwt.listOf(
                        GoodSaveData.builder().name(specialGood1).build(),
                        GoodSaveData.builder().name("砍口垒本子2").build()
                    ))
                    .build());
            deskSaveDatas.put(
                "少女前线同好组",
                DeskSaveData.builder()
                    .name("少女前线同好组")
                    .room(specialRoom)
                    .area("特")
                    .areaIndex(1)
                    .x(100)
                    .y(300)
                    .goodSaveDatas(JavaFeatureForGwt.listOf(
                        GoodSaveData.builder().name(specialGood2).build(),
                        GoodSaveData.builder().name("少女前线本子2").build()
                    ))
                    .build());

            MyGameplaySaveData saveData = new MyGameplaySaveData();
            
            Map<String, GoodPrivateData> goodPrivateDataMap = new HashMap<>();
            goodPrivateDataMap.put(specialGood1, 
                    GoodPrivateData.builder()
                            .tags(JavaFeatureForGwt.listOf(GoodRuntimeTag.IN_CART))
                            .build());
            goodPrivateDataMap.put(specialGood2, 
                    GoodPrivateData.builder()
                            .tags(JavaFeatureForGwt.listOf(GoodRuntimeTag.STAR))
                            .build());
            
            saveData.setDefaultUserPrivateData(ExternalUserPrivateData.builder()
                    .goodPrivateDataMap(goodPrivateDataMap)
                    .build()
                    );
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

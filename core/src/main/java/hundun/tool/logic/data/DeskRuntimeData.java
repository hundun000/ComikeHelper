package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.save.DeskSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2023/05/09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeskRuntimeData {

    String idName;
    String showName;
    DeskLocation location;
    List<GoodRuntimeData> goodSaveDatas;
    List<FileHandle> detailFileHandles;
    FileHandle coverFileHandle;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeskLocation {
        String room;
        String area;
        int areaIndex;
        Vector2 pos;
        
        public static class Companion {
            public static String SPLIT = ";";
            
            public static String toLine(String room, String area, int areaIndex) {
                return room + SPLIT + area + SPLIT + areaIndex;
            }

            public static String toLine(DeskSaveData deskSaveData) {
                return deskSaveData.getRoom() + SPLIT + deskSaveData.getArea() + SPLIT + deskSaveData.getAreaIndex();
            }
        }

        public static class Factory {

            
            public static DeskLocation fromLine(LayoutConst layoutConst, DeskSaveData deskSaveData) {
                return DeskLocation.builder()
                        .room(deskSaveData.getRoom())
                        .area(deskSaveData.getArea())
                        .areaIndex(deskSaveData.getAreaIndex())
                        .pos(new Vector2(deskSaveData.getX(), deskSaveData.getY()))
                        .build();
            }


            
        }
    }

    public static class Factory {
        public static DeskRuntimeData fromExternalRuntimeData(LayoutConst layoutConst, ExternalDeskData externalDeskData) {
            DeskSaveData deskSaveData = externalDeskData.getDeskSaveData();
            DeskRuntimeData result = DeskRuntimeData.builder()
                    .idName(deskSaveData.getIdName())
                    .showName(deskSaveData.getRealName() != null ? deskSaveData.getRealName() : "未知")
                    .location(DeskLocation.Factory.fromLine(layoutConst, deskSaveData))
                    .coverFileHandle(externalDeskData.getCoverFileHandle())
                    .detailFileHandles(externalDeskData.getImageFileHandles())
                    .build();
            result.setGoodSaveDatas(deskSaveData.getGoodSaveDatas().stream().map(it -> GoodRuntimeData.Factory.fromSaveData(result, it)).collect(Collectors.toList()));
            return result;
        }
        
    }

}

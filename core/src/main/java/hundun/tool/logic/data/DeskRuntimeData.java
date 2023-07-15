package hundun.tool.logic.data;

import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;

import hundun.tool.libgdx.screen.LayoutConst;
import hundun.tool.logic.data.external.ExternalDeskData;
import hundun.tool.logic.data.save.DeskSaveData;
import hundun.tool.logic.data.generic.GenericPosData;
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

    String uiName;
    GenericPosData mainLocation;
    List<GenericPosData> companionLocationList;
    List<GoodRuntimeData> goodSaveDatas;
    List<FileHandle> detailFileHandles;
    FileHandle coverFileHandle;

    public static class Factory {
        public static DeskRuntimeData fromExternalData(LayoutConst layoutConst, ExternalDeskData externalDeskData) {
            DeskSaveData deskSaveData = externalDeskData.getDeskSaveData();

            DeskRuntimeData mainResult = DeskRuntimeData.builder()
                    .uiName(deskSaveData.getRealName() != null ? deskSaveData.getRealName() : deskSaveData.getIdName())
                    .mainLocation(deskSaveData.getMainPos())
                    .companionLocationList(deskSaveData.getCompanionPosList())
                    .coverFileHandle(externalDeskData.getCoverFileHandle())
                    .detailFileHandles(externalDeskData.getImageFileHandles())
                    .build();
            mainResult.setGoodSaveDatas(deskSaveData.getGoodSaveDatas().stream().map(it ->
                    GoodRuntimeData.Factory.fromSaveData(mainResult, it)).collect(Collectors.toList())
            );


            return mainResult;
        }
        
    }

}

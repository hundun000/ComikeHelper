package hundun.tool.logic.data.external;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;

import hundun.tool.logic.data.save.DeskSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/31
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalDeskData {
    DeskSaveData deskSaveData;
    FileHandle coverFileHandle;
    List<FileHandle> imageFileHandles;

    
    
    public static class Factory {

        
        public static ExternalDeskData fromBasic(DeskSaveData deskSaveData, FileHandle coverFileHandle) {
            return ExternalDeskData.builder()
                    .deskSaveData(deskSaveData)
                    .coverFileHandle(coverFileHandle)
                    .imageFileHandles(new ArrayList<>(0))
                    .build();
        }
        
        public static ExternalDeskData fromFolder(FileHandle imageFolder, String key, FileHandle defaultCover) {
            ExternalDeskData externalDeskData = new ExternalDeskData();
            externalDeskData.setImageFileHandles(new ArrayList<>());
            if (imageFolder.exists()) {
                Arrays.stream(imageFolder.list()).forEach(it -> {
                    if (it.name().startsWith("cover.")) {
                        externalDeskData.setCoverFileHandle(it);
                    } else {
                        externalDeskData.getImageFileHandles().add(it);
                    }
                });
            }
            if (externalDeskData.getCoverFileHandle() == null) {
                externalDeskData.setCoverFileHandle(defaultCover);
            }
            return externalDeskData;
        }
    }
}
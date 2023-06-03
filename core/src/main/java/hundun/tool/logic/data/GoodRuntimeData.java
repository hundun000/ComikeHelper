package hundun.tool.logic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hundun.tool.ComikeHelperGame;
import hundun.tool.logic.data.DeskRuntimeData.DeskLocation;
import hundun.tool.logic.data.GoodRuntimeData.GoodRuntimeTag;
import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.save.GoodSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2023/05/09
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodRuntimeData {

    DeskRuntimeData ownerRef;
    String name;
    List<GoodRuntimeTag> tags;
    
    public enum GoodRuntimeTag {
        IN_CART,
        ;
    }
    
    public static class Factory {
        public static GoodRuntimeData fromSaveData(DeskRuntimeData ownerRef, GoodSaveData saveData) {
            return GoodRuntimeData.builder()
                    .ownerRef(ownerRef)
                    .name(saveData.getName())
                    .tags(new ArrayList<>())
                    .build();
        }
    }


    public void lazyInit(ExternalUserPrivateData userPrivateData) {

        if (userPrivateData.getCartGoodIds().contains(name)) {
            this.getTags().add(GoodRuntimeTag.IN_CART);
        }
    }
    
}

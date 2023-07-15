package hundun.tool.logic.data;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hundun.tool.logic.data.external.ExternalUserPrivateData;
import hundun.tool.logic.data.external.ExternalUserPrivateData.GoodPrivateData;
import hundun.tool.logic.data.save.GoodSaveData;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    Map<GoodRuntimeTag, Boolean> tagStateMap;
    
    public enum GoodRuntimeTag {
        IN_CART,
        STAR,
        WAITING,
        HEART,
        DONE,
        ;
    }
    
    public static class Factory {
        public static GoodRuntimeData fromSaveData(DeskRuntimeData ownerRef, GoodSaveData saveData) {
            Map<GoodRuntimeTag, Boolean> tagStateMap = Stream.of(GoodRuntimeTag.values()).collect(Collectors.toMap(
                    it -> it,
                    it -> false
            ));
            return GoodRuntimeData.builder()
                    .ownerRef(ownerRef)
                    .name(saveData.getName())
                    .tagStateMap(tagStateMap)
                    .build();
        }
    }


    public void lazyInit(ExternalUserPrivateData userPrivateData) {
        GoodPrivateData goodPrivateData = userPrivateData.getGoodPrivateDataMap().get(name);
        if (goodPrivateData != null) {
            goodPrivateData.getTags().forEach(it -> this.getTagStateMap().put(it, true));
        }
    }
    
}

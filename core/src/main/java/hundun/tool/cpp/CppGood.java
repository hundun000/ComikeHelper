package hundun.tool.cpp;

import java.util.List;

import lombok.Data;

@Data
public class CppGood {

    private long doujinshiID;
    private String name;
    private String yuanzhu;
    private String typeName;
    private String tag;
    private String authorID;
    private String authorNickName;
    private String picUrl;
    private List<CppEventInfo> cppEventInfo;
    private String authorPortraitUrl;
    private List<CppAuthor> cppAuthor;


}

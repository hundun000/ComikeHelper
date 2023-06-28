package hundun.tool.cpp;

import lombok.Data;

@Data
public class CppAuthor {

    private int doujinshiId;
    private int userId;
    private String facePicUrl;
    private String name;
    private String url;
    private int authorEnabled;
    private int likedCount;
    private int doujinshiCount;

}
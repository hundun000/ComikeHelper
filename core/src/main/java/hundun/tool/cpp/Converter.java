package hundun.tool.cpp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

    static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonRootBean cppDataConvert(String jsonText) throws JsonProcessingException {
        return objectMapper.readValue(jsonText, JsonRootBean.class);
    }

    public static void main(String[] args) throws JsonProcessingException {
        String input = "{\n" +
                "    \"result\": {\n" +
                "        \"pageCount\": 2,\n" +
                "        \"total\": 13,\n" +
                "        \"currentPage\": 1,\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"doujinshiID\": 475136,\n" +
                "                \"name\": \"[十字仙踪]七刀米羔-Maid Life 2\",\n" +
                "                \"yuanzhu\": \"原创\",\n" +
                "                \"typeName\": \"图集\",\n" +
                "                \"tag\": \"原创\",\n" +
                "                \"authorID\": \"1006\",\n" +
                "                \"authorNickName\": \"黄昏\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/4/2c2d9800-0d12-4c54-900f-0d20c6b711b4.png\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 475136,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"壹L01-04\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2020/5/187bf250-10d7-49dd-ba92-facafb5f7193.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1006,\n" +
                "                        \"facePicUrl\": \"/2020/5/187bf250-10d7-49dd-ba92-facafb5f7193.png\",\n" +
                "                        \"name\": \"黄昏\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 474324,\n" +
                "                \"name\": \"向山进发吧唧组\",\n" +
                "                \"yuanzhu\": \"向山进发\",\n" +
                "                \"typeName\": \"徽章\",\n" +
                "                \"tag\": \"向山进发\",\n" +
                "                \"authorID\": \"1206197\",\n" +
                "                \"authorNickName\": \"高橋羽音\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/4/8e622ed3-b80d-4072-ad52-6ec96ff65dab.png\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 474324,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": null,\n" +
                "                        \"eventEnabled\": null,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2021/5/86eba301-97fe-4262-a1b8-0130a99d4912.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1206197,\n" +
                "                        \"facePicUrl\": \"/2021/5/86eba301-97fe-4262-a1b8-0130a99d4912.png\",\n" +
                "                        \"name\": \"高橋羽音\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 466139,\n" +
                "                \"name\": \"纽带乐队的SNS观察日记\",\n" +
                "                \"yuanzhu\": \"孤独摇滚\",\n" +
                "                \"typeName\": \"漫画\",\n" +
                "                \"tag\": \"孤独摇滚\",\n" +
                "                \"authorID\": \"1075091\",\n" +
                "                \"authorNickName\": \"画纱\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/6/9d70ebc4-4b4a-4cfb-aa9e-17445958e556.jpeg\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 466139,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": null,\n" +
                "                        \"eventEnabled\": null,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2021/3/fde126d0-b22b-440b-905d-4a5a304fbd4d.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1075091,\n" +
                "                        \"facePicUrl\": \"/2023/4/23390935-d5a3-486c-ba9c-b8056b48943f.png\",\n" +
                "                        \"name\": \"画纱\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 464862,\n" +
                "                \"name\": \"向山进发手机包\",\n" +
                "                \"yuanzhu\": \"向山进发\",\n" +
                "                \"typeName\": \"其他\",\n" +
                "                \"tag\": \"向山进发\",\n" +
                "                \"authorID\": \"1202548\",\n" +
                "                \"authorNickName\": \"雨音\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/3/099e28ad-783b-4f3a-87d6-a24a27a1295e.jpeg\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 464862,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"壹I01-04\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2019/9/2b1373e5-c43b-42b2-9572-82964eba81f7.png\",\n" +
                "                \"authorList\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 455337,\n" +
                "                \"name\": \"雨音个人刊《星夜pro3》\",\n" +
                "                \"yuanzhu\": \"原创\",\n" +
                "                \"typeName\": \"图集\",\n" +
                "                \"tag\": \"seiyaproject|原创\",\n" +
                "                \"authorID\": \"1202548\",\n" +
                "                \"authorNickName\": \"雨音\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/3/99fb9db6-ad6d-437a-a0ea-d8b1fc669297.png\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 455337,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"壹I01-04\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2019/9/2b1373e5-c43b-42b2-9572-82964eba81f7.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1202548,\n" +
                "                        \"facePicUrl\": \"/2019/9/2b1373e5-c43b-42b2-9572-82964eba81f7.png\",\n" +
                "                        \"name\": \"雨音\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 454031,\n" +
                "                \"name\": \"罗德岛TIME2\",\n" +
                "                \"yuanzhu\": \"明日方舟\",\n" +
                "                \"typeName\": \"图集\",\n" +
                "                \"tag\": \"明日方舟|阿米娅|罗德岛|全员向\",\n" +
                "                \"authorID\": \"1332863\",\n" +
                "                \"authorNickName\": \"加热电褥子\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/4/937118c4-1511-447b-aa78-a3985abb9ee2.jpeg\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 454031,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"壹A65\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2020/7/71e76a3f-1498-4c03-9f5a-cbaf2cfdfdf4.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1332863,\n" +
                "                        \"facePicUrl\": \"/2020/7/71e76a3f-1498-4c03-9f5a-cbaf2cfdfdf4.png\",\n" +
                "                        \"name\": \"Ruzi\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 435668,\n" +
                "                \"name\": \"海馬記録 feat.ナースロボ_タイプＴ\",\n" +
                "                \"yuanzhu\": \"原创\",\n" +
                "                \"typeName\": \"CD\",\n" +
                "                \"tag\": \"原创\",\n" +
                "                \"authorID\": \"1456616\",\n" +
                "                \"authorNickName\": \"理一\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/4/50900c47-450a-4b1e-a0c8-a7e7b9698b25.png\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 435668,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"壹B42壹B43\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2021/3/27479e97-e60d-4605-9fed-cfcf10408c6b.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1456616,\n" +
                "                        \"facePicUrl\": \"/2021/3/27479e97-e60d-4605-9fed-cfcf10408c6b.png\",\n" +
                "                        \"name\": \"理一\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"doujinshiID\": 431297,\n" +
                "                \"name\": \"设计排版排雷手册\",\n" +
                "                \"yuanzhu\": \"原创\",\n" +
                "                \"typeName\": \"其他本子\",\n" +
                "                \"tag\": \"原创\",\n" +
                "                \"authorID\": \"100000\",\n" +
                "                \"authorNickName\": \"无差别同人站\",\n" +
                "                \"picUrl\": \"https://imagecdn3.allcpp.cn/upload/2023/2/c6469d94-7cd5-47e0-87a6-70783db6afdf.png\",\n" +
                "                \"eventInfoList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiID\": 431297,\n" +
                "                        \"eventID\": 1200,\n" +
                "                        \"eventName\": \"CP29 D1\",\n" +
                "                        \"eventDay\": 0,\n" +
                "                        \"posation\": \"\",\n" +
                "                        \"eventEnabled\": 0,\n" +
                "                        \"isDay\": null,\n" +
                "                        \"wannaBuyCount\": null\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"authorPortraitUrl\": \"https://imagecdn3.allcpp.cn/face/2020/3/62512dc0-4fa3-4708-8f86-845496592678.png\",\n" +
                "                \"authorList\": [\n" +
                "                    {\n" +
                "                        \"doujinshiId\": 0,\n" +
                "                        \"userId\": 1082872,\n" +
                "                        \"facePicUrl\": \"/2018/10/7/6340f87d-5bed-41cc-8441-bec60ff270bf.jpg\",\n" +
                "                        \"name\": \"理查理查\",\n" +
                "                        \"url\": \"\",\n" +
                "                        \"authorEnabled\": -1,\n" +
                "                        \"likedCount\": 0,\n" +
                "                        \"doujinshiCount\": 0\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"message\": \"\",\n" +
                "    \"isSuccess\": true\n" +
                "}";

        JsonRootBean jsonRootBean = objectMapper.readValue(input, JsonRootBean.class);
        System.out.println(objectMapper.writeValueAsString(jsonRootBean));
    }
}

package hundun.tool.cpp;

import java.util.List;

import lombok.Data;

@Data
public class Result {

    private int pageCount;
    private int total;
    private int currentPage;
    private List<CppGood> list;


}
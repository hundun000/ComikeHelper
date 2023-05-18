package hundun.tool.logic.util;

import java.util.List;
import java.util.Map;

public interface IComplexExternalHandler<T_MAIN, T_SUB> {
    void writeMainData(T_MAIN saveData);

    T_MAIN readMainData();

    void writeSubFolderData(String subFolderName, T_SUB saveData);

    T_SUB readSubFolderData(String subFolderName);

    Map<String, T_SUB> readAllSubFolderData();

    void writeAllSubFolderData(Map<String, T_SUB> saveDataMap);
}

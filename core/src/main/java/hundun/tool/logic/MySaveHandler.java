package hundun.tool.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hundun.gdxgame.gamelib.base.IFrontend;
import hundun.gdxgame.gamelib.base.save.ISaveTool;
import hundun.gdxgame.gamelib.starter.save.PairChildrenSaveHandler;
import hundun.tool.logic.data.DeskData;
import hundun.tool.logic.data.RootSaveData;
import hundun.tool.logic.data.RootSaveData.MyGameplaySaveData;
import hundun.tool.logic.data.RootSaveData.MySystemSettingSaveData;


/**
 * @author hundun
 * Created on 2023/01/13
 */
public class MySaveHandler extends PairChildrenSaveHandler<RootSaveData, MySystemSettingSaveData, MyGameplaySaveData>{

    public MySaveHandler(IFrontend frontend, ISaveTool<RootSaveData> saveTool) {
        super(frontend, RootSaveData.Factory.INSTANCE, saveTool);
    }
    
    @Override
    protected RootSaveData genereateStarterRootSaveData() {
        MyGameplaySaveData saveData = new MyGameplaySaveData();
        saveData.setDeskDatas(new ArrayList<>()
                );
        return new RootSaveData(null, saveData);
    }

}

package hundun.tool.android;

import hundun.gdxgame.gamelib.base.save.ISaveTool;
import hundun.tool.logic.data.RootSaveData;

public class AndroidSaveTool implements ISaveTool<RootSaveData> {
    @Override
    public void lazyInitOnGameCreate() {

    }

    @Override
    public boolean hasRootSave() {
        return false;
    }

    @Override
    public void writeRootSaveData(RootSaveData saveData) {

    }

    @Override
    public RootSaveData readRootSaveData() {
        return null;
    }
}

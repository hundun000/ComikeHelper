package hundun.tool.libgdx.screen.builder;

import hundun.tool.libgdx.screen.BuilderScreen;
import hundun.tool.libgdx.screen.shared.BasePageableTable;
import hundun.tool.libgdx.screen.shared.RoomSwitchPageVM;

public class BuilderMainBoardVM extends BasePageableTable {

    BuilderScreen builderScreen;

    AllButtonPageVM allButtonPageVM;
    RoomSwitchPageVM roomSwitchPageVM;

    private enum BuilderMainBoardState {
        PAGE1,
        PAGE2
        ;
    }

    public BuilderMainBoardVM(BuilderScreen screen) {
        super(screen);
        init("builder", screen.getGame());

        this.builderScreen = screen;

        this.allButtonPageVM = new AllButtonPageVM(screen);
        this.roomSwitchPageVM = new RoomSwitchPageVM(screen);

        addPage(BuilderMainBoardState.PAGE1.name(),
                "读写数据",
                allButtonPageVM
        );
        addPage(BuilderMainBoardState.PAGE2.name(),
                "切换展馆",
                roomSwitchPageVM
        );
    }

    public void updateForShow() {

        roomSwitchPageVM.updateData();

        updateByState(BuilderMainBoardState.PAGE1.name());
    }

}

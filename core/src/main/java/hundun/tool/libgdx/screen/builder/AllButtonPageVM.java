package hundun.tool.libgdx.screen.builder;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fasterxml.jackson.core.JsonProcessingException;

import hundun.tool.cpp.Converter;
import hundun.tool.cpp.JsonRootBean;
import hundun.tool.libgdx.screen.BuilderScreen;
import hundun.tool.logic.ExternalResourceManager.MergeWorkInProgressModel;

public class AllButtonPageVM extends Table {

    BuilderScreen screen;

    public AllButtonPageVM(BuilderScreen screen) {
        this.screen = screen;

        TextButton loadCppButton = new TextButton("append cpp", screen.getGame().getMainSkin());
        loadCppButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.startInputBox("cpp", str -> {
                    JsonRootBean cppBean = null;
                    try {
                        cppBean = Converter.cppDataConvert(str);
                        MergeWorkInProgressModel model = screen.getGame().getLogicContext().appendCppBean(cppBean);
                        screen.startDialog(model, "cpp merge WIP");
                        return null;
                    } catch (JsonProcessingException e) {
                        return e.getMessage();
                    }


                });
            }
        });
        this.add(loadCppButton).row();

        TextButton loadExcelButton = new TextButton("append excel", screen.getGame().getMainSkin());
        loadExcelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MergeWorkInProgressModel model = screen.getGame().getLogicContext().appendExcelData();
                screen.startDialog(model, "excel merge WIP");
            }
        });
        this.add(loadExcelButton).row();

        TextButton appendDefaultButton = new TextButton("append default", screen.getGame().getMainSkin());
        appendDefaultButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MergeWorkInProgressModel model = screen.getGame().getLogicContext().appendDefaultData();
                screen.startDialog(model, "default merge WIP");
            }
        });
        this.add(appendDefaultButton).row();

        TextButton appendSaveButton = new TextButton("append save", screen.getGame().getMainSkin());
        appendSaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MergeWorkInProgressModel model = screen.getGame().getLogicContext().appendSaveData();
                screen.startDialog(model, "save merge WIP");
            }
        });
        this.add(appendSaveButton).row();

        TextButton saveButton = new TextButton("save", screen.getGame().getMainSkin());
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.getGame().getLogicContext().saveCurrentSharedData();
                screen.getGame().getLogicContext().calculateAndSaveCurrentUserData();
                screen.startDialog(
                        "保存完成",
                        "save done",
                        null
                );
            }
        });
        this.add(saveButton).row();

        TextButton previewAndDeleteUnknownDesksButton = new TextButton("清理", screen.getGame().getMainSkin());
        previewAndDeleteUnknownDesksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screen.startDialog(
                        "以下数据将被删除：" + screen.getGame().getLogicContext().previewAllUnknownDesks().toString(),
                        "delete WIP",
                        () -> screen.getGame().getLogicContext().deleteAllUnknownDesks()
                        );
            }
        });
        this.add(previewAndDeleteUnknownDesksButton).row();
    }
}

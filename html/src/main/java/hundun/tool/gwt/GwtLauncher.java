package hundun.tool.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.FreetypeInjector;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.inject.OnCompletion;

import hundun.tool.ComikeHelperGame;



/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    
private final ComikeHelperGame game;
    
    public GwtLauncher() {
        game = new ComikeHelperGame(new GwtPreferencesSaveTool("ComikeHelperGame-html-save"));
    }
    
    @Override
    public GwtApplicationConfiguration getConfig () {
        // Resizable application, uses available space in browser with no padding:
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
        cfg.padVertical = 0;
        cfg.padHorizontal = 0;
        return cfg;
        // If you want a fixed size application, comment out the above resizable section,
        // and uncomment below:
        //return new GwtApplicationConfiguration(640, 480);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return game;
    }
    
    @Override
    public void onModuleLoad () {
        FreetypeInjector.inject(new OnCompletion() {
            public void run () {
                // Replace HtmlLauncher with the class name
                // If your class is called FooBar.java than the line should be FooBar.super.onModuleLoad();
                GwtLauncher.super.onModuleLoad();
            }
        });
    }
}

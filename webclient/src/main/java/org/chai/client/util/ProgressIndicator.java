package org.chai.client.util;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Image;

public class ProgressIndicator extends Window {
    private static ProgressIndicator ref;
    public static boolean isBarVisible = false;

    private ProgressIndicator() { }
    
    public static void showProgressBar() {
        ProgressIndicator win = getProgressBar();
        isBarVisible = true;
        win.show();
        int ver = (com.google.gwt.user.client.Window.getClientHeight() / 2) - 32;
        int hor = (com.google.gwt.user.client.Window.getClientWidth() / 2) - 32;
        ref.setPosition(hor, ver);
    }
    
    /**
     * Hides the progress bar
     */
    public static void hideProgressBar() {
        if (ref != null) {
            ref.hide();
            isBarVisible = false;
        }
    }

    /**
     * Gets a reference to the ProgressIndicator,
     * and creates it if it is not already initialised
     * @return ProgressIndicator
     */
    private static ProgressIndicator getProgressBar()
    {
      if (ref == null) {
          ref = new ProgressIndicator();
          ref.setResizable(false);
          ref.setSize(32, 32);
          ref.setPlain(true);
          ref.setBodyBorder(false);
          ref.setBorders(false);
          ref.setShadow(false);
          ref.setFrame(false);
          ref.setHeaderVisible(false);
          // create the content panel which contains the progress stuff
          ContentPanel cp = new ContentPanel();
          cp.setLayout(new FitLayout());
          cp.setHeaderVisible(false);
          Image img = new Image("gxt/images/wait30trans.gif");
          cp.setWidth("32");
          cp.setHeight("32");
          cp.add(img);
      	  ref.add(cp);
      }
      
      return ref;
    }
}

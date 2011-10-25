package xiatian.pim.component.dialog;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class ScreenInfo {

  public static int getScreenID( JFrame jf ) {
    int scrID = 1;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gd = ge.getScreenDevices();
    for (int i = 0; i < gd.length; i++) {
      GraphicsConfiguration gc = gd[i].getDefaultConfiguration();
      Rectangle r = gc.getBounds();
      if (r.contains(jf.getLocation())) {
        scrID = i+1;
      }
    }
    return scrID;
  }

  public static Dimension getScreenDimension( int scrID ) {
    Dimension d = new Dimension(0, 0);
    if (scrID > 0) {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      DisplayMode mode = ge.getScreenDevices()[scrID - 1].getDisplayMode();
      d.setSize(mode.getWidth(), mode.getHeight());
    }
    return d;
  }

  public static int getScreenWidth( int scrID ) {
    Dimension d = getScreenDimension(scrID);
    return d.width;
  }

  public static int getScreenHeight( int scrID ) {
    Dimension d = getScreenDimension(scrID);
    return d.height;
  }

}

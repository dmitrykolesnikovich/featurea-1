package featurea.builder.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class TrayIconHelper {

  private final TrayIcon trayIcon;
  private final SystemTray tray;

  public TrayIconHelper(final JFrame frame) {
    trayIcon = new TrayIcon(createImage("gen.png", "gen"));
    trayIcon.setImageAutoSize(true);
    trayIcon.setToolTip("Builder");
    tray = SystemTray.getSystemTray();

    trayIcon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frame.setVisible(true);
        frame.setState(Frame.NORMAL);
        frame.requestFocus();
        frame.getContentPane().requestFocus();
        hide();
      }
    });
  }

  public void create() {
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
    }
    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      System.out.println("TrayIcon could not be added.");
      return;
    }
  }

  public void hide() {
    tray.remove(trayIcon);
  }

  private static Image createImage(String path, String description) {
    URL imageURL = TrayIconHelper.class.getResource(path);

    if (imageURL == null) {
      System.err.println("XmlTag not found: " + path);
      return null;
    } else {
      return (new ImageIcon(imageURL, description)).getImage();
    }
  }


}

package featurea.desktop;

import featurea.app.MediaPlayer;
import featurea.util.FileUtil;
import featurea.util.Preferences;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.jar.JarFile;

public class FeatureaMenuBar extends JMenuBar {

  private MediaPlayer mediaPlayer;
  private final JCheckBoxMenuItem screenModeMenuItem = new JCheckBoxMenuItem() {
    @Override
    public void setSelected(boolean b) {
      super.setSelected(b);
    }
  };
  private final JCheckBoxMenuItem outlineCheckBoxMenuItem = new JCheckBoxMenuItem();
  private final Preferences preferences = new Preferences(new File(LwjglNatives.PREFERENCES_PATH + "/featurea.properties"));

  public FeatureaMenuBar(final SingleInstanceServer.Program program) {
    JMenu fileMenu = new JMenu("File");
    add(fileMenu);

    /*add menu items*/
    setupMenuItem(fileMenu, "Open File", "Open File", KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String extension = "xml";
        JFileChooser fileChooser = new JFileChooser();
        File lastFile = getLastFile();
        if (lastFile != null) {
          fileChooser.setCurrentDirectory(lastFile.getParentFile());
        }
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("*." + extension, extension));
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File[] selectedFiles = fileChooser.getSelectedFiles();
          Arrays.sort(selectedFiles);
          for (File selectedFile : selectedFiles) {
            program.openFile(selectedFile);
            preferences.put("lastFile", FileUtil.formatPath(selectedFile.getAbsolutePath()));
            preferences.save();
          }
        }
      }
    }, new JMenuItem());

    setupMenuItem(fileMenu, "Reload", "Reload", KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            mediaPlayer.stop();
            onProjectSwitch(mediaPlayer);
          }
        });
      }
    }, new JMenuItem());

    setupMenuItem(fileMenu, "Invalidate Textures", "Invalidate Textures", KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.render.invalidateTextures();
      }
    }, new JMenuItem());

    setupMenuItem(fileMenu, "Outline", "Outline", KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.render.isOutline = outlineCheckBoxMenuItem.isSelected();
      }
    }, outlineCheckBoxMenuItem);

    setupMenuItem(fileMenu, "Screen Mode", "Screen Mode", KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.render.isScreenMode = screenModeMenuItem.isSelected();
      }
    }, screenModeMenuItem);

    setupMenuItem(fileMenu, "Build", "Build", KeyStroke.getKeyStroke(KeyEvent.VK_F10, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mediaPlayer.project.build(false);
      }
    }, new JMenuItem());
  }

  private void setupMenuItem(JMenu menu, String text, String toolTipText, KeyStroke keyStroke, ActionListener actionListener, JMenuItem menuItem) {
    menuItem.setText(text);
    menuItem.setToolTipText(toolTipText);
    menuItem.setAccelerator(keyStroke);
    menuItem.addActionListener(actionListener);
    menu.add(menuItem);
  }


  public File getLastFile() {
    String lastFile = preferences.getValue("lastFile");
    if (lastFile != null) {
      return new File(lastFile);
    } else {
      JarFile jarFile = FileUtil.retrieveJarFile(Simulator.class);
      if (jarFile != null) {
        lastFile = jarFile.getName();
        return new File(lastFile);
      }
      return null;
    }
  }

  public void onProjectSwitch(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    screenModeMenuItem.setSelected(mediaPlayer.render.isScreenMode);
    outlineCheckBoxMenuItem.setSelected(mediaPlayer.render.isOutline);
  }

}

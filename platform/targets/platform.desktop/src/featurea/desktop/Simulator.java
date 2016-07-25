package featurea.desktop;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.app.Screen;
import featurea.opengl.OpenGLManager;
import featurea.util.FileUtil;
import featurea.util.JarUtils;
import featurea.util.Size;
import featurea.xml.XmlContext;
import featurea.xml.XmlEditor;
import org.lwjgl.LWJGLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.jar.JarFile;

public class Simulator extends JFrame {

  static {
    initOnlyOnce();
  }

  private File xmlFile;
  public XmlContext xmlContext;
  private static boolean isInit;
  public static int instanceCount;
  private LwjglWindow window;
  private File manifestFile;
  private boolean isProduction;

  public Simulator() throws HeadlessException {
    /*setJMenuBar(new FeatureaMenuBar(this));*/
  }

  public void setManifestFile(File manifestFile) {
    this.manifestFile = manifestFile;
  }

  public boolean isProduction() {
    return isProduction;
  }

  public void setProduction(boolean isProduction) {
    this.isProduction = isProduction;
  }

  @Override
  public void setSize(int width, int height) {
    getContentPane().setPreferredSize(new Dimension(width, height));
  }

  public void setSize(Size size) {
    setSize((int) size.width, (int) size.height);
  }

  public void start() {
    createWindow();

    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    requestFocus();
    getContentPane().requestFocus();
    getWindow().requestFocus();
  }

  public void createWindow() {
    try {
      window = new LwjglWindow(this, manifestFile, isProduction);
    } catch (LWJGLException e) {
      e.printStackTrace();
    }
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent ev) {
        window.close();
        instanceCount--;
        if (instanceCount == 0) {
          window.exit();
        }
      }
    });
    getContentPane().add(window);
  }

  public LwjglWindow getWindow() {
    return window;
  }

  /*lifecycle callbacks: onCreateFeatureaContext -> onTick, onDraw -> onExit*/

  public void onCreate(MediaPlayer mediaPlayer) {
    JarFile jarFile = FileUtil.retrieveJarFile(Simulator.class);
    if (jarFile != null) {
      mediaPlayer.getFiles().add(jarFile);
      mediaPlayer.getClassLoader().add(jarFile);
    }
    mediaPlayer.app.onCreate();
    mediaPlayer.app.onResume();
    XmlEditor editor = mediaPlayer.getResources().editor;
    if (editor != null) {
      editor.reload();
    } else {
      reload();
    }
  }

  public void onTick(double elapsedTime) {
    getMediaPlayer().app.onTick(elapsedTime);
  }

  public void onDrawBackground() {
    getMediaPlayer().app.onDrawBackground();
  }

  public void onDraw(OpenGLManager gl) {
    getMediaPlayer().app.onDraw();
    getMediaPlayer().render.unbind();
  }

  public void onExit() {
    getMediaPlayer().app.onPause();
    getMediaPlayer().app.onDestroy();
  }

  public void onInput() {
    getMediaPlayer().app.onInput();
  }

  /*private API*/

  public static void initOnlyOnce() {
    if (!isInit) {
      isInit = true;
      new LwjglNatives().register();
      Context.gl = new OpenGLImpl();
      Context.al = new OpenALImpl();
    }
  }

  public MediaPlayer getMediaPlayer() {
    return window.mediaPlayer;
  }

  public void setXmlFile(File xmlFile) {
    this.xmlFile = xmlFile;
    setManifestFile(new File(FileUtil.formatPath(FileUtil.retrieveProjectFileByXmlFile(xmlFile).getAbsolutePath())));
  }

  public File getXmlFile() {
    return xmlFile;
  }

  protected String getFileResources() {
    if (getMediaPlayer().isProduction()) {
      return "";
    } else {
      return null;
    }
  }

  /**/

  public static void startNewProcess(boolean isProduction, File xmlFile) {
    try {
      String s = FileUtil.formatPath("${FEATUREA_HOME}/tools/simulator.jar");
      File simulatorJarFile = new File(s);
      JarFile jarFile = new JarFile(simulatorJarFile.getAbsolutePath());
      JarUtils.execute(jarFile, isProduction + "", FileUtil.formatPath(xmlFile.getAbsolutePath()));
      /*main(new String[]{isProduction + "", FileUtil.formatPath(xmlFile.getAbsolutePath())});*/
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    boolean isProduction = Boolean.parseBoolean(args[0]);
    File xmlFile = new File(args[1]);

    Simulator.startSimulator(isProduction, xmlFile);
  }

  public static void startSimulator(boolean isProduction, File xmlFile) {
    Simulator simulator = new Simulator();
    simulator.setProduction(isProduction);
    simulator.setXmlFile(xmlFile);

    // >>
    SimulatorSettings settings = simulator.getSettings();
    simulator.setSize(settings.getWidth(), settings.getHeight());
    simulator.setTitle(settings.getTitle());
    // <<

    simulator.start();
  }

  public SimulatorSettings getSettings() {
    return new SimulatorSettings(getMediaPlayer().project);
  }

  // hot fix todo improve
  public void onDoubleClick() {
    // no op
  }

  public void reload() {
    if (xmlFile != null) {
      xmlContext = getMediaPlayer().getResources().getContext(xmlFile);
      if (xmlContext.xmlTag != null) {
        Object resource = xmlContext.xmlTag.getResource();
        if (resource instanceof Screen) {
          getMediaPlayer().app.screen = (Screen) resource;
        } else if (resource instanceof Layer) {
          Layer layer = (Layer) resource;
          String screenId = getMediaPlayer().project.settings.getScreenId();
          if (screenId != null) {
            getMediaPlayer().app.screen = getMediaPlayer().getResources().getResource(screenId);
          } else {
            getMediaPlayer().app.screen = new Screen();
          }
          getMediaPlayer().app.screen.add(layer);
        }
      }
    }
  }

}

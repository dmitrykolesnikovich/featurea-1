package featurea.xmlEditor;

import featurea.desktop.Simulator;
import featurea.desktop.SingleInstanceServer;
import featurea.swing.FeatureaSwingUtil;
import featurea.util.FileUtil;
import featurea.util.JarUtils;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class FeatureaSingleInstanceServer extends SingleInstanceServer {

  public static FeatureaSingleInstanceServer instance;

  public FeatureaSingleInstanceServer(File... files) throws IOException {
    super(9999, files);
  }

  public static void startNewProcess(boolean isProduction, File xmlFile) {
    try {
      JarFile jarFile = FileUtil.retrieveJarFile(FeatureaSingleInstanceServer.class);
      if (jarFile == null) {
        jarFile = new JarFile(FileUtil.formatPath("${FEATUREA_HOME}/tools/Featurea.jar"));
      }
      try {
        JarUtils.execute(jarFile, "--simulator", isProduction + "", FileUtil.formatPath(xmlFile.getAbsolutePath()));
      } catch (Throwable e) {
        e.printStackTrace();
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    System.setProperty("featurea.editMode", "true");
    System.setProperty("featurea.platform.prefix", "featurea");
    if (args.length == 3 && "--simulator".equals(args[0])) {
      Simulator.startSimulator(Boolean.valueOf(args[1]), new File(args[2]));
    } else {
      FeatureaSwingUtil.setupTheme();
      File[] files = new File[args.length];
      for (int i = 0; i < args.length; i++) {
        files[i] = new File(args[i]);
      }
      try {
        instance = new FeatureaSingleInstanceServer(files);
      } catch (IOException e) {
        System.err.println("Another instance of this application is already running.  Exiting.");
        System.exit(0);
      }
    }
  }

  @Override
  public Program newProgram() {
    return new FeatureaWindow();
  }

}

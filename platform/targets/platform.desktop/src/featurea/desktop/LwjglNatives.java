package featurea.desktop;

import featurea.app.Context;
import featurea.util.FileUtil;
import featurea.util.Targets;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class LwjglNatives {

  public static final String FEATUREA_DIR = System.getProperty("user.home") + "/.featurea";
  public static final String NATIVES_PATH = FEATUREA_DIR + "/natives";
  public static final String PREFERENCES_PATH = FEATUREA_DIR + "/preferences";

  private static final String[] LIBS = new String[]{
      "windows/jinput-dx8.dll",
      "windows/jinput-dx8_64.dll",
      "windows/jinput-raw.dll",
      "windows/jinput-raw_64.dll",
      "windows/lwjgl.dll",
      "windows/lwjgl64.dll",
      "windows/OpenAL32.dll",
      "windows/OpenAL64.dll",
      "macosx/libjinput-osx.jnilib",
      "macosx/liblwjgl.jnilib",
      "macosx/openal.dylib",
      "linux/libjinput-linux.so",
      "linux/libjinput-linux64.so",
      "linux/liblwjgl.so",
      "linux/liblwjgl64.so",
      "linux/libopenal.so",
      "linux/libopenal64.so",
  };

  private void install() {
    System.setProperty("featurea.preferences", PREFERENCES_PATH);
    if (!new File(NATIVES_PATH).exists()) {
      JarFile jarFile = FileUtil.retrieveJarFile(Context.class);
      if (jarFile != null) {
        for (String lib : LIBS) {
          try {
            ZipEntry entry = jarFile.getEntry(lib);
            FileUtil.copy(jarFile.getInputStream(entry), new File(NATIVES_PATH + "/" + lib));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  public void register() {
    install();
    setLibraryPath(NATIVES_PATH + "/" + Targets.name);
  }

  private void setLibraryPath(String libraryPath) {
    System.setProperty("org.lwjgl.librarypath", libraryPath);
    try {
      Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
      usrPathsField.setAccessible(true);
      String[] paths = (String[]) usrPathsField.get(null);
      for (String path : paths) {
        if (path.equals(path)) {
          return;
        }
      }
      String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
      newPaths[newPaths.length - 1] = libraryPath;
      usrPathsField.set(null, newPaths);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}



package featurea.util;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

public class JarUtils {

  private JarUtils() {
    // no op
  }

  public static void execute(JarFile jarFile, String... args) throws IOException, InterruptedException {
    List<String> list = new java.util.ArrayList<>();
    list.add("java");
    if (jarFile != null) {
      String jarFilePath = FileUtil.formatPath(jarFile.getName());
      list.add("-jar");
      list.add(jarFilePath);
    }
    for (String arg : args) {
      list.add(arg);
    }
    Runtime.getRuntime().exec(list.toArray(new String[list.size()]));
  }

}

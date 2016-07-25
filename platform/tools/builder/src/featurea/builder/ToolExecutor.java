package featurea.builder;

import featurea.app.Project;
import featurea.util.ArrayUtil;
import featurea.util.JarUtils;
import featurea.util.Properties;
import featurea.util.StringUtil;

import java.io.File;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * Example how to use: write in the build.properties file property
 * <p/>
 * tools=tools/genSprites/bin/genSprites.jar
 * <p/>
 * and ToolExecutor will execute ${projectPath}/tools/genSprites/bin/genSprites.jar with one parameter ${projectPath}
 */
public final class ToolExecutor {

  private final Project project;

  public ToolExecutor(Project project) {
    this.project = project;
  }

  public void performGen() {
    Properties tools = project.toolsProperties;
    for (Map.Entry<String, String> entry : tools.entrySet()) {
      String key = entry.getKey();
      if (key.equals("config")) {
        continue;
      }
      File file = project.findFile(key);
      String[] args = StringUtil.split(entry.getValue(), ",");
      args = ArrayUtil.add(args, project.file.getAbsolutePath(), 0);
      try {
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        JarUtils.execute(jarFile, args);
      } catch (Throwable e1) {
        e1.printStackTrace();
      }
    }
  }

}

package featurea.builder.java;

import featurea.app.Project;
import featurea.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ConfigToJavaTool {

  private final Project project;
  private final Files config;

  public ConfigToJavaTool(Project project) {
    this.project = project;
    this.config = new Files().add(project.configurationDir);
  }

  public void performGen() {
    String[] filePaths = StringUtil.split(project.toolsProperties.getValue("config"), ",");
    String configPackage = project.pakage + ".config";
    for (String filePath : filePaths) {
      try {
        FileInputStream test = new FileInputStream(new File("E:/featurea/engines/platformer/config/featurea/platformer/engine.properties"));
        test.available();
      } catch (Throwable e) {
        e.printStackTrace();
      }

      InputStream inputStream = config.getStream(filePath);
      String javaFileName = StringUtil.upperCaseLastLetterAfterDelimiter(filePath.replaceAll("\\.properties", ".java"), '/');
      String dir = FileUtil.getDir(javaFileName);
      String name = FileUtil.getName(javaFileName);
      String resultName = dir + "/config/" + name;
      Properties properties = new Properties(inputStream);
      String result = "package " + configPackage + ";" + ConfigUtil.toJava(properties, filePath);
      FileUtil.write(result, new File(project.generatedFilesDir, resultName));
    }
  }

}

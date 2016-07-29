package featurea.builder;

import featurea.app.Project;
import featurea.util.FileUtil;

import java.io.File;
import java.util.Map;

public class ManifestGenTool {

  private final Project project;

  public ManifestGenTool(Project project) {
    this.project = project;
  }

  public void performGen() {
    File file = new File(project.generatedFilesDir, Project.PROJECT_FILE_NAME);
    String result = getCode();
    FileUtil.write(result, file);
  }

  private String getCode() {
    String result = "<project package=\"" + project.pakage + "\">";

    // dependencies
    result += "<dependencies>";
    for (String pakage : project.getPackages()) {
      if (!project.pakage.equals(pakage)) {
        result += "<file path=\"" + pakage.replaceAll("\\.", "/") + "\"/>";
      }
    }
    result += "</dependencies>";

    // packs
    result += "<packs>";
    for (Map.Entry<String, String> entry : project.packProperties.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      result += "<pack name=\"" + key + "\" files=\"" + value + "\"/>";
    }
    result += "</packs>";

    result += "</project>";
    return result;
  }

}

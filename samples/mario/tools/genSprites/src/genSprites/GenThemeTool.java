package genSprites;

import featurea.app.Project;
import featurea.util.FileUtil;

import java.io.File;
import java.util.List;

public class GenThemeTool {

  private final Project project;

  private GenThemeTool(Project project) {
    this.project = project;
  }

  public static void gen(Project project, List<String> enumConstants) {
    new GenThemeTool(project).performGen(enumConstants);
  }

  private void performGen(List<String> enumConstants) {
    String code = getCode(enumConstants);
    File javaFile = new File(project.generatedFilesDir, project.pakage + "/Theme.java");
    FileUtil.write(code, javaFile);
  }

  private String getCode(List<String> enumConstants) {
    StringBuilder builder = new StringBuilder();
    builder.append("package " + project.pakage + "; public enum Theme {");
    for (String enumConstant : enumConstants) {
      builder.append(enumConstant + ",");
    }
    builder.append("}");
    return builder.toString();
  }

}

package genSprites;

import featurea.app.Project;
import featurea.util.FileUtil;
import featurea.util.StringUtil;

import java.io.File;
import java.util.List;

public final class WorldsCode {

  private final Project project;

  private WorldsCode(Project project) {
    this.project = project;
  }

  public static void gen(Project project, List<String> list) {
    new WorldsCode(project).performGen();
  }

  private void performGen() {
    StringBuilder builder = new StringBuilder();
    builder.append("package mario;");
    builder.append("import mario.objects.hero.World;");
    builder.append("import featurea.app.Context;");
    builder.append("public class Worlds { ");
    builder.append("private static int index; public static World current() { return getWorld(index); } public static World next(int delta) { index+=delta; return getWorld(index); }");
    builder.append("public static World getWorld(int index) { switch (index) {");
    List<String> levels = project.getFiles().getChildren("levels", ".xml");
    for (int i = 0; i < levels.size(); i++) {
      String file = levels.get(i);
      String fileName = FileUtil.getName(file);
      fileName = fileName.replaceAll(".xml", "");
      builder.append("case " + i + ": {" +
          "   return levels.res." + StringUtil.uppercaseFirstLetter(fileName) + ".value();" +
          " }");
    }
    builder.append(" } return null; }");
    builder.append(generateGetWorldByMarker(project));
    builder.append("public static int getIndex() { return index; }");
    builder.append("}");
    FileUtil.write(builder.toString(), new File(project.generatedFilesDir, project.pakage + "/Worlds.java"));
  }

  private String generateGetWorldByMarker(Project project) {
    String result = "";
    result = "public static World getBonusWorld(String marker) { if (!Context.isProduction()) {" +
        "      return Context.getResources().getResource(\"/bonusLevels/\" + marker);" +
        "    } else {" +
        "      switch (marker) {";

    for (String filePath : project.getFiles().getChildren("bonusLevels", ".xml")) {
      result += caseCode(filePath);
    }
    for (String filePath : project.getFiles().getChildren("misc", ".xml")) {
      result += caseCode(filePath);
    }

    result += "} throw new IllegalArgumentException(\"marker = \" + marker); }}";
    return result;
  }

  private String caseCode(String filePath) {
    String fileName = FileUtil.getName(filePath);
    String dir = FileUtil.getDir(filePath).replaceAll("/", ".");
    fileName = fileName.replaceAll(".xml", "");
    return "case\"" + fileName + "\":" + " return " + dir + ".res." + StringUtil.uppercaseFirstLetter(fileName) + ".value();";
  }

}

package genSprites;

import featurea.app.Project;
import featurea.util.FileUtil;

import java.io.File;

public class WorldFireworkCode {

  private final Project project;

  public WorldFireworkCode(Project project) {
    this.project = project;
  }

  public static void gen(Project project) {
    new WorldFireworkCode(project).performGen();
  }

  private void performGen() {
    StringBuilder builder = new StringBuilder();
    builder.append("package mario; public class WorldFirework { public static double[] getValue() { switch (Worlds.getIndex()) {");
    int iCount = 8;
    int jCount = 4;
    for (int i = 0; i < iCount; i++) {
      for (int j = 0; j < jCount; j++) {
        builder.append(
            "case " + (i * jCount + j) + ": {" +
                "  return mario.config.Firework.world" + (i + 1) + "_" + (j + 1) + ";" +
                "}");
      }
    }
    builder.append("} return null; } }");
    FileUtil.write(builder.toString(), new File(project.generatedFilesDir, project.pakage + "/WorldFirework.java"));
  }

}

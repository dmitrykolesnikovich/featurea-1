package featurea.builder;

import com.badlogic.gdx.texturePacker.TexturePacker;
import featurea.app.Builder;
import featurea.app.Project;
import featurea.builder.java.AssetsCode;
import featurea.builder.java.ConfigToJavaTool;
import featurea.builder.java.GenJavaTool;
import featurea.util.FileUtil;

import java.io.File;

// manifest not project is target for builder. builder has it's own project instance while player has it's own
public class BuilderImpl implements Builder {

  private Project project;
  private boolean isTexturePack;

  @Override
  public void build(File file, boolean isTexturePack) {
    this.project = new Project();
    this.project.setFile(file);
    this.project.setProduction(false);
    this.isTexturePack = isTexturePack;
    Thread thread = new Thread() {
      @Override
      public void run() {
        performBuild();
        System.out.println("Build complete");
      }
    };
    thread.start();
  }

  private void performBuild() {
    if (isTexturePack) {
      FileUtil.deleteDirectory(project.generatedFilesDir);
      new TexturePacker(project).pack();
    }
    buildDependencies(project);
    new ManifestGenTool(project).performGen();
  }

  private void buildDependencies(Project parent) {
    for (Project child : parent.children) {
      buildDependencies(child);
    }
    buildDependency(parent);
  }

  private void buildDependency(Project project) {
    try {
      new ConfigToJavaTool(project).performGen();
      new AssetsCode(project).performGen();
      new GenJavaTool(project).performGen();
      new ToolExecutor(project).performGen();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

}

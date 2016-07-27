package featurea.app;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import featurea.util.*;
import featurea.xml.XmlFormatter;
import featurea.xml.XmlPrimitives;
import featurea.xml.XmlSchema;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Project {

  /*components*/
  private boolean isProduction;
  public final ProjectClassLoader classLoader = new ProjectClassLoader();
  private Files files;
  public XmlSchema xmlSchema;
  public XmlFormatter xmlFormatter;
  public Settings settings;
  public XmlPrimitives xmlPrimitives;

  public File file;
  @NotNull
  public String pakage;
  public final List<File> classPath = new ArrayList<>();
  @Nullable
  public File resourcesDir;
  @Nullable
  public File configurationDir;
  @Nullable
  public File generatedFilesDir;
  @NotNull
  public Properties packProperties;
  @NotNull
  public Properties toolsProperties;
  public final List<Project> children = new ArrayList<>();
  public Project parent;

  private final ProjectParser parser = new ProjectParser(this);
  public final XmlResources resources = new XmlResources(this);

  public Project() {

  }

  /**
   * @param file 1) project.xml, 2) artifact or 3) package inside artifact
   */
  /*package*/ Project(File file, Project parent) {
    this.parent = parent;
    setFile(file);
  }

  public void setFile(File file) {
    try {
      this.file = file;
      if (file.getName().endsWith(".jar")) {
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        JarEntry jarEntry = jarFile.getJarEntry("project.xml");
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        parser.readInputStream(inputStream);
        classPath.add(file); // IMPORTANT
      } else if ("project.xml".equals(file.getName())) {
        parser.readInputStream(new FileInputStream(file));
        File dir = this.file.getParentFile();
        resourcesDir = new File(dir, "res");
        configurationDir = new File(dir, "config");
        generatedFilesDir = new File(dir, "gen");
      } else {
        pakage = FileUtil.formatPath(file.getPath());
      }
      setupComponents(false);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public File findFile(String path) {
    return FileUtil.getFile(getRoot(), path);
  }

  public File getRoot() {
    return file.getAbsoluteFile().getParentFile();
  }

  /**/

  public List<File> getResourcesRoots() {
    List<File> result = new ArrayList<>();
    result.add(resourcesDir);
    for (Project child : children) {
      result.addAll(child.getResourcesRoots());
    }
    return result;
  }

  public List<File> getConfigurationRoots() {
    List<File> result = new ArrayList<>();
    result.add(configurationDir);
    for (Project child : children) {
      result.addAll(child.getConfigurationRoots());
    }
    return result;
  }

  public List<File> getContentRoots() {
    List<File> result = new ArrayList<>();
    result.addAll(getResourcesRoots());
    result.addAll(getConfigurationRoots());
    return result;
  }

  public List<File> getClassPath() {
    List<File> result = new ArrayList<>();
    result.addAll(classPath);
    for (Project child : children) {
      result.addAll(child.getClassPath());
    }
    return result;
  }

  public List<String> getPackages() {
    List<String> result = new ArrayList<>();
    result.add(pakage);
    for (Project child : children) {
      result.addAll(child.getPackages());
    }
    return result;
  }

  public void setProduction(boolean isProduction) {
    if (this.isProduction != isProduction) {
      setupComponents(isProduction);
    }
  }

  public boolean isProduction() {
    return isProduction;
  }

  public void build(boolean isTexturePack) {
    try {
      Builder builder = ReflectionUtil.newInstance((Class<Builder>) Class.forName("featurea.builder.BuilderImpl"));
      builder.build(file, isTexturePack);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void setupComponents(boolean isProduction) {
    this.isProduction = isProduction;
    if (isProduction) {
      files = new UnpackedFiles(this);
    } else {
      files = new Files();
      List<File> contentRoots = getContentRoots();
      files.addAll(contentRoots);
      classLoader.setContentRoots(contentRoots);
    }
    files.addAll(this.getClassPath());
    classLoader.addAll(this.getClassPath());
    if ("project.xml".equals(file.getName())) {
      xmlSchema = new XmlSchema(this);
      xmlFormatter = new XmlFormatter(this);
      xmlPrimitives = new XmlPrimitives(this);
      settings = new Settings(this);
    }
  }

  public Files getFiles() {
    return files;
  }

  @Override
  public String toString() {
    return file.getAbsolutePath();
  }

}

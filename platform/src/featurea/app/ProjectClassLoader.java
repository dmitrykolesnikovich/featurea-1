package featurea.app;

import featurea.util.ClassPath;

import java.io.File;
import java.util.List;
import java.util.jar.JarFile;

public class ProjectClassLoader {

  private ClassPath classPath = new ClassPath();

  public void setContentRoots(List<File> contentRoots) {
    classPath = new ClassPath();
    classPath.addAll(contentRoots);
  }


  public Class loadClass(String canonicalClassName) {
    if (canonicalClassName == null) {
      return null;
    }
    try {
      return classPath.loadClass(canonicalClassName);
    } catch (ClassNotFoundException skip1) {
      return null;
    }
  }

  public void add(JarFile jarFile) {
    classPath.add(jarFile);
  }

  public void addAll(List<File> contentRoots) {
    classPath.addAll(contentRoots);
  }

}

package featurea.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class ClassPath extends URLClassLoader {

  public ClassPath() {
    this(ClassPath.class.getClassLoader());
  }

  public ClassPath(ClassLoader parentClassLoader) {
    super(new URL[]{}, parentClassLoader);
  }

  public List<File> listFiles() {
    List<File> result = new ArrayList<>();
    for (URL url : getURLs()) {
      result.add(new File(url.getPath()));
    }
    return result;
  }

  public List<String> list() {
    List<String> result = new ArrayList<>();
    for (URL url : getURLs()) {
      result.add(url.getPath());
    }
    return result;
  }

  /*different add methods*/

  public ClassPath add(File file) {
    if (file != null) {
      try {
        addURL(file.toURI().toURL());
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
    return this;
  }

  public void addAll(File... files) {
    for (File file : files) {
      add(file);
    }
  }

  public void addAll(List<File> files) {
    for (File file : files) {
      add(file);
    }
  }

  public void add(JarFile file) {
    add(new File(file.getName()));
  }

  public void addAll(JarFile... files) {
    for (JarFile file : files) {
      add(file);
    }
  }

}

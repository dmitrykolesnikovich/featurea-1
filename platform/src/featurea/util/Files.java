package featurea.util;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Files {

  public PrintStream err = System.err;
  protected ClassPath classPath;

  public Files() {
    classPath = new ClassPath(null);
  }

  public List<File> getRoots() {
    return classPath.listFiles();
  }

  public Files addAll(List<File> dirs) {
    for (File dir : dirs) {
      add(dir);
    }
    return this;
  }

  /**
   * @return unpacked files list
   */
  public List<String> listFilesRecursively(String dir, String... extensions) {
    dir = FileUtil.formatPath(dir);
    Set<String> result = new TreeSet<>();
    if (FileUtil.isAbsolutePath(dir)) {
      File dirFile = new File(dir);
      FileUtil.inflateFilesRecursively(result, dirFile, extensions);
    } else {
      inflateList(result, dir, extensions);
    }

    if (Targets.isAndroid) {
      Iterator<String> iterator = result.iterator();
      while (iterator.hasNext()) {
        String file = FileUtil.formatPath(iterator.next());
        if (file.endsWith(".png") && file.startsWith("res/")) {
          iterator.remove();
        }
      }
    }
    return new ArrayList<>(result);
  }


  public boolean exists(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    if (FileUtil.isAbsolutePath(filePath)) {
      return new File(filePath).exists();
    }
    InputStream inputStream = null;
    try {
      inputStream = classPath.getResourceAsStream(filePath);
      return inputStream != null;
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getText(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    InputStream inputStream;
    if (FileUtil.isAbsolutePath(filePath)) {
      try {
        inputStream = new FileInputStream(filePath);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      inputStream = classPath.getResourceAsStream(filePath);
    }
    if (inputStream == null) {
      return null;
    }
    return FileUtil.getText(inputStream);
  }

  public InputStream getStream(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    if (FileUtil.isAbsolutePath(filePath)) {
      if (filePath.contains(".jar!")) {
        try {
          int index = filePath.indexOf(".jar!");
          String jarFilePath = filePath.substring(0, index + 4);
          String entry = filePath.substring(index + 6, filePath.length());
          JarFile jarFile = new JarFile(jarFilePath);
          JarEntry jarEntry = jarFile.getJarEntry(entry);
          return jarFile.getInputStream(jarEntry);
        } catch (Throwable e) {
          return null;
        }
      } else {
        try {
          return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
          return null;
        }
      }
    } else {
      return classPath.getResourceAsStream(filePath);
    }
  }

  public boolean isDir(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    File file = findFile(filePath);
    if (file != null) {
      return file.isDirectory();
    } else {
      return false;
    }
  }

  public boolean isFile(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    File file = findFile(filePath);
    if (file != null) {
      return file.isFile();
    } else {
      return false;
    }
  }

  public File findFile(String filePath) {
    filePath = FileUtil.formatPath(filePath);
    if (FileUtil.isAbsolutePath(filePath)) {
      File file = new File(filePath);
      if (file.exists()) {
        return file;
      }
    } else {
      try {
        URL url = classPath.getResource(filePath);
        URI uri = url.toURI();
        return new File(uri);
      } catch (Exception e) {
        // no op
      }
    }
    return null;
  }

  public String getRoot(String relativePath) {
    relativePath = FileUtil.formatPath(relativePath);
    relativePath = FileUtil.formatPath(relativePath);
    String absolutePath = FileUtil.formatPath(findFile(relativePath).getAbsolutePath());
    String root = absolutePath.replaceAll(relativePath, "");
    root = FileUtil.formatPath(root);
    return root;
  }

  public String getRelativePath(File file) {
    return getRelativePath(file.getAbsolutePath());
  }

  public String getRelativePath(String absolutePath) {
    absolutePath = FileUtil.formatPath(absolutePath);
    if (FileUtil.isAbsolutePath(absolutePath)) {
      for (File file : classPath.listFiles()) {
        String root = file.getAbsolutePath();
        root = FileUtil.formatPath(root);
        if (absolutePath.toLowerCase().startsWith(root.toLowerCase())) {
          String result = absolutePath.substring(root.length(), absolutePath.length());
          result = FileUtil.formatPath(result);
          if (result.startsWith("/")) {
            result = result.substring(1, result.length());
          }
          return result;
        }
      }
      return null;
    } else {
      return absolutePath;
    }
  }

  public void cacheIfNotExists(String internalPath, String externalPath) {
    File file = new File(externalPath);
    if (!file.exists()) {
      try {
        InputStream inputStream = classPath.getResourceAsStream(internalPath);
        FileUtil.copy(inputStream, file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void inflateList(Set<String> result, String rootDir, String... extensions) {
    try {
      Enumeration<URL> urls = classPath.getResources(rootDir);
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        URI uri = url.toURI();
        String filePath = uri.toString();
        filePath = filePath.replaceAll("jar:", "").replaceAll("file:", "");
        filePath = FileUtil.formatPath(filePath);
        if (isArchive(filePath)) {
          int index = filePath.indexOf(".jar!") + 4;
          String archive = filePath.substring(0, index);
          String archiveRootDir = filePath.substring(index + 2, filePath.length());
          result.addAll(fromArchive(archive, archiveRootDir, extensions));
        } else {
          File dir = new File(filePath);
          File[] files = dir.listFiles();
          if (files != null) {
            for (File child : files) {
              String archive = child.getName();
              String childPath = getPath(rootDir, archive);
              if (child.isFile() && FileUtil.filterByExtensions(archive, extensions) && !result.contains(childPath)) {
                result.add(childPath);
              } else if (child.isDirectory()) {
                inflateList(result, childPath, extensions);
              } else if (isArchive(filePath)) {
                result.addAll(fromArchive(archive, rootDir, extensions));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    /*for (File root : myClassPath.getClasspath()) {
      if (isArchive(root)) {
        result.addAll(fromArchive(root.getAbsolutePath(), rootDir, extensions));
      }
    }*/
  }

  private ArrayList<String> fromArchive(String archive, String rootDir, String... extensions) {
    ArrayList<String> result = new ArrayList<String>();
    try {
      JarFile jar = new JarFile(archive);
      Enumeration<? extends JarEntry> enumeration = jar.entries();
      while (enumeration.hasMoreElements()) {
        ZipEntry zipEntry = enumeration.nextElement();
        String name = zipEntry.getName();
        if (FileUtil.filterByExtensions(name, extensions) && name.startsWith(rootDir)) {
          result.add(zipEntry.getName());
        }
      }
    } catch (Throwable skip) {
      // no op
    }
    return result;
  }

  private String getPath(String dir, String file) {
    if (dir.isEmpty()) {
      return file;
    } else {
      return dir + "/" + file;
    }
  }

  private boolean isArchive(File file) {
    return isArchive(file.getAbsolutePath());
  }

  private boolean isArchive(String file) {
    return (file.endsWith(".jar") || file.contains(".jar!") || file.endsWith(".apk") || file.contains(".apk!"));
  }

  public Files add(JarFile jarFile) {
    System.out.println("addJarFile: " + jarFile);
    classPath.add(jarFile);
    return this;
  }

  public Files add(File dir) {
    classPath.add(dir);
    return this;
  }

  public Files setClassPath(File... dirs) {
    for (File dir : dirs) {
      add(dir);
    }
    return this;
  }


}

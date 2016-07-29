package featurea.util;

import featurea.app.Project;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public final class FileUtil {

  private FileUtil() {
    // no op
  }

  public static void copyPaste(InputStream from, File to) {
    copyPaste(from, to.getAbsolutePath());
  }

  public static void copyPaste(InputStream from, String to) {
    try {
      File dir = new File(to).getParentFile();
      boolean isMakeDir = dir.mkdirs();
      if (dir.exists() || isMakeDir) {
        FileOutputStream writer = new FileOutputStream(to);
        byte[] buffer = new byte[4096];
        while (true) {
          int length = from.read(buffer);
          if (length == -1) break;
          writer.write(buffer, 0, length);
        }
        from.close();
        writer.close();
      } else {
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copy(InputStream input, StringBuffer data) {
    String str;
    BufferedReader in;
    try {
      in = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
      while ((str = in.readLine()) != null) {
        data.append("\n" + str);
      }
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static File write(String text, File file) {
    try {
      file.delete();
      file.getParentFile().mkdirs();
      file.createNewFile();
      BufferedWriter output = new BufferedWriter(new FileWriter(file));
      output.write(text);
      output.flush();
      output.close();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return file;
  }

  public static void copy(File from, File to) throws IOException {
    copy(new FileInputStream(from), to);
  }

  public static void copy(InputStream from, File to) throws IOException {
    if (to.exists()) {
      to.delete();
    }
    if (!to.exists()) {
      String path = to.getAbsolutePath();
      path = formatPath(path);
      int index = path.lastIndexOf("/");
      String dir = path.substring(0, index);
      File dirFile = new File(dir);
      if (!dirFile.exists()) {
        if (!dirFile.mkdirs()) {
          System.err.println(dirFile + " can not be created");
        }
      }
      to.createNewFile();
    }
    try {
      from.reset();
    } catch (Exception skip) {
    }
    int count;
    OutputStream localDbStream = new FileOutputStream(to);
    byte[] buffer = new byte[1024];
    while ((count = from.read(buffer)) != -1) {
      localDbStream.write(buffer, 0, count);
    }
    localDbStream.flush();
    localDbStream.close();
    from.close();
  }

  public static String formatPath(String file) {
    if (file == null || file.isEmpty()) {
      return file;
    }
    file = escape(file);
    Map<String, String> properties = System.getenv();
    for (String key : properties.keySet()) {
      String regex = "\\$\\{" + key + "\\}";
      String value = escape(properties.get(key));
      try {
        file = file.replaceAll(regex, value);
      } catch (StringIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  private static String escape(String file) {
    if (file == null || file.isEmpty()) {
      return file;
    }
    file = file.replaceAll("%20", " ").replaceAll("\\\\", "/").replaceAll("file://", "");
    if (file.charAt(0) == '/' && file.contains(":")) {
      file = file.substring(1, file.length());
    }
    if (file.endsWith("/")) {
      file = file.substring(0, file.length() - 1);
    }
    if (file.charAt(file.length() - 1) == '/') {
      file = file.substring(0, file.length() - 1);
    }
    return file;
  }

  public static boolean isAbsolutePath(String filePath) {
    if (filePath == null) {
      System.out.println("breakpoint");
    }
    return filePath.startsWith("/") || filePath.contains(":");
  }

  public static String getNameWithoutExtension(String file) {
    String last = getName(file);
    String[] nameAndExtension = last.split("\\.");
    String name = nameAndExtension[0];
    return name;
  }

  public static String getName(String file) {
    file = formatPath(file);
    String[] token = file.split("/");
    String result = token[token.length - 1];
    return result;
  }

  public static String getParent(String file) {
    int lastIndex = file.lastIndexOf("/");
    String parent = file.substring(0, lastIndex);
    return parent;
  }

  public static String getDir(String file) {
    file = formatPath(file);
    int lastIndex = 0;
    try {
      lastIndex = file.lastIndexOf("/");
      if (lastIndex == -1) {
        return "";
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return file.substring(0, lastIndex);
  }

  /*
  * ALARM
  */
  public static void deleteDirectory(File directory) {
    if (directory.isFile()) {
      directory.delete();
    } else if (directory.isDirectory()) {
      File[] children = directory.listFiles();
      if (children != null) {
        for (File child : children) {
          deleteDirectory(child);
        }
      }
      if (directory.list().length == 0) {
        directory.delete();
      }
    }
  }

  public static String getFile(Class klass) {
    URL location = klass.getProtectionDomain().getCodeSource().getLocation();
    if (location == null) {
      return null;
    }
    String sourcePath = formatPath(location.getPath());
    return sourcePath;
  }

  public static List<File> formatStringsToFiles(String[] files) {
    List<File> result = new ArrayList<>();
    for (String path : files) {
      path = formatPath(path);
      File file = new File(path);
      if (file.exists()) {
        result.add(file);
      } else {
        System.err.println("[FileUtil.formatStringsToFiles] File not found: " + path);
      }
    }
    return result;
  }

  public static String getText(File file) {
    try {
      return getText(new FileInputStream(file));
    } catch (Exception e) {
      return null;
    }
  }

  public static String getText(InputStream inputStream) {
    StringBuffer result = new StringBuffer();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
    String line;
    try {
      boolean isFirstLine = true;
      while ((line = reader.readLine()) != null) {
        if (!isFirstLine) {
          result.append("\n");
        }
        result.append(line);
        isFirstLine = false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("");
    } finally {
      try {
        inputStream.close();
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result.toString();
  }

  public static void inflateFilesRecursively(Set<String> result, File root, String... extensions) {
    File[] listFiles = root.listFiles();
    if (listFiles != null) {
      for (File file : listFiles) {
        if (file.isFile() && filterByExtensions(file.getName(), extensions)) {
          result.add(file.getAbsolutePath());
        } else {
          inflateFilesRecursively(result, file, extensions);
        }
      }
    }
  }

  public static boolean filterByExtensions(String name, String[] extensions) {
    if (extensions.length == 0) {
      return true;
    }
    for (String ex : extensions) {
      if (name.endsWith(ex)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isArchive(Class klass) {
    return Targets.isMobile || retrieveJarFile(klass) != null;
  }

  public static JarFile retrieveJarFile(Class klass) {
    try {
      String file = getFile(klass);
      if (file != null && file.endsWith(".jar")) {
        return new JarFile(file);
      }
    } catch (Throwable skip) {
      // no op
    }
    return null;
  }

  public static List<File> toFiles(String[] array) {
    List<File> result = new ArrayList<>();
    for (String item : array) {
      result.add(new File(item));
    }
    return result;
  }

  public static File retrieveProjectFileByXmlFile(File file) {
    for (File dir = file.getParentFile(); dir != null; dir = dir.getParentFile()) {
      File[] files = dir.listFiles();
      if (files != null) {
        for (File childFile : files) {
          if (childFile.getName().endsWith(Project.PROJECT_FILE_NAME)) {
            return childFile;
          }
        }
      }
    }
    return null;
  }

  public static File getFile(File root, String path) {
    try {
      path = FileUtil.formatPath(path);
      while (path.startsWith("../")) {
        path = path.substring(3, path.length());
        root = root.getParentFile();
        if (root == null) {
          return null;
        }
      }
      return new File(root, path);
    } catch (NullPointerException e) {
      e.printStackTrace();
      return null;
    }
  }

}

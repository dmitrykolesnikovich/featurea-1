package featurea.util;

import featurea.app.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;

public class Properties {

  public final Map<String, String> properties = new LinkedHashMap<>();

  public Properties() {
    // no op
  }

  public Properties(Project project, String file) {
    this(project.getFiles().getStream(file));
  }

  public Properties(InputStream inputStream) {
    properties.putAll(readProperties(inputStream));
  }

  public Properties(File file) {
    try {
      properties.putAll(readProperties(new FileInputStream(file)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> readProperties(InputStream inputStream) {
    Map<String, String> result = new LinkedHashMap<>();
    LinesIterator iterator = new LinesIterator(inputStream);
    while (iterator.hasNext()) {
      String line = iterator.next();
      if (!line.isEmpty() && !line.startsWith("#")) {
        int index = line.indexOf("=");
        String[] tokens = new String[]{line.substring(0, index), line.substring(index + 1, line.length())};
        String key = tokens[0].trim();
        String value;
        if (tokens.length == 2) {
          value = tokens[1].trim();
        } else {
          value = "";
        }
        result.put(key, value);
      }
    }
    return result;
  }

  public String getValue(String key) {
    return properties.get(key);
  }

  public void put(String key, String value) {
    properties.put(key, value);
  }

  public boolean containsKey(String key) {
    return properties.containsKey(key);
  }

  public Collection<String> values() {
    return properties.values();
  }

  public Set<String> getKeys() {
    return properties.keySet();
  }

  public List<Map.Entry<String, String>> entrySet() {
    return new ArrayList<>(properties.entrySet());
  }

  public String getKey(String value) {
    if (value == null) {
      return null;
    }
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      if (entry.getValue().equals(value)) {
        return entry.getKey();
      }
    }
    return null;
  }

  private static class LinesIterator implements Iterator<String> {

    private String[] lines;
    private int index = -1;

    public LinesIterator(File file) {
      String text = FileUtil.getText(file);
      if (text != null) {
        lines = text.trim().split("\n");
      }
    }

    public LinesIterator(InputStream file) {
      String text = FileUtil.getText(file);
      if (text != null) {
        lines = text.trim().split("\n");
      }
    }

    @Override
    public boolean hasNext() {
      if (lines == null) {
        return false;
      }
      return index < lines.length - 1;
    }

    @Override
    public String next() {
      index++;
      String result = lines[index].trim();
      while (result.endsWith("\\") && index < lines.length) {
        result = result.substring(0, result.length() - 1);
        index++;
        if (index < lines.length) {
          result += lines[index].trim();
        }
      }
      return result;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove");
    }
  }

}

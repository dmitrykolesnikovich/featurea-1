package featurea.xml;

import featurea.app.MediaPlayer;
import featurea.app.Config;
import featurea.app.Project;
import featurea.util.Format;
import featurea.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlFormatter extends Config {

  public XmlFormatter(Project project) {
    super(project, "xmlFormatter.properties");
  }

  public List<Format> getFormats(XmlTag xmlTag, String key) {
    List<Format> result = new ArrayList<>();
    String field = xmlTag.name + "." + key;
    Map<String, String> definitions = getDefinitions(field);
    for (Map.Entry<String, String> formatDefinition : definitions.entrySet()) {
      Format format = newFormat(formatDefinition, xmlTag, key);
      result.add(format);
    }
    return result;
  }

  /**
   * @return size or -1
   */
  public int getSize(String field) {
    Map<String, String> definitions = getDefinitions(field);
    String value = definitions.get("size");
    if (value != null) {
      return Integer.valueOf(value);
    } else {
      return -1;
    }
  }

  /*private API*/

  private Map<String, String> getDefinitions(String field) {
    String value = getValue(field);
    String[] formatDefinitions = StringUtil.split(value, ",");
    Map<String, String> result = new HashMap<>();
    for (String formatDefinition : formatDefinitions) {
      String[] tokens = StringUtil.split(formatDefinition, "=");
      result.put(tokens[0], tokens[1]);
    }
    return result;
  }

  private Format newFormat(Map.Entry<String, String> definition, XmlTag xmlTag, String key) {
    switch (definition.getKey()) {
      case Size.KEY: {
        return new Size(xmlTag, key, Integer.valueOf(definition.getValue()));
      }
      case Delimiter.KEY: {
        return new Delimiter(xmlTag, key, definition.getValue());
      }
    }
    throw new IllegalArgumentException("formatDefinition = " + definition);
  }

  private class Size implements Format {

    public static final String KEY = "size";

    private final XmlTag xmlTag;
    private final String key;
    private final int count;

    public Size(XmlTag xmlTag, String key, int count) {
      this.xmlTag = xmlTag;
      this.key = key;
      this.count = count;
    }

    @Override
    public String format(String text) {
      String result = "";
      String[] tokens = StringUtil.split(text, ",");
      for (int i = 0; i < tokens.length; i++) {
        if (i != 0 && count != 0 && i % count == 0) {
          result += "\n";
        }
        result += tokens[i] + ",";
      }
      return result;
    }
  }

  private class Delimiter implements Format {

    public static final String KEY = "delimiter";

    private final XmlTag xmlTag;
    private final String key;
    private final String delimiter;

    public Delimiter(XmlTag xmlTag, String key, String delimiter) {
      this.xmlTag = xmlTag;
      this.key = key;
      this.delimiter = delimiter;
    }

    @Override
    public String format(String text) {
      String result = "";
      String[] tokens = StringUtil.split(text, delimiter);
      for (int i = 0; i < tokens.length; i++) {
        result += tokens[i] + delimiter + "\n";
      }
      return result;
    }
  }

  public String format(XmlTag xmlTag, String key, String value) {
    List<Format> formats = getFormats(xmlTag, key);
    for (Format format : formats) {
      value = format.format(value);
    }
    return value;
  }

}

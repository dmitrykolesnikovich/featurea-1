package featurea.xml;

import featurea.app.Config;
import featurea.app.Project;
import featurea.util.ReflectionUtil;
import featurea.util.StringUtil;

import java.util.*;

// https://www.w3.org/standards/xml/schema
public class XmlSchema extends Config {

  public XmlSchema(Project project) {
    super(project, "xmlSchema.properties");
  }

  public String getTagName(String canonicalClassName) {
    for (Map.Entry<String, String> entry : entrySet()) {
      if (entry.getValue().startsWith(canonicalClassName + ":")) {
        String key = entry.getKey();
        if (!key.contains(".")) {
          return key;
        }
      }
    }
    return null;
  }

  public String getCanonicalClassName(String tagName) {
    String value = getValue(tagName);
    if (value != null) {
      String[] tokens = StringUtil.split(value, ":");
      return tokens[0];
    }
    return tagName; // IMPORTANT
  }

  public String getCanonicalSuperClassName(String tagName) {
    String value = getValue(tagName);
    if (value != null) {
      String[] tokens = StringUtil.split(value, ":");
      if (tokens.length == 2) {
        return tokens[1];
      }
    }
    return null;
  }

  public List<String> getAttributes(final String tagName) {
    List<Set<String>> result = new ArrayList<>();
    String currentName = tagName;
    while (currentName != null && !Object.class.getCanonicalName().equals(getCanonicalClassName(currentName))) {
      Set<String> currentResult = new LinkedHashSet<>();
      inflateWithSchema(currentResult, currentName, this);
      result.add(currentResult);
      currentName = getTagName(getCanonicalSuperClassName(currentName));
    }
    Collections.reverse(result);
    Set<String> finalResult = new LinkedHashSet<>();
    for (Set<String> item : result) {
      finalResult.addAll(item);
    }
    ArrayList<String> list = new ArrayList<>(finalResult);
    list.add(0, "id");
    list.add(1, "link");
    return list;
  }

  private void inflateWithSchema(Set<String> result, String tagName, Config config) {
    for (String key : config.properties.keySet()) {
      String prefix = tagName + ".";
      if (key.startsWith(prefix)) {
        result.add(key.replaceAll(prefix, ""));
      }
    }
  }

  public boolean isEnum(XmlTag xmlTag, String key) {
    String canonicalSuperClassName = getCanonicalSuperClassName(xmlTag.name + "." + key);
    String canonicalClassName = getCanonicalClassName(xmlTag.name + "." + key);
    return !canonicalClassName.contains(",") && !ReflectionUtil.isPrimitive(canonicalClassName) && canonicalSuperClassName == null;
  }

  public String getSuperKey(String key) {
    String[] tokens = StringUtil.split(key, "\\.");
    if (tokens.length == 2) {
      String tagName = tokens[0];
      String primitiveName = tokens[1];
      String superClassName = getCanonicalSuperClassName(tagName);
      if (superClassName != null && !Object.class.getCanonicalName().equals(superClassName)) {
        String superTagName = getTagName(superClassName);
        if (superTagName != null) {
          return superTagName + "." + primitiveName;
        } else {
          getTagName(superClassName);
          throw new NullPointerException();
        }
      }
    }
    return null;
  }

}

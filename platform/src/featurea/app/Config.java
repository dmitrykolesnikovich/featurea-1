package featurea.app;

import featurea.util.Properties;

import java.io.InputStream;
import java.util.Map;

public class Config extends Properties {

  public final Project project;
  public final String name;

  public Config(Project project, String name) {
    this.project = project;
    this.name = name;
    inflateProperties(name);
  }

  @Override
  public String getValue(String key) {
    String value = super.getValue(key);
    if (value != null) {
      return value;
    } else {
      key = project.xmlSchema.getSuperKey(key);
      if (key != null) {
        return getValue(key);
      } else {
        return null;
      }
    }
  }

  private Config inflateProperties(String name) {
    for (String pakage : project.getPackages()) {
      String file = pakage.replaceAll("\\.", "/") + "/" + name;
      InputStream inputStream = project.getFiles().getStream(file);
      if (inputStream != null) {
        Map<String, String> newProperties = readProperties(inputStream);
        for (Map.Entry<String, String> entry : newProperties.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();
          if (!this.properties.containsKey(key)) {
            this.properties.put(key, value);
          }
        }
      }
    }
    return this;
  }

}

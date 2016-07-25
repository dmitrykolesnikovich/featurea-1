package featurea.util;

import java.io.File;
import java.util.Map;

// IMPORTANT Config is read only, while Preferences is writable. That's why Preferences is not Config.
public class Preferences extends Properties {

  public final File file;

  public Preferences(File file) {
    super(file);
    this.file = file;
  }

  public void save() {
    StringBuilder stringBuilder = new StringBuilder();
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      stringBuilder.append(key + "=" + value + "\n");
    }
    FileUtil.write(stringBuilder.toString(), file);
  }

}

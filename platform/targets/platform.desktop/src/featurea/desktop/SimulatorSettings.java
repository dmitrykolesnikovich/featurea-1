package featurea.desktop;

import featurea.app.MediaPlayer;
import featurea.app.Config;
import featurea.app.Project;
import featurea.util.Size;

public class SimulatorSettings extends Config {

  public SimulatorSettings(Project project) {
    super(project, "simulator.properties");
  }

  public int getWidth() {
    String sizeValue = properties.get("size");
    if (sizeValue != null) {
      Size size = Size.valueOf(sizeValue);
      return (int) size.width;
    } else {
      return 320;
    }
  }

  public int getHeight() {
    String sizeValue = properties.get("size");
    if (sizeValue != null) {
      Size size = Size.valueOf(sizeValue);
      return (int) size.height;
    } else {
      return 480;
    }
  }

  public String getTitle() {
    return properties.get("title");
  }

}

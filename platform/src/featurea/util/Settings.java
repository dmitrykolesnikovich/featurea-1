package featurea.util;

import featurea.app.Config;
import featurea.app.Project;

import java.util.Arrays;
import java.util.List;

public class Settings extends Config {

  public Settings(Project project) {
    super(project, "settings.properties");
  }

  public double getStep() {
    String step = getValue("step");
    if (step != null) {
      try {
        return Double.valueOf(step);
      } catch (NumberFormatException skip) {
        // no op
      }
    }
    return 1;
  }

  public int roundValue(double value) {
    double step = getStep();
    value /= step;
    value *= step;
    return (int) value;
  }

  public String getScreenId() {
    return getValue("screenId");
  }

  public List<String> getRebuild() {
    return Arrays.asList(StringUtil.split(getValue("rebuild"), ","));
  }

  public double getMaxScale() {
    String maxScaleString = getValue("maxScale");
    if (maxScaleString != null) {
      try {
        return Double.valueOf(maxScaleString);
      } catch (NumberFormatException skip) {
        // no op
      }
    }
    return 100;
  }

  public double getPointScale() {
    String pointScaleString = getValue("pointScale");
    if (pointScaleString != null) {
      try {
        return Double.valueOf(pointScaleString);
      } catch (NumberFormatException skip) {
        // no op
      }
    }
    return 10;
  }

  public double getWarmupTime() {
    String pointScaleString = getValue("warmupTime");
    if (pointScaleString != null) {
      try {
        return Double.valueOf(pointScaleString);
      } catch (NumberFormatException skip) {
        // no op
      }
    }
    return 0;
  }

}

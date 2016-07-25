package featurea.util;

import featurea.app.Config;

import java.io.File;

public class ConfigUtil {

  private ConfigUtil() {
    // no op
  }

  public static String toJava(Properties properties, String file) {
    String className = StringUtil.uppercaseFirstLetter(FileUtil.formatPath(FileUtil.getName(file)).
        replaceAll("\\.properties", ""));
    StringBuffer buffer = new StringBuffer();
    buffer.append("public class " + className + " {");
    for (String key : properties.getKeys()) {
      String value = properties.getValue(key);
      String code;
      try {
        double formatValue = CastUtil.castValue(value, double.class);
        code = "public static double " + key + " = " + formatValue + ";";
      } catch (Throwable e) {
        try {
          double[] formatValue = CastUtil.castValue(value, double[].class);
          code = "public static double[] " + key + " = new double[]{";
          for (double element : formatValue) {
            code += element + ",";
          }
          code += "};";
        } catch (Throwable e1) {
          try {
            String formatValue = CastUtil.castValue(value, String.class);
            code = "public static String " + key + " = \"" + formatValue + "\";";
          } catch (Throwable e3) {
            code = e3.getMessage();
          }
        }
      }
      buffer.append(code);
    }
    buffer.append("}");
    return buffer.toString();
  }

  public static void load(Config config, Class klass) {
    for (String key : config.getKeys()) {
      String value = config.getValue(key);
      ReflectionUtil.setStaticField(klass, key, value);
    }
  }

  public static void save(Config config, String file) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : config.getKeys()) {
      String value = config.getValue(key);
      stringBuilder.append(key + "=" + value + "\n");
    }
    FileUtil.write(stringBuilder.toString(), new File(file));
  }

}

package featurea.util;

import featurea.app.XmlResources;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class FormatUtil {

  private FormatUtil() {
    // no op
  }

  public static String formatClassName(String className) {
    return className.replaceAll("\\$", ".").replaceAll(".class", "");
  }

  public static String formatArgsString(String argsString, Class klass, String methodName) throws FormatException {
    for (Method method : ReflectionUtil.getMethods(klass, methodName)) {
      try {
        String[] values = ArrayUtil.split(argsString);
        return formatArgsString(values, method.getParameterTypes());
      } catch (FormatException skip) {
      }
    }
    throw new FormatException();
  }

  public static String formatArgsString(String[] values, Class... classes) throws FormatException {
    if (classes.length == 1 && classes[0].isArray()) {
      Class componentType = classes[0].getComponentType();
      return formatArray(values, componentType);
    }
    if (classes.length == values.length) {
      String result = "";
      for (int i = 0; i < values.length; i++) {
        result += formatValue(values[i], classes[i]) + ", ";
      }
      result = result.substring(0, result.length() - 2);
      return result;
    } else if (classes.length == 1) {
      return formatValue(ArrayUtil.toString(values), classes[0]);
    }
    throw new FormatException();
  }

  public static String formatValue(String value, Class klass) throws FormatException {
    String canonicalName = formatClassName(klass.getCanonicalName());
    if (null == value) {
      return null;
    }
    if ("false".equals(value)) {
      return "false";
    }
    if ("true".equals(value)) {
      return "true";
    }
    if (klass == String.class) {
      return "\"" + value + "\"";
    }
    try {
      int result = Integer.valueOf(value);
      return value + "";
    } catch (NumberFormatException skip) {
    }
    try {
      double result = Float.valueOf(value);
      return value + "f";
    } catch (NumberFormatException skip) {
    }
    try {
      double result = Double.valueOf(value);
      return value + "";
    } catch (NumberFormatException skip) {
      // no op
    }
    Field field = ReflectionUtil.getField(klass, value);
    if (field != null && Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
      return canonicalName + "." + value;
    }
    try {
      Method method = klass.getMethod("valueOf", String.class);
      if (method != null) {
        return canonicalName + ".valueOf(\"" + value + "\")";
      }
    } catch (Exception skip) {
    }
    throw new FormatException();
  }

  private static String formatArray(String[] array, Class componentType) throws FormatException {
    if (componentType == int.class) {
      String result = "new int[] { ";
      for (String element : array) {
        result += element + ", ";
      }
      result += "}";
      return result;
    }
    if (componentType == double.class) {
      String result = "new double[] { ";
      for (String element : array) {
        result += element + "f, ";
      }
      result += "}";
      return result;
    }
    if (componentType == double.class) {
      String result = "new double[] { ";
      for (String element : array) {
        result += element + ", ";
      }
      result += "}";
      return result;
    }
    if (componentType == String.class) {
      String result = "new String[] { ";
      for (String element : array) {
        result += "\"" + element.trim() + "\", ";
      }
      result += "}";
      return result;
    }
    throw new FormatException();
  }

  public static String formatArray(String array, String componentType) throws FormatException {
    if ("int[]".equals(componentType)) {
      String result = "new int[] { ";
      result += array;
      result += "}";
      return result;
    }
    if ("double[]".equals(componentType)) {
      String result = "new double[] { ";
      result += array;
      result += "}";
      return result;
    }
    if ("double[]".equals(componentType)) {
      String result = "new double[] { ";
      result += array;
      result += "}";
      return result;
    }
    throw new FormatException();
  }

  public static String formatArgsString(XmlResources resources, String[] values, String[] classes) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < classes.length; i++) {
      String value = values[i];
      String klass = classes[i];

      // >>
      String[] tokens = StringUtil.split(klass, ":");
      klass = tokens[0];
      String canonicalSuperClassName = tokens.length >= 2 ? tokens[1] : null;
      // <<

      if (klass.endsWith("[]")) {
        try {
          value = FormatUtil.formatArray(value, klass);
        } catch (FormatException e) {
          e.printStackTrace();
        }
      } else if (String.class.getCanonicalName().equals(klass)) {
        value = "\"" + value + "\"";
      } else if (int.class.getCanonicalName().equals(klass) || double.class.getCanonicalName().equals(klass) ||
          double.class.getCanonicalName().equals(klass) || boolean.class.getCanonicalName().equals(klass)) {
        // no op
      } else {
        if (canonicalSuperClassName == null || Enum.class.getCanonicalName().equals(canonicalSuperClassName)) {
          value = formatClassName(klass) + "." + value;
        } else {
          value = klass + ".valueOf(\"" + value + "\")";
        }
      }
      result.append(value + ",");
    }

    String stringResult = result.toString();
    if (!stringResult.isEmpty()) {
      stringResult = stringResult.substring(0, stringResult.length() - 1);
    }
    return stringResult;
  }

}

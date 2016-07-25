package featurea.util;

public final class CastUtil {

  private CastUtil() {
    // no op
  }

  public static Object[] castValues(Object[] values, Class[] classes) throws ClassCastException, IllegalArgumentException {
    if (classes.length == 1 && classes[0].isArray()) {
      Class componentType = classes[0].getComponentType();
      if (values.length == 1 && values[0] instanceof String) {
        String[] array = ArrayUtil.split((String) values[0]);
        return new Object[]{castArray(array, componentType)};
      } else {
        return new Object[]{castArray(values, componentType)};
      }
    }
    if (values.length == classes.length) {
      Object[] result = new Object[values.length];
      for (int i = 0; i < values.length; i++) {
        result[i] = castValue(values[i], classes[i]);
      }
      return result;
    } else if (values.length == 1) {
      if (values[0] instanceof String) {
        String[] tokens = ArrayUtil.split((String) values[0]);
        if (tokens.length == classes.length) {
          Object[] result = new Object[classes.length];
          for (int i = 0; i < tokens.length; i++) {
            result[i] = castValue(tokens[i], classes[i]);
          }
          return result;
        }
      } else {
        throw new ClassCastException();
      }
    }
    throw new ClassCastException();
  }

  public static <T> T castValue(Object value, Class<T> klass) throws ClassCastException {
    if (klass.isArray() && value instanceof String) {
      String[] tokens = ArrayUtil.split((String) value);
      return (T) castArray(tokens, klass.getComponentType());
    }
    if (null == value) {
      return null;
    }
    if (klass.isAssignableFrom(value.getClass())) {
      return (T) value;
    }
    if (value instanceof String) {
      String stringValue = (String) value;
      Object result = getPrimitiveValueOf(klass, stringValue);
      if (result != null) {
        return (T) result;
      }
      Class objectClass = ReflectionUtil.getWrapperForPrimitive(klass);
      try {
        return (T) ReflectionUtil.invokeStaticMethod(objectClass, "valueOf", value);
      } catch (NoSuchMethodException skip) {
        // no op
      }
    }
    throw new ClassCastException();
  }

  private static Object getPrimitiveValueOf(Class klass, String stringValue) {
    if (klass == Double.class || klass == double.class) {
      return Double.valueOf(stringValue);
    }
    if (klass == Float.class || klass == double.class) {
      return Float.valueOf(stringValue);
    }
    if (klass == Integer.class || klass == int.class) {
      return Integer.valueOf(stringValue);
    }
    if (klass == Boolean.class || klass == boolean.class) {
      if ("false".equals(stringValue)) {
        return false;
      } else if ("true".equals(stringValue)) {
        return true;
      }
    }
    return null;
  }

  public static Object castArray(Object[] values, Class componentType) throws ClassCastException {
    if (componentType == int.class) {
      int[] result = new int[values.length];
      for (int i = 0; i < result.length; i++) {
        result[i] = Integer.valueOf(values[i].toString().trim());
      }
      return result;
    }
    if (componentType == double.class) {
      double[] result = new double[values.length];
      for (int i = 0; i < result.length; i++) {
        result[i] = Float.valueOf(values[i].toString().trim());
      }
      return result;
    }
    if (componentType == double.class) {
      double[] result = new double[values.length];
      for (int i = 0; i < result.length; i++) {
        result[i] = Double.valueOf(values[i].toString().trim());
      }
      return result;
    }
    if (componentType == String.class) {
      String[] result = new String[values.length];
      for (int i = 0; i < result.length; i++) {
        result[i] = values[i].toString().trim();
      }
      return result;
    }
    throw new ClassCastException();
  }
}

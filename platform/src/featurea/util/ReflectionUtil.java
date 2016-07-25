package featurea.util;

import featurea.app.Context;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ReflectionUtil {

  private static final Class[] PRIMITIVE_CLASSES = {
      byte.class,
      short.class,
      int.class,
      double.class,
      double.class,
      boolean.class,
      char.class,

      byte[].class,
      short[].class,
      int[].class,
      double[].class,
      double[].class,
      boolean[].class,
      char[].class,
  };

  private static final Comparator<Method> METHODS_COMPARATOR = new Comparator<Method>() {
    @Override
    public int compare(Method method1, Method method2) {
      Class holderClass1 = method1.getDeclaringClass();
      Class holderClass2 = method2.getDeclaringClass();
      if (holderClass2.equals(holderClass1)) {
        Class<?>[] parameterTypes1 = method1.getParameterTypes();
        Class<?>[] parameterTypes2 = method2.getParameterTypes();
        if (parameterTypes1.length > parameterTypes2.length) {
          return -1;
        } else if (parameterTypes2.length < parameterTypes1.length) {
          return 1;
        } else {
          for (int i = 0; i < parameterTypes1.length; i++) {
            Class parameterType1 = parameterTypes1[i];
            Class parameterType2 = parameterTypes2[i];
            if (parameterType2.equals(parameterType1)) {
              continue;
            } else if (parameterType2.isAssignableFrom(parameterType1)) {
              return -1;
            } else if (parameterType1.isAssignableFrom(parameterType2)) {
              return 1;
            } else {
              return 0;
            }
          }
          return 0;
        }
      } else {
        if (holderClass2.isAssignableFrom(holderClass1)) {
          return -1;
        } else {
          return 1;
        }
      }
    }
  };

  private ReflectionUtil() {
    // no op
  }

  public static Method getMethod(Class klass, String methodName, Class... parameterTypes) {
    for (Method method : klass.getDeclaredMethods()) {
      if (method.getName().equals(methodName)) {
        if (method.getParameterTypes().length == parameterTypes.length) {
          for (int i = 0; i < parameterTypes.length; i++) {
            if (method.getParameterTypes()[i].isAssignableFrom(parameterTypes[i])) {
              method.setAccessible(true);
              return method;
            }
          }
        }
      }
    }
    if (klass == Object.class || klass.getSuperclass() == null) {
      return null;
    } else {
      return getMethod(klass.getSuperclass(), methodName, parameterTypes);
    }
  }

  public static List<Method> getMethods(Class klass, String methodName) {
    List<Method> result = new ArrayList<>();
    inflateMethods(result, klass, methodName);
    Collections.sort(result, METHODS_COMPARATOR);
    return result;
  }

  private static void inflateMethods(List<Method> result, Class klass, String methodName) {
    for (Method method : klass.getDeclaredMethods()) {
      if (method.getName().equals(methodName)) {
        method.setAccessible(true); // IMPORTANT
        result.add(method);
      }
    }
    if (klass != Object.class && !klass.isInterface()) {
      inflateMethods(result, klass.getSuperclass(), methodName);
    }
  }

  public static List<Method> getStaticMethods(Class klass, String methodName) {
    List<Method> result = new ArrayList<>();
    inflateStaticMethods(result, klass, methodName);
    Collections.sort(result, METHODS_COMPARATOR);
    return result;
  }

  private static void inflateStaticMethods(List<Method> result, Class klass, String methodName) {
    for (Method method : klass.getDeclaredMethods()) {
      if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers())) {
        method.setAccessible(true);
        result.add(method);
      }
    }
    if (klass != Object.class && !klass.isInterface()) {
      inflateStaticMethods(result, klass.getSuperclass(), methodName);
    }
  }

  public static String getSetterMethodName(String key) {
    return "set" + StringUtil.uppercaseFirstLetter(key);
  }

  public static String getFieldName(String name, Object value) {
    String stringValue = null;
    try {
      stringValue = value.toString();
    } catch (Throwable skip) {
      // no op
    }
    if ("true".equals(stringValue) || "false".equals(stringValue)) {
      name = "is" + name.toUpperCase().charAt(0) + name.substring(1, name.length());
      return name;
    } else {
      return name;
    }
  }

  public static Field getField(Class klass, String fieldName) {
    try {
      while (klass != null && klass != Object.class) { // IMPORTANT do not make !klass.isInterface() check here
        Field field = null;
        try {
          field = klass.getDeclaredField(fieldName);
        } catch (Exception skip) {
        }
        if (field != null) {
          field.setAccessible(true);
          return field;
        } else {
          klass = klass.getSuperclass();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T newInstance(Class<T> klass, Object... args) {
    try {
      Class[] parameterTypes = new Class[args.length];
      for (int i = 0; i < args.length; i++) {
        parameterTypes[i] = args[i].getClass();
      }
      Constructor<T> constructor = klass.getDeclaredConstructor(parameterTypes);
      T result = constructor.newInstance(args);
      return result;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return null;
    }
  }

  public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException {
    try {
      List<Method> methods = getMethods(object.getClass(), methodName);
      for (Method method : methods) {
        try {
          Object[] castArgs = CastUtil.castValues(args, method.getParameterTypes());
          return method.invoke(object, castArgs);
        } catch (ClassCastException skip) {
          // no op
        } catch (InvocationTargetException e) {
          e.printStackTrace();
          return null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    throw new NoSuchMethodException(object.getClass().getCanonicalName() + "." + methodName + ": "/* + ArrayUtil.toString(args)*/);
  }

  public static <T> T invokeStaticMethod(Class<T> klass, String methodName, Object... args) throws NoSuchMethodException {
    List<Method> methods = getStaticMethods(klass, methodName);
    for (Method method : methods) {
      try {
        Object[] castArgs = CastUtil.castValues(args, method.getParameterTypes());
        return (T) method.invoke(null, castArgs);
      } catch (ClassCastException skip) {
        // no op
      } catch (InvocationTargetException e) {
        e.printStackTrace();
        return null;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    throw new NoSuchMethodException(klass.getCanonicalName() + "." + methodName + ": " + args);
  }


  public static boolean setPublicField(Object object, String fieldName, Object value) {
    try {
      Field field = getField(object.getClass(), fieldName);
      if (field != null && Modifier.isPublic(field.getModifiers())) {
        return setField(object, fieldName, value);
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean setField(Object object, String fieldName, Object value) {
    Field field = getField(object.getClass(), fieldName);
    if (field != null) {
      try {
        field.setAccessible(true);
        Object castValue = CastUtil.castValue(value, field.getType());
        field.set(object, castValue);
        return true;
      } catch (Exception skip) {
        System.out.println("[ReflectionUtil] " + skip.getClass().getSimpleName() + ": " + skip.getMessage());
      }
    }
    return false;
  }

  public static boolean setStaticField(Class klass, String fieldName, Object value) {
    Field field = getField(klass, fieldName);
    if (field != null) {
      try {
        field.setAccessible(true);
        Object castValue = CastUtil.castValue(value, field.getType());
        field.set(null, castValue);
        return true;
      } catch (Exception skip) {
        System.out.println("[ReflectionUtil] " + skip.getClass().getSimpleName() + ": " + skip.getMessage());
      }
    }
    return false;
  }

  public static Class getWrapperForPrimitive(Class klass) {
    if (boolean.class == klass) {
      return Boolean.class;
    }
    if (int.class == klass) {
      return Integer.class;
    }
    if (double.class == klass) {
      return Float.class;
    }
    if (double.class == klass) {
      return Double.class;
    }
    return klass;
  }

  public static Class getCommonSuperClassRecursively(Class superClass, List<Class> classes) {
    for (Class klass : classes) {
      if (!superClass.isAssignableFrom(klass)) {
        return getCommonSuperClassRecursively(superClass.getSuperclass(), classes);
      }
    }
    return superClass;
  }

  public static Method getMethodAny(Class klass, String method) {
    for (Method declaredMethod : klass.getDeclaredMethods()) {
      if (declaredMethod.getName().equals(method)) {
        declaredMethod.setAccessible(true);
        return declaredMethod;
      }
    }
    if (klass == Object.class) {
      return null;
    } else {
      return getMethodAny(klass.getSuperclass(), method);
    }
  }

  public static String getFieldNameBySetterName(String name) {
    if (name.startsWith("set")) {
      name = name.substring(3, name.length());
      if (name.isEmpty()) {
        return null;
      }
      name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
      return name;
    }
    return null;
  }

  public static boolean isPrimitive(String klass) {
    for (Class primitiveClass : PRIMITIVE_CLASSES) {
      if (primitiveClass.getCanonicalName().equals(klass)) {
        return true;
      }
    }
    return false;
  }

  public static String getSimpleClassName(String canonicalClassName) {
    String[] tokens = FormatUtil.formatClassName(canonicalClassName).split("\\.");
    return tokens[tokens.length - 1];
  }

}

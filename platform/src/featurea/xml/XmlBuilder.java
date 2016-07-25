package featurea.xml;

import featurea.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public final class XmlBuilder {

  public void buildRecursively(XmlTag xmlTag) {
    buildChildren(xmlTag);
    setAttributes(xmlTag);
    addChildren(xmlTag);
    buildSelf(xmlTag);
  }

  private void buildChildren(XmlTag xmlTag) {
    for (XmlTag childXmlTag : xmlTag.getChildren()) {
      buildRecursively(childXmlTag);
    }
  }

  private void setAttributes(XmlTag xmlTag) {
    for (Map.Entry<String, String> attribute : xmlTag.getAttributes()) {
      setAttribute(xmlTag, attribute.getKey(), attribute.getValue());
    }
  }

  public void addChildren(XmlTag xmlTag) {
    List<XmlTag> children = xmlTag.getChildren();
    for (int index = 0; index < children.size(); index++) {
      XmlTag childXmlTag = children.get(index);
      add(xmlTag, childXmlTag, index);
    }
  }

  public static void add(XmlTag parentXmlTag, XmlTag childXmlTag) {
    String key = childXmlTag.getKey();
    if (key != null && parentXmlTag.getXmlSchema().getAttributes(parentXmlTag.name).contains(key)) {
      setAttribute(parentXmlTag, key, childXmlTag.getResource());
    } else {
      try {
        ReflectionUtil.invokeMethod(parentXmlTag.getResource(), "add", childXmlTag.getResource());
      } catch (NoSuchMethodException e) {
        System.err.println("[XmlBuilder.setAsSetter] NoSuchMethodException: " + e.getMessage());
      }
    }
  }

  public static void add(XmlTag parentXmlTag, XmlTag childXmlTag, int index) {
    String key = childXmlTag.getKey();
    if (key != null && parentXmlTag.getXmlSchema().getAttributes(parentXmlTag.name).contains(key)) {
      setAttribute(parentXmlTag, key, childXmlTag.getResource());
    } else {
      try {
        ReflectionUtil.invokeMethod(parentXmlTag.getResource(), "add", childXmlTag.getResource(), index);
      } catch (NoSuchMethodException e) {
        try {
          ReflectionUtil.invokeMethod(parentXmlTag.getResource(), "add", childXmlTag.getResource());
        } catch (NoSuchMethodException e1) {
          System.err.println("[XmlBuilder.setAsSetter] NoSuchMethodException: " + e1.getMessage());
        }
      }
    }
  }

  public void buildSelf(XmlTag xmlTag) {
    xmlTag.getResource().build();
  }

  public static void setAttribute(XmlTag xmlTag, String key, Object resource) {
    if (key == null) {
      throw new NullPointerException();
    }
    if ("id".equals(key) || "link".equals(key)) {
      return;
    }
    if (resource instanceof String) {
      String string = (String) resource;
      if (string.startsWith("@")) {
        String existingXmlId = string.substring(1, string.length());
        XmlTag linkXmlTag = xmlTag.context.findXmlTagById(xmlTag.getIdByLink(existingXmlId));
        setAttribute(xmlTag, key, linkXmlTag.getResource());
      }
    }
    if (resource instanceof String) {
      if (setAsLink(xmlTag, key, (String) resource)) {
        return;
      }
    }
    String attributeName = xmlTag.name + "." + key;
    String attributeCanonicalClassName = xmlTag.getXmlSchema().getCanonicalClassName(attributeName);
    try {
      if (xmlTag.getXmlSchema().isEnum(xmlTag, key)) {
        Class constantsClass = xmlTag.getProject().classLoader.loadClass(attributeCanonicalClassName);
        setAsConstant(xmlTag, key, resource.toString(), constantsClass);
      } else {
        setAsSetter(xmlTag, key, resource);
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  private static boolean setAsLink(XmlTag xmlTag, String key, String link) {
    XmlTag linkTag = xmlTag.getXmlTagByLink(link);
    if (linkTag != null) {
      setAttribute(xmlTag, key, linkTag.getResource());
      return true;
    } else {
      return false;
    }
  }

  private static boolean setAsConstant(XmlTag xmlTag, String key, String constantName, Class constantsClass) {
    try {
      String setterMethodName = ReflectionUtil.getSetterMethodName(key);
      List<Method> methods = ReflectionUtil.getMethods(xmlTag.getResource().getClass(), setterMethodName);
      for (Method method : methods) {
        if (method.getParameterTypes().length == 1) {
          Field constantField = ReflectionUtil.getField(constantsClass, constantName);
          Object constantValue = constantField.get(null);
          setAsSetter(xmlTag, key, constantValue);
          return true;
        }
      }
    } catch (Throwable skip) {
      // no op
    }
    return false;
  }

  private static boolean setAsSetter(XmlTag xmlTag, String key, Object value) {
    String setterMethodName = ReflectionUtil.getSetterMethodName(key);
    try {
      ReflectionUtil.invokeMethod(xmlTag.getResource(), setterMethodName, value);
      return true;
    } catch (NoSuchMethodException e) {
      System.err.println("[XmlBuilder.setAsSetter] NoSuchMethodException: " + e.getMessage());
      return false;
    }
  }

  public void initRecursively(XmlTag xmlTag) {
    XmlResource resource = null;
    XmlTag linkXmlTag = xmlTag.getLink();
    if (linkXmlTag != null) {
      if (linkXmlTag.prodOrDevClassLoad() == null) {
        throw new XmlFileBuildException(linkXmlTag, "Class not found: " + linkXmlTag.name);
      }
      resource = linkXmlTag.getResource();
    } else {
      if (xmlTag.prodOrDevClassLoad() == null) {
        throw new XmlFileBuildException(xmlTag, "Class not found: " + xmlTag.name);
      }
      resource = init(xmlTag);
    }
    if (resource != null) {
      xmlTag.setResource(resource);
    } else {
      // >> just for debug todo delete this
      if (linkXmlTag != null) {
        if (linkXmlTag.prodOrDevClassLoad() == null) {
          throw new XmlFileBuildException(linkXmlTag, "Class not found: " + linkXmlTag.name);
        }
        resource = linkXmlTag.getResource();
      } else {
        if (xmlTag.prodOrDevClassLoad() == null) {
          throw new XmlFileBuildException(xmlTag, "Class not found: " + xmlTag.name);
        }
        resource = init(xmlTag);
      }
      // <<
      throw new XmlFileBuildException(xmlTag, "Resource is not initialized properly: " + xmlTag.name);
    }
    for (XmlTag childXmlTag : xmlTag.getChildren()) {
      initRecursively(childXmlTag);
    }
  }

  private XmlResource init(XmlTag xmlTag) {
    if ("Joystick".equals(xmlTag.name)) {
      System.out.println("breakpoint");
    }
    Class devClass = xmlTag.devClassLoad();
    XmlResource result = null;
    if (!xmlTag.getXmlSchema().getAttributes(xmlTag.name).contains("value")) {
      String valueAttribute = xmlTag.getAttribute("value");
      if (valueAttribute != null) {
        try {
          result = (XmlResource) ReflectionUtil.invokeStaticMethod(devClass, "valueOf", valueAttribute);
        } catch (NoSuchMethodException e) {
          System.err.println(e.getMessage());
        }
      }
    }
    if (result == null) {
      result = (XmlResource) ReflectionUtil.newInstance(devClass);
    }
    return result;
  }

}

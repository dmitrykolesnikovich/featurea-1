package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.ui.ClickListener;
import featurea.util.FormatUtil;
import featurea.util.ReflectionUtil;
import featurea.util.StringUtil;
import featurea.xml.XmlContext;
import featurea.xml.XmlTag;

import java.util.Map;

public final class Setters {

  private Setters() {
    // no op
  }

  public static String code(XmlTag xmlTag, Project project) {
    StringBuffer result = new StringBuffer();
    result.append("{");
    Iterable<Map.Entry<String, String>> attributes = xmlTag.getAttributes();
    for (Map.Entry<String, String> entry : attributes) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (!"id".equals(key) && !"link".equals(key)) {
        if (!value.startsWith("@")) {
          String setAttributeCode = setAttributeCode(xmlTag, key, value, project);
          result.append(setAttributeCode);
        } else {
          String existingXmlId = value.substring(1, value.length());
          XmlTag linkXmlTag = xmlTag.context.findXmlTagById(xmlTag.getIdByLink(existingXmlId));
          String var = Variable.getVar(linkXmlTag);
          String setterMethodName = ReflectionUtil.getSetterMethodName(key);
          String setVarCode = setterMethodName + "(" + var + ");";
          result.append(setVarCode);
        }
      }
    }
    result.append("}");
    return result.toString();
  }

  private static String setAttributeCode(XmlTag xmlTag, String key, String value, Project project) {
    String canonicalClassName = project.xmlSchema.getCanonicalClassName(xmlTag.name + "." + key);
    if (ClickListener.class.getCanonicalName().equals(canonicalClassName)) {
      return "{ String a = \"!!!!!!!!\"; }";
    } else {
      String setterMethodName = ReflectionUtil.getSetterMethodName(key);
      XmlTag linkTag = xmlTag.getXmlTagByLink(value);
      if (linkTag != null) {
        return setterMethodName + "(" + Instance.code(linkTag, project) + ");";
      } else {
        XmlContext context = xmlTag.context;
        String devClassName = xmlTag.devClassName();
        String tagName = project.xmlSchema.getTagName(devClassName);
        String fieldClassName = project.xmlSchema.getValue(tagName + "." + key);
        String[] parameterTypes = StringUtil.split(fieldClassName, ",");
        String[] values = StringUtil.split(value, ",");
        if (values.length == parameterTypes.length) {
          value = FormatUtil.formatArgsString(context.resources, values, parameterTypes);
        } else if (parameterTypes.length == 1) {
          value = FormatUtil.formatArgsString(context.resources, new String[]{value}, parameterTypes);
        }
        return setterMethodName + "(" + value + ");";
      }
    }
  }

}

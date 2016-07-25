package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.ui.ClickListener;
import featurea.util.ReflectionUtil;
import featurea.xml.XmlTag;

import java.util.HashMap;
import java.util.Map;

public final class Children {

  private final Map<XmlTag, String> tag2var = new HashMap<>();

  public String code(XmlTag parent, Project project) {
    StringBuilder result = new StringBuilder();
    result.append("{");

    for (XmlTag childXmlTag : parent.getChildren()) {
      String variableName = Variable.nextName(childXmlTag);
      String prodOrDevClassName = childXmlTag.prodOrDevClassName();
      result.append("final " + prodOrDevClassName + " " + variableName + " = " + Instance.code(childXmlTag, project) + ";");
      result.append(ClickListener.valueOf(childXmlTag, variableName));
      tag2var.put(childXmlTag, variableName);
    }

    // add
    for (XmlTag child : parent.getChildren()) {
      String variableName = tag2var.get(child);
      String key = child.getKey();
      if (key != null && project.xmlSchema.getAttributes(parent.name).contains(key)) {
        result.append(ReflectionUtil.getSetterMethodName(key) + "(" + variableName + ");");
      } else {
        result.append("add(" + variableName + ");");
      }
    }

    result.append("}");
    return result.toString();
  }

}

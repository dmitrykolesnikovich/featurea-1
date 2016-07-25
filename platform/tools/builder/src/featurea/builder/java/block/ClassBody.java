package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.xml.XmlContext;
import featurea.xml.XmlTag;

import java.util.ArrayList;
import java.util.List;

public final class ClassBody {

  private final XmlContext context;

  public ClassBody(XmlContext context) {
    this.context = context;
  }

  public String code(XmlTag tag, Project project) {
    List<String> classBody = new ArrayList<>();
    classBody.add(Blocks.valueMethod.code(tag, project));
    if (project.xmlSchema != null) {
      classBody.add("{ " + "if (featurea.app.Context.isProduction()) { ");
      classBody.add(InstanceMethods.code(tag, project));
      classBody.add("}" + " }");
    }
    classBody.add(classBodyRecursively(tag, project));
    String classBodyWithName = Blocks.className.code(tag, classBody);
    return classBodyWithName;
  }

  private <T> String classBodyRecursively(XmlTag parent, Project project) {
    StringBuilder result = new StringBuilder();
    for (XmlTag xmlField : parent.xmlFields()) {
      if (!xmlField.getId().startsWith("id")) {
        result.append(code(xmlField, project));
      }
    }
    return result.toString();
  }

}

package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.xml.XmlTag;

public class ValueMethod {

  protected String code(XmlTag tag, Project project) {
    StringBuilder result = new StringBuilder();

    // >> todo simplify this shit
    String extendsClassName;
    if (tag.getLink() != null) {
      extendsClassName = tag.getLink().prodOrDevClassName();
    } else {
      extendsClassName = tag.devClassName();
    }
    // <<

    String prodSimpleClassName = tag.prodSimpleClassName();

    result.append("public static " + extendsClassName + " value() {");
    result.append("if (!featurea.app.Context.isProduction()) {");
    result.append("return featurea.app.Context.getResources().getResource(" + prodSimpleClassName + ".class);");
    result.append("} else {");
    if (!project.isProduction()) {
      result.append("return new " + prodSimpleClassName + "();");
    } else {
      result.append("return null;");
    }
    result.append("}");
    result.append("}");
    return result.toString();
  }

}

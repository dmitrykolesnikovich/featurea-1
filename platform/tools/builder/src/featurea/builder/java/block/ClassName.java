package featurea.builder.java.block;

import featurea.xml.XmlTag;

import java.util.List;

public class ClassName {

  public String code(XmlTag tag, List<String> blocks) {
    String block = "";
    for (String block1 : blocks) {
      block += block1;
    }
    String prodSimpleClassName = getClassName(tag);
    String extendsClassName;
    if (tag.getLink() != null) {
      extendsClassName = tag.getLink().prodOrDevClassName();
    } else {
      extendsClassName = tag.devClassName();
    }
    boolean isStatic = tag.getParent() != null;
    return "public " + (isStatic ? "static " : "") + "class " + prodSimpleClassName + " extends " + extendsClassName + " { " + block + " } ";
  }

  public static String getClassName(XmlTag tag) {
    return tag.prodSimpleClassName().replaceAll("-", "_");
  }

}

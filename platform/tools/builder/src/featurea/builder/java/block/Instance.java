package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.xml.XmlTag;

public final class Instance {

  private Instance() {
    // no op
  }

  public static String code(XmlTag xmlTag, Project project) {
    String prodClassName = xmlTag.prodClassName();
    if (prodClassName != null) {
      prodClassName = Hacks.hack2(prodClassName);
      return "new " + prodClassName + "()";
    } else {
      return "new " + xmlTag.devClassName() + "() { " + InstanceMethods.code(xmlTag, project) + " }";
    }
  }

}

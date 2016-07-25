package featurea.builder.java.block;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.xml.XmlTag;

public final class InstanceMethods {

  private InstanceMethods() {
    // no op
  }

  public static String code(XmlTag tag, Project project) {
    StringBuilder result = new StringBuilder();
    result.append(Setters.code(tag, project));
    result.append(new Children().code(tag, project));
    result.append("{this.build();}");
    return result.toString();
  }

}

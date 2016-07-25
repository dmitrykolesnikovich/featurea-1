package featurea.util;

import com.sun.istack.internal.Nullable;
import featurea.app.Config;
import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.xml.XmlSchema;
import featurea.xml.XmlTag;

public class AppendXmlTag extends Config {

  public AppendXmlTag(Project project) {
    super(project, "appendXmlTag.properties");
  }

  @Nullable
  public XmlTag getXmlTag(String name) {
    XmlSchema xmlSchema = project.xmlSchema;
    String target = getValue(name);
    while (target == null) {
      String superClassName = xmlSchema.getCanonicalSuperClassName(name);
      name = xmlSchema.getTagName(superClassName);
      if (name == null) {
        return null;
      }
      target = getValue(name);
    }
    XmlTag selectedXmlTag = project.resources.editor.getSelectedXmlTag();
    while (!selectedXmlTag.name.equals(target)) {
      selectedXmlTag = selectedXmlTag.getParent();
      if (selectedXmlTag == null) {
        return null;
      }
    }
    return selectedXmlTag;
  }

}

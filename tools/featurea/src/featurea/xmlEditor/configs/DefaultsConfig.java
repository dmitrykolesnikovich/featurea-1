package featurea.xmlEditor.configs;

import featurea.app.Config;
import featurea.app.Project;
import featurea.xml.XmlTag;

import java.util.List;

public class DefaultsConfig extends Config {

  public DefaultsConfig(Project project) {
    super(project, "defaults.properties");
  }

  public void inflate(XmlTag xmlTag) {
    List<String> attributes = project.xmlSchema.getAttributes(xmlTag.name);
    for (String attribute : attributes) {
      String key = xmlTag.name + "." + attribute;
      String value = getValue(key);
      if (value != null) {
        xmlTag.attributes.put(attribute, value);
      }
    }
  }

}

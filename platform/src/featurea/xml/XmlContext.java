package featurea.xml;

import com.sun.istack.internal.Nullable;
import featurea.app.XmlResources;

import java.util.LinkedHashMap;
import java.util.Map;

public class XmlContext {

  public final XmlResources resources;
  /*package*/ final Map<XmlResource, XmlTag> resource2tag = new LinkedHashMap<>();
  /*package*/ final Map<XmlTag, XmlParser.XmlTagPosition> tag2position = new LinkedHashMap<>();
  @Nullable
  public XmlTag xmlTag;
  public final String id;

  public XmlContext(XmlResources resources, String id) {
    this.resources = resources;
    this.id = id;
    try {
      xmlTag = XmlParser.parse(this);
    } catch (XmlFileNotFoundException e) {
      xmlTag = null;
    }
  }

  public XmlTag getXmlTagByResource(XmlResource resource) {
    return resource2tag.get(resource);
  }

  public Iterable<XmlTag> listTags() {
    return resource2tag.values();
  }

  public XmlTag findXmlTagById(String id) {
    return findXmlTagByIdRecursively(xmlTag, id);
  }

  private XmlTag findXmlTagByIdRecursively(XmlTag xmlTag, String id) {
    if (id.equals(xmlTag.getId())) {
      return xmlTag;
    }
    for (XmlTag childXmlTag : xmlTag.getChildren()) {
      XmlTag result = findXmlTagByIdRecursively(childXmlTag, id);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

}

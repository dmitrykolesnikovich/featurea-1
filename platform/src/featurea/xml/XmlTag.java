package featurea.xml;

import featurea.app.Config;
import featurea.app.Project;
import featurea.util.FileUtil;
import featurea.util.FormatUtil;
import featurea.util.ReflectionUtil;
import featurea.util.StringUtil;

import java.util.*;

public class XmlTag implements Cloneable {

  private final List<XmlTag> children = new ArrayList<>();
  public final Map<String, String> attributes = new LinkedHashMap<>();

  private XmlTag parent;
  public final String name;
  private final String file;
  private XmlResource resource;
  public final XmlContext context;

  public XmlTag(XmlContext context, String name, String file) {
    this.context = context;
    this.name = name;
    this.file = file;
  }

  public final void addChild(XmlTag child) {
    this.addChild(children.size(), child);
  }

  public void addChild(int index, XmlTag child) {
    children.add(index, child);
    child.parent = this;
    if (resource != null && child.resource != null) { // IMPORTANT resource not getResource()
      XmlBuilder.add(this, child, index);
    }
  }

  public void removeChild(XmlTag child) {
    children.remove(child);
    child.parent = null;

    // >> experiment todo improve this
    if (resource != null && child.resource != null) { // IMPORTANT resource not getResource()
      try {
        ReflectionUtil.invokeMethod(resource, "remove", child.resource);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    // <<
  }

  private String devClass() {
    XmlTag link = getLink();
    if (link != null) {
      return link.devClassName();
    }
    try {
      return getXmlSchema().getCanonicalClassName(name);
    } catch (NullPointerException e) {
      System.out.println("breakpoint");
      return null;
    }
  }

  public String devClassName() {
    String devClass = devClass();
    if (devClass != null) {
      return FormatUtil.formatClassName(devClass);
    } else {
      return null;
    }
  }

  public Class devClassLoad() {
    return getProject().classLoader.loadClass(devClass());
  }

  private String prodClass() {
    String id = getId();
    XmlTag link = getLink();
    if (link != null) {
      return link.prodClass();
    } else {
      if (attributes.get("id") == null ||
          id == null ||
          attributes.get("id").startsWith("id") ||
          id.contains("/id")) {
        return null;
      }
      String dottedId = id.replaceAll("/", "\\.").substring(1, id.length());
      dottedId = StringUtil.upperCaseLastLetterAfterDelimiter(dottedId, '.');
      dottedId = dottedId.replaceFirst(getResPackage(), "");
      dottedId = StringUtil.upperCaseEachLetterAfterEachDelimiter(dottedId, '.');
      dottedId = dottedId.substring(1, dottedId.length());
      try {
        dottedId = dottedId.replaceAll("\\.", "\\$");
      } catch (StringIndexOutOfBoundsException e) {
        e.printStackTrace();
      }
      String genPackage = getGenPackage();
      if (!genPackage.isEmpty()) {
        return genPackage + "." + dottedId;
      } else {
        return dottedId;
      }
    }
  }

  public String prodClassName() {
    String prodClass = prodClass();
    if (prodClass != null) {
      return FormatUtil.formatClassName(prodClass);
    } else {
      return null;
    }
  }

  public String prodSimpleClassName() {
    String id = attributes.get("id");
    if (id != null) {
      return StringUtil.upperCaseEachLetterAfterEachDelimiter(id);
    } else {
      return null;
    }
  }

  public Class prodClassLoad() {
    String prodClass = prodClass();
    if (prodClass != null) {
      return getProject().classLoader.loadClass(prodClass);
    } else {
      return null;
    }
  }

  public Class prodOrDevClassLoad() {
    Class result = prodClassLoad();
    if (result == null) {
      result = devClassLoad();
    }
    return result;
  }

  public String prodOrDevClassName() {
    String result = prodClassName();
    if (result == null) {
      result = devClassName();
    }
    return result;
  }

  public XmlTag getLink() {
    String link = attributes.get("link");
    if (link != null) {
      return getXmlTagByLink(link);
    }
    return null;
  }

  public XmlTag getXmlTagByLink(String link) {
    String id = getIdByLink(link);
    return context.resources.getTag(id); // creates new instance of XmlTag class
  }

  public String getKey() {
    return attributes.get("id");
  }

  public String getResPackage() {
    String dir = FileUtil.getDir(file);
    return dir.replaceAll("/", ".");
  }

  public String getGenPackage() {
    String result;
    String resPackage = getResPackage();
    int index = resPackage.indexOf('.');
    if (index != -1) {
      String token1 = resPackage.substring(0, index);
      String token2 = resPackage.substring(index + 1, resPackage.length());
      result = token1 + ".res." + token2;
    } else {
      result = resPackage + ".res";
    }
    return result;
  }

  public String getRPackage() {
    String result;
    String resPackage = getResPackage();
    int index = resPackage.indexOf('.');
    if (index != -1) {
      result = resPackage.substring(0, index);
    } else {
      result = resPackage;
    }
    return result;
  }

  public String getIdByLink(String link) {
    String id = getId();
    // >> just for dev featurea todo fix this shit
    if (id == null) {
      return link;
    }
    // <<
    if (link.startsWith("/")) {
      return link;
    }
    int slashLastIndex = id.lastIndexOf("/");
    String parentId = id.substring(0, slashLastIndex);
    String[] tokens = link.split("/");
    int startIndex = 0;
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if ("..".equals(token)) {
        int lastIndex = parentId.lastIndexOf("/");
        parentId = parentId.substring(0, lastIndex);
        startIndex++;
      }
    }
    for (int i = startIndex; i < tokens.length; i++) {
      parentId += "/" + tokens[i];
    }
    return parentId;
  }

  public List<XmlTag> xmlFields() {
    List<XmlTag> result = new ArrayList<>();
    inflateXmlFieldsRecursively(result, this);
    return result;
  }

  private void inflateXmlFieldsRecursively(List<XmlTag> result, XmlTag tag) {
    XmlTag link = tag.getLink();
    if (link != null) {
      inflateXmlFieldsRecursively(result, link);
    }
    for (XmlTag child : tag.children) {
      if (child.getKey() != null) {
        result.add(child);
      }
    }
  }

  @Override
  public String toString() {
    return getId();
  }

  public String getId() {
    String result = "";
    if (parent != null) {
      result += parent.getId();
    } else {
      String dir = FileUtil.getDir(file);
      result += "/" + dir;
    }
    String myId = getKey();
    if (myId == null) {
      if (parent != null) {
        myId = "id" + parent.children.indexOf(this);
      } else {
        return result + "/idNotFound";
      }
    }
    result += "/" + myId;
    return result;
  }

  public String getFile() {
    return file;
  }

  public <T extends XmlResource> T getResource() {
    if (resource == null) {
      try {
        XmlBuilder xmlBuilder = new XmlBuilder();
        xmlBuilder.initRecursively(this);
        xmlBuilder.buildRecursively(this);
        xmlBuilder = null;
      } catch (XmlFileBuildException e) {
        XmlFileBuildException.log(e);
      }
    }
    return (T) resource;
  }

  public void setResource(XmlResource resource) {
    context.resource2tag.put(resource, this);
    this.resource = resource;
  }

  public XmlTag getRoot() {
    if (parent == null) {
      return this;
    } else {
      return parent.getRoot();
    }
  }

  /*Primitive API*/

  public void setAttribute(String key, String value) {
    setAttributeAndBuild(key, value);
    if (context.resources.editor != null) {
      context.resources.editor.editAttribute(key, value);
    }
  }

  /*private*/ void setAttributeAndBuild(String key, String value) {
    attributes.put(key, value);
    if (resource != null) {
      try {
        ReflectionUtil.invokeMethod(resource, ReflectionUtil.getSetterMethodName(key), value);
        resource.build(); // IMPORTANT
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  public String getAttribute(String key) {
    return attributes.get(key);
  }

  public Iterable<Map.Entry<String, String>> getAttributes() {
    // 1)
    List<Map.Entry<String, String>> list = new ArrayList<>();
    for (String key : getXmlSchema().getAttributes(name)) {
      if (attributes.containsKey(key)) {
        list.add(new MapEntry(key, attributes.get(key)));
      }
    }
    // >> just for test todo delete
    if (list.size() != new HashSet<>(list).size()) {
      System.out.println("breakpoint");
    }
    list.add(0, new MapEntry("id", attributes.get("id")));
    list.add(1, new MapEntry("link", attributes.get("link")));
    // <<
    return list;
    // 2)
    /*return attributes.entrySet();*/
  }

  public int depth() {
    int count = 0;
    XmlTag xmlTag = this;
    while (xmlTag.parent != null) {
      count++;
      xmlTag = xmlTag.parent;
    }
    return count;
  }

  public XmlTag getParent() {
    return parent;
  }

  public List<XmlTag> getChildren() {
    return children;
  }

  public int indexOf(XmlTag child) {
    return children.indexOf(child);
  }

  public static List<String> retrieveAreaTagsFromSchema(XmlSchema xmlSchema) {
    List<String> result = new ArrayList<>();
    retrieveAreaTagsRecursively(result, xmlSchema);
    return result;
  }

  private static void retrieveAreaTagsRecursively(List<String> result, Config config) {
    for (String key : config.properties.keySet()) {
      try {
        if (!key.contains(".") && config.getValue(key + ".position") != null) {
          result.add(key);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static class MapEntry implements Map.Entry<String, String> {

    private final String key;
    private final String value;

    public MapEntry(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public String setValue(String value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof MapEntry) {
        MapEntry mapEntry = (MapEntry) obj;
        return mapEntry.getKey().equals(getKey());
      }
      return false;
    }

  }

  @Override
  public XmlTag clone() {
    // todo improve this shit
    XmlTag xmlTag = new XmlTag(context, name, file);
    xmlTag.attributes.putAll(attributes);
    xmlTag.children.addAll(children);
    xmlTag.parent = parent;
    xmlTag.getResource(); // IMPORTANT
    return xmlTag;
    // <<
  }

  public XmlSchema getXmlSchema() {
    return getProject().xmlSchema;
  }

  public Project getProject() {
    return context.resources.project;
  }

}

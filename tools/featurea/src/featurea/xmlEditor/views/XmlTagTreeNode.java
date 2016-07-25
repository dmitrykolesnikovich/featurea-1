package featurea.xmlEditor.views;

import featurea.xml.XmlTag;

import javax.swing.tree.DefaultMutableTreeNode;

public class XmlTagTreeNode extends DefaultMutableTreeNode {

  public XmlTagTreeNode(Object userObject) {
    super(userObject);
  }

  @Override
  public String toString() {
    XmlTag xmlTag = (XmlTag) getUserObject();
    String key = xmlTag.getKey();
    String name = xmlTag.name;
    if (name == null) {
      throw new IllegalStateException("xmlTag.name == null");
    }
    String result = name;
    if (key != null) {
      result = result + " (" + key + ")";
    }
    return result;
  }

}

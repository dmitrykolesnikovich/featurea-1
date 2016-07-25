package featurea.xml;

import featurea.app.Context;

public class XmlFileBuildException extends RuntimeException {

  private final XmlTag xmlTag;
  private final String message;

  public XmlFileBuildException(XmlTag xmlTag, String message) {
    this.xmlTag = xmlTag;
    this.message = message;
  }

  public static void log(XmlFileBuildException xmlFileBuildException) {
    XmlTag xmlTag = xmlFileBuildException.xmlTag;
    String message = xmlFileBuildException.message;
    XmlParser.XmlTagPosition xmlTagPosition = xmlTag.context.tag2position.get(xmlTag);
    System.err.println(xmlTagPosition.file + ": " + xmlTagPosition.position + " (" + message + ")");
  }

}


package featurea.xml;

import com.sun.istack.internal.Nullable;
import featurea.app.Project;
import featurea.util.StringUtil;
import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class XmlParser {

  private final static String LINE_NUMBER = "lineNumber";
  private final XmlContext xmlContext;

  private XmlParser(XmlContext xmlContext) {
    this.xmlContext = xmlContext;
  }

  public static XmlTag parse(XmlContext xmlContext) throws XmlFileNotFoundException {
    XmlTag rootXmlTag = new XmlParser(xmlContext).performParse(xmlContext.id);
    if (rootXmlTag == null) {
      return null;
    }
    if (rootXmlTag.getId().equals("/" + rootXmlTag.getFile().replaceAll(".xml", ""))) {
      return rootXmlTag;
    } else {
      XmlFileBuildException.log(new XmlFileBuildException(rootXmlTag, "Invalid root tag id: " + rootXmlTag.getId()));
      return null;
    }
  }

  @Nullable
  private XmlTag performParse(String id) throws XmlFileNotFoundException {
    id = StringUtil.lowerCaseEachLetterAfterEachDelimiter(id, '/');
    String[] ids = StringUtil.split(id, "/");
    for (int i = 0; i < ids.length; i++) {
      int j = 1;
      String filePath = ids[0];
      while (j <= i) {
        filePath += "/" + ids[j];
        j++;
      }
      filePath += ".xml";
      if (getProject().getFiles().exists(filePath)) {
        List<String> idsParam = new ArrayList<>();
        for (int k = i; k < ids.length; k++) {
          idsParam.add(ids[k]);
        }
        return performParse(filePath, idsParam.toArray(new String[idsParam.size()]));
      }
    }
    return null;
  }

  private XmlTag performParse(String file, String... ids) throws XmlFileNotFoundException {
    XmlTag xmlTag = null;
    InputStream inputStream = null;
    try {
      inputStream = getProject().getFiles().getStream(file);
      if (inputStream != null) {
        Document document = readXML(inputStream);
        Element rootElement = document.getDocumentElement();
        Element startElement = getStartElementElement(rootElement, 1, ids);
        if (startElement != null) {
          xmlTag = new XmlTag(xmlContext, startElement.getTagName(), file);
          addXmlTagToContext(xmlTag, file, startElement);
          parseNodeRecursively(xmlTag, startElement, file);
        }
      }
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new XmlFileNotFoundException(e);
    }
    return xmlTag;
  }


  private void parseNodeRecursively(XmlTag parentTag, Node parentNode, String file) {
    NamedNodeMap nodeMap = parentNode.getAttributes();
    for (int i = 0; i < nodeMap.getLength(); i++) {
      Node attribute = nodeMap.item(i);
      String key = attribute.getNodeName();
      String value = attribute.getNodeValue();
      parentTag.attributes.put(key, value);
    }
    XmlTag currentTag;
    NodeList nodes = parentNode.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node childNode = nodes.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        currentTag = new XmlTag(xmlContext, childNode.getNodeName(), file);
        parentTag.addChild(currentTag);
        addXmlTagToContext(currentTag, file, (Element) childNode);
        parseNodeRecursively(currentTag, childNode, file);
      }
    }
  }

  public static Document readXML(final InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
    final Document document;
    SAXParser parser;
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    parser = factory.newSAXParser();
    final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    document = docBuilder.newDocument();
    final Stack<Element> elementStack = new Stack<Element>();
    final StringBuilder textBuffer = new StringBuilder();
    final DefaultHandler handler = new DefaultHandler() {
      private Locator locator;

      @Override
      public void setDocumentLocator(final Locator locator) {
        this.locator = locator;
      }

      @Override
      public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
          throws SAXException {
        addTextIfNeeded();
        final Element el = document.createElement(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
          el.setAttribute(attributes.getQName(i), attributes.getValue(i));
        }
        el.setUserData(LINE_NUMBER, this.locator.getLineNumber(), null);
        elementStack.push(el);
      }

      @Override
      public void endElement(final String uri, final String localName, final String qName) {
        addTextIfNeeded();
        final Element closedEl = elementStack.pop();
        if (elementStack.isEmpty()) {
          document.appendChild(closedEl);
        } else {
          final Element parentEl = elementStack.peek();
          parentEl.appendChild(closedEl);
        }
      }

      @Override
      public void characters(final char ch[], final int start, final int length) throws SAXException {
        textBuffer.append(ch, start, length);
      }

      private void addTextIfNeeded() {
        if (textBuffer.length() > 0) {
          final Element el = elementStack.peek();
          final Node textNode = document.createTextNode(textBuffer.toString());
          el.appendChild(textNode);
          textBuffer.delete(0, textBuffer.length());
        }
      }
    };
    parser.parse(inputStream, handler);
    return document;
  }

  public static class XmlTagPosition {

    public final File file;
    public final int position;

    public XmlTagPosition(File file, int position) {
      this.file = file;
      this.position = position;
    }

    @Override
    public String toString() {
      return file + ": " + position;
    }

  }

  private void addXmlTagToContext(XmlTag xmlTag, String file, Element element) {
    xmlContext.tag2position.put(xmlTag, new XmlTagPosition(getProject().getFiles().findFile(file), (Integer) element.getUserData(LINE_NUMBER)));
  }

  private Element getStartElementElement(Element parent, int index, String[] ids) {
    if (index >= ids.length) {
      return parent;
    }
    String id = ids[index];
    int intId = -1;
    try {
      if (id.startsWith("id")) {
        intId = Integer.valueOf(id.replaceFirst("id", ""));
      }
    } catch (NumberFormatException e) {
      System.err.println("[XmlParser] NumberFormatException: " + e.getMessage());
    }
    NodeList children = parent.getChildNodes();
    if (intId != -1) {
      if (intId < ids.length) {
        Element childElement = null;
        for (int i = 0; i < children.getLength() && intId >= 0; i++) {
          Node child = children.item(i);
          if (child.getNodeType() == Node.ELEMENT_NODE) {
            childElement = (Element) child;
            intId--;
          }
        }
        return getStartElementElement(childElement, index + 1, ids);
      }
    } else {
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        if (child.getNodeType() == Node.ELEMENT_NODE) {
          Element childElement = (Element) child;
          if (childElement.getAttribute("id").equals(id)) {
            return getStartElementElement(childElement, index + 1, ids);
          }
        }
      }
    }

    return null;
  }

  private String formatId(String childId) {
    return childId.replaceAll("-", "_");
  }

  private Project getProject() {
    return xmlContext.resources.project;
  }

}

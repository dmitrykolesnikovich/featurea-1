package featurea.xml;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import featurea.app.Layer;
import featurea.app.Screen;
import featurea.util.FileUtil;
import featurea.util.StringUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

public final class XmlUtil {

  private static final int indent = 2;
  private final Document document;

  private XmlUtil() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    document = builder.newDocument();
  }

  public static void save(XmlTag xmlTag, File file) {
    new XmlUtil().performSave(xmlTag, file);
  }

  private void performSave(XmlTag xmlTag, File file) {
    try {
      Element rootElement = retrieveElementFromXmlTag(xmlTag);
      document.appendChild(rootElement);
      Writer writer = new FileWriter(file);
      OutputFormat format = new OutputFormat(document);
      format.setIndenting(true);
      format.setIndent(indent);
      format.setOmitXMLDeclaration(true);
      XMLSerializer serializer = new XMLSerializer(writer, format);
      serializer.serialize(document);
      String text = FileUtil.getText(file);
      text = text.replaceAll("&#xa;", "\n");
      text = text.replaceAll("&#x9;", "\t");
      FileUtil.write(text, file);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private Element retrieveElementFromXmlTag(XmlTag xmlTag) {
    try {
      Element element = document.createElement(xmlTag.name);
      for (Map.Entry<String, String> attribute : xmlTag.getAttributes()) {
        String key = attribute.getKey();
        String value = attribute.getValue();
        if (value != null && !value.isEmpty()) {
          value = xmlTag.context.resources.project.xmlFormatter.format(xmlTag, key, value);
          value = inflateSpaces(value, xmlTag, key);
          element.setAttribute(key, value);
        }
      }
      for (XmlTag child : xmlTag.getChildren()) {
        element.appendChild(retrieveElementFromXmlTag(child));
      }
      return element;
    } catch (DOMException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Layer getLayer(XmlTag xmlTag, Screen screen) {
    if (xmlTag != null) {
      if (screen != null) {
        for (Layer layer : screen.getLayers()) {
          if (layer == xmlTag.getResource()) {
            return layer;
          } else if (layer.listAreas().contains(xmlTag.getResource())) {
            return layer;
          }
        }
      }
    }
    return null;
  }

  public static void setAttribute(XmlTag xmlTag, String key, String value) {
    xmlTag.setAttributeAndBuild(key, value);
  }

  private String inflateSpaces(String value, XmlTag xmlTag, String key) {
    String result = "";
    String[] tokens = StringUtil.split(value, "\n");
    for (int i = 0; i < tokens.length; i++) {
      String tokenValue = tokens[i];
      if (i != 0) {
        for (int j = 0; j < key.length() + 2; j++) {
          tokenValue = " " + tokenValue;
        }
        for (int j = 0; j < xmlTag.depth() + 1; j++) {
          tokenValue = "\t" + tokenValue;
        }
        tokenValue = "\n" + tokenValue;
      }
      result += tokenValue;
    }
    return result;
  }


}

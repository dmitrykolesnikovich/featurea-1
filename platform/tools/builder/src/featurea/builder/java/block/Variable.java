package featurea.builder.java.block;

import featurea.xml.XmlTag;

import java.util.HashMap;
import java.util.Map;

public final class Variable {

  private Variable() {
    // no op
  }

  private static int counter;
  private static final Map<XmlTag, String> map = new HashMap<>();

  public static String nextName(XmlTag xmlTag) {
    String result = "var" + counter++;
    map.put(xmlTag, result);
    return result;
  }

  public static String getVar(XmlTag xmlTag) {
    return map.get(xmlTag);
  }

}

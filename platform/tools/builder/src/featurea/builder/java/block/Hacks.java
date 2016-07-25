package featurea.builder.java.block;

import featurea.xml.XmlTag;

public final class Hacks {

  private Hacks() {
    // no op
  }

  public static String hack1(XmlTag tag, String prodClassName) {
    if (prodClassName.startsWith(tag.getGenPackage())) {
      prodClassName = prodClassName.replaceFirst(tag.getGenPackage() + ".", "");
    }
    return prodClassName;
  }

  public static String hack2(String fieldName) {
    return fieldName.replaceAll("-", "_");
  }

}

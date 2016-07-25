package featurea.util;

import featurea.xml.XmlResource;

public class ArrayList<T> extends java.util.ArrayList<T> implements XmlResource {

  @Override
  public ArrayList build() {
    return this;
  }

}

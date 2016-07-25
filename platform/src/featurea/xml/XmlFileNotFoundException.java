package featurea.xml;

public class XmlFileNotFoundException extends Exception {

  public XmlFileNotFoundException() {
    super();
  }

  public XmlFileNotFoundException(String message) {
    super(message);
  }

  public XmlFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlFileNotFoundException(Throwable cause) {
    super(cause);
  }

  protected XmlFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

package featurea.builder.java.block;

public final class PackageXmlWrap {

  private PackageXmlWrap() {
    // no op
  }

  public static String code(String packageName, String block) {
    return "package " + packageName + "; " + block;
  }

}

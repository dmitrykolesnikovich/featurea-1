package featurea.builder.java.block;

public final class PackageWrap {

  public String code(String resPackage, String block) {
    return "package " + resPackage + "; " + block;
  }

}

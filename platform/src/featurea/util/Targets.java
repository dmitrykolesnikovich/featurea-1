package featurea.util;

public interface Targets {

  boolean isAndroid = "android runtime".equals(System.getProperty("java.runtime.name").toLowerCase());
  boolean isLinux = System.getProperty("os.name").contains("Linux") && !isAndroid;
  boolean isWindows = System.getProperty("os.name").contains("Windows");
  boolean isMac = System.getProperty("os.name").contains("Mac");
  boolean isDesktop = isWindows || isLinux || isMac;
  boolean isIos = !isAndroid && !isWindows && !isLinux && !isMac;
  boolean isMobile = isAndroid || isIos;
  boolean is64Bit = System.getProperty("os.arch").equals("amd64");
  /*boolean isIntelliJ = System.getProperty("idea.platform.prefix") != null;*/
  boolean isFeaturea = System.getProperty("featurea.platform.prefix") != null;
  String name = isAndroid ? "android" : isIos ? "ios" : isWindows ? "windows" : isLinux ? "linux" : isMac ? "macosx" : null;

}

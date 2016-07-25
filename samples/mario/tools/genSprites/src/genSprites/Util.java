package genSprites;

import featurea.util.StringUtil;

public class Util {

  public static String getPropertyName(String string) {
    String[] tokens = string.split("\\.");
    if (tokens.length == 0) {
      return string;
    }
    if (tokens.length == 1) {
      return getPropertyNameSingle(tokens[0]);
    } else {
      String result = "";
      for (String token : tokens) {
        result += getPropertyNameSingle(token) + ".";
      }
      return result;
    }
  }

  private static String getPropertyNameSingle(String string) {
    if (string.isEmpty()) {
      return string;
    }
    String result = string;
    if (Character.isDigit(string.charAt(0))) {
      result = "_" + result;
    }
    result = result.replaceAll(" ", "_");
    return result;
  }

}

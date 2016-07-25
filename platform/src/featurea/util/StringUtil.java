package featurea.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
  private final String value;
  private final String delimiter;

  public StringUtil(String value, String delimiter) {
    this.value = value;
    this.delimiter = delimiter;
  }

  public static String[] split(String string, String delimiter) {
    if (string == null || string.isEmpty()) {
      return new String[]{};
    }
    List<String> result = null;
    try {
      String[] tokens = string.trim().split(delimiter);
      if (tokens.length == 0) {
        return new String[]{string};
      }
      result = new ArrayList<>();
      for (int i = 0; i < tokens.length; i++) {
        String token = tokens[i].trim();
        if (!token.isEmpty()) {
          result.add(token);
        }
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return result.toArray(new String[result.size()]);
  }

  public static String uppercaseFirstLetter(String key) {
    return (key.toUpperCase().charAt(0) + key.substring(1, key.length())).trim();
  }

  public static String lowerFirstLetter(String key) {
    return (key.toLowerCase().charAt(0) + key.substring(1, key.length())).trim();
  }

  public String lastToken(int count) throws ArrayIndexOutOfBoundsException {
    String result = "";
    String[] tokens = value.split(delimiter);
    for (int i = tokens.length - count; i < tokens.length; i++) {
      result += tokens[i] + delimiter;
    }
    if (!result.isEmpty()) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  public String first(int count) {
    String result = "";
    String[] tokens = value.split(delimiter);
    for (int i = 0; i < count; i++) {
      result += tokens[i] + delimiter;
    }
    if (!result.isEmpty()) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  public static String upperCaseLastLetterAfterDelimiter(String result, char delimiter) {
    int index = result.lastIndexOf(delimiter);
    if (index != -1) {
      String token1 = result.substring(0, index + 1);
      String token2 = result.substring(index + 1, result.length());
      token2 = (token2.charAt(0) + "").toUpperCase() + token2.substring(1, token2.length());
      result = token1 + token2;
    }
    return result;
  }

  public static String upperCaseEachLetterAfterEachDelimiter(String result, char... delimiters) {
    if (delimiters.length == 0) {
      return upperCaseLetterAfterDelimiter(result, -1);
    }
    for (char delimiter : delimiters) {
      result = upperCaseEachLetterAfterDelimiter(result, delimiter);
    }
    return result;
  }

  private static String upperCaseEachLetterAfterDelimiter(String result, char delimiter) {
    if (result.contains(delimiter + "")) {
      char[] chars = result.toCharArray();
      for (int index = 0; index < chars.length - 1; index++) {
        char ch = chars[index];
        if (ch == delimiter) {
          result = upperCaseLetterAfterDelimiter(result, index);
        }
      }
      return result;
    } else {
      return upperCaseLetterAfterDelimiter(result, -1);
    }
  }

  private static String upperCaseLetterAfterDelimiter(String result, int index) {
    result = result.substring(0, index + 1) + (result.charAt(index + 1) + "").toUpperCase() +
        (index + 2 <= result.length() ? result.substring(index + 2, result.length()) : "");
    return result;
  }

  public static String lowerCaseEachLetterAfterEachDelimiter(String result, char... delimiters) {
    if (delimiters.length == 0) {
      return lowerCaseLetterAfterDelimiter(result, -1);
    }
    for (char delimiter : delimiters) {
      result = lowerCaseEachLetterAfterDelimiter(result, delimiter);
    }
    return result;
  }

  private static String lowerCaseEachLetterAfterDelimiter(String result, char delimiter) {
    if (result.contains(delimiter + "")) {
      char[] chars = result.toCharArray();
      for (int index = 0; index < chars.length - 1; index++) {
        char ch = chars[index];
        if (ch == delimiter) {
          result = lowerCaseLetterAfterDelimiter(result, index);
        }
      }
      return result;
    } else {
      return lowerCaseLetterAfterDelimiter(result, -1);
    }
  }

  private static String lowerCaseLetterAfterDelimiter(String result, int index) {
    result = result.substring(0, index + 1) + (result.charAt(index + 1) + "").toLowerCase() +
        (index + 2 <= result.length() ? result.substring(index + 2, result.length()) : "");
    return result;
  }

  public static List<File> files(String value, String delimiter) {
    List<File> result = new ArrayList<>();
    String[] tokens = split(value, delimiter);
    for (String token : tokens) {
      result.add(new File(token));
    }
    return result;
  }

}

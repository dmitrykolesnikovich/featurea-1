package featurea.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ArrayUtil {

  private ArrayUtil() {
    // no op
  }

  public static String[] split(String string) {
    return StringUtil.split(string, ",");
  }

  public static String toString(String[] values) {
    String result = "";
    for (String value : values) {
      result += value + ", ";
    }
    result = result.substring(0, result.length() - 2);
    return result;
  }

  private static String stringOf(Object value) {
    if (value instanceof File) {
      File file = (File) value;
      return FileUtil.formatPath(file.getAbsolutePath());
    }
    return value.toString();
  }


  public static String toString(double[] array) {
    String result = "";
    for (double value : array) {
      result += value + ", ";
    }
    result = result.substring(0, result.length() - 2);
    return result;
  }

  public static String toString(int[] array) {
    String result = "";
    for (int value : array) {
      result += value + ", ";
    }
    result = result.substring(0, result.length() - 2);
    return result;
  }

  public static String toString(int[][] array) {
    String result = "";
    for (int[] element1 : array) {
      for (int element2 : element1) {
        result += element2 + ", ";
      }
    }
    result = result.substring(0, result.length() - 2);
    return result;
  }

  public static int indexOf(Object[] array, Object element) {
    for (int i = 0; i < array.length; i++) {
      if (element == array[i]) {
        return i;
      }
    }
    return -1;
  }

  public static int[][] twoDimensions(String[] array, int size, int startIndex, int finishIndex) {
    int[] intArray = new int[finishIndex - startIndex + 1];
    for (int i = startIndex; i <= finishIndex; i++) {
      intArray[i - startIndex] = Integer.valueOf(array[i]);
    }
    return twoDimensions(intArray, size, 0, intArray.length - 1);
  }

  public static int[][] twoDimensions(int[] array, int size, int startIndex, int finishIndex) {
    int[][] result = new int[(finishIndex - startIndex + 1) / size][size];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = array[startIndex + i * result[i].length + j];
      }
    }
    return result;
  }

  public static int[][] twoDimensions(int[] array, int size) {
    return twoDimensions(array, size, 0, array.length - 1);
  }

  public static int[][] remove(int[][] array, List<int[]> whatToRemove) {
    List<int[]> result = new ArrayList<>();
    for (int[] element : array) {
      result.add(element);
    }
    for (int[] chunk : array) {
      for (int[] selectedChunk : whatToRemove) {
        if (equals(selectedChunk, chunk)) {
          result.remove(chunk);
          break;
        }
      }
    }
    return result.toArray(new int[result.size()][]);
  }

  private static boolean equals(int[] array1, int[] array2) {
    if (array1.length != array2.length) {
      return false;
    }
    for (int i = 0; i < array1.length; i++) {
      if (array1[i] != array2[i]) {
        return false;
      }
    }
    return true;
  }

  public static String[] subArray(String[] array, int startIndex, int finishIndex) {
    String[] result = new String[finishIndex - startIndex + 1];
    System.arraycopy(array, startIndex, result, 0, result.length);
    return result;
  }

  public static String[] add(String[] array, String item) {
    return ArrayUtil.add(array, item, array.length);
  }

  public static String[] add(String[] array, String item, int index) {
    List<String> result = new ArrayList<>();
    for (int i = 0; i < array.length; i++) {
      result.add(array[i]);
    }
    result.add(index, item);
    return result.toArray(new String[result.size()]);
  }

}

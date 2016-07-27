package tests;

// https://coderanch.com/t/384661/java/java/find-physical-path-current-java
public class CurrentPhysicalPath extends java.io.File {
  CurrentPhysicalPath() {
    super(".");//
  }

  String getResult() {
    return toString();//
  }

  public static void main(String[] args) {
    CurrentPhysicalPath cpp = new CurrentPhysicalPath();//
    System.out.println(cpp.getResult());
  }
}
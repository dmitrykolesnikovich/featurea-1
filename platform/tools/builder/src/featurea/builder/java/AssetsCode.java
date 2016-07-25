package featurea.builder.java;

import featurea.app.Project;
import featurea.builder.java.block.PackageWrap;
import featurea.util.FileUtil;
import featurea.util.Files;
import featurea.util.StringUtil;

import java.io.File;

public class AssetsCode {

  private final Project project;
  private final Files assets;

  public AssetsCode(Project project) {
    this.project = project;
    this.assets = new Files().add(project.resourcesDir);
  }

  public void performGen() {
    File genDir = project.generatedFilesDir;
    String projectPackage = project.pakage;
    String classBody = new PackageWrap().code(projectPackage, code());
    FileUtil.write(classBody, new File(genDir, projectPackage.replaceAll("\\.", "/") + "/Assets.java"));
  }

  private String code() {
    StringBuffer result = new StringBuffer();
    result.append("public interface Assets {");
    genFileRecursively(result, project.resourcesDir);
    result.append("}");
    return result.toString();
  }

  private void genFileRecursively(StringBuffer javaCode, File dir) {
    String file = dir.getAbsolutePath();
    file = FileUtil.formatPath(file);
    if (dir.isDirectory()) {
      javaCode.append(createDirStringField(file));
      if (dir != project.resourcesDir) {
        javaCode.append(createInterface(file));
      }
      File[] children = dir.listFiles();
      if (children != null) {
        for (File child : children) {
          genFileRecursively(javaCode, child);
        }
      }
      if (dir != project.resourcesDir) {
        javaCode.append("}");
      }
    } else if (dir.isFile()) {
      if (file.endsWith(".fnt")) {
        javaCode.append(createFontField(file));
      } else {
        if (isPngFntFile(file)) {
        } else {
          if (file.endsWith(".png") || file.endsWith(".mp3")) {
            javaCode.append(createStringField(file));
          }
        }
      }
    }
  }

  private String createInterface(String file) {
    return "interface " + StringUtil.upperCaseEachLetterAfterEachDelimiter(FileUtil.getName(file)) + " {";
  }

  private boolean isPngFntFile(String pngPath) {
    if (pngPath.endsWith(".png")) {
      String fntPath = pngPath.replaceAll(".png", ".fnt");
      if (new File(fntPath).exists()) {
        return true;
      }
    }
    return false;
  }

  private String createStringField(String file) {
    file = relativePath(file);
    String fileName = FileUtil.getNameWithoutExtension(file);
    fileName = getPropertyName(fileName);
    String fieldCode = "\tString " + fileName + " = \"" + file + "\";";
    return fieldCode;
  }

  private String createDirStringField(String file) {
    file = relativePath(file);
    String fieldName;
    if (!file.isEmpty()) {
      String fileName = FileUtil.getNameWithoutExtension(file);
      fieldName = getPropertyName(fileName);
      String fieldCode = "\tString " + fieldName + " = \"" + file + "\";";
      return fieldCode;
    }
    return "";
  }

  private String createFontField(String file) {
    file = relativePath(file);
    String fileName = FileUtil.getNameWithoutExtension(file);
    fileName = getPropertyName(fileName);
    String fieldCode = "\tString " + fileName + " = \"" + file + "\";";
    return fieldCode;
  }

  private String relativePath(String file) {
    return assets.getRelativePath(file);
  }

  private String getPropertyName(String fileName) {
    String result = fileName;
    if (Character.isDigit(fileName.charAt(0))) {
      result = "_" + result;
    }
    result = result.replaceAll(" ", "_");
    if ("default".equals(result)) {
      result = "_default";
    }
    return result;
  }

}

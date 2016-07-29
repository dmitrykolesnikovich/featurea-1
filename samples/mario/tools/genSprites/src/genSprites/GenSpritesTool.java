package genSprites;

import featurea.app.Project;
import featurea.swing.FeatureaSwingUtil;
import featurea.util.FileUtil;
import featurea.util.Size;
import featurea.util.StringUtil;

import java.io.File;
import java.util.*;

/**
 * Example how to use: GenSpritesTool.main(E:\featurea\samples\mario\module.properties overworld castle underground underwater night)
 */
public class GenSpritesTool {

  // >> TODO somehow avoid this constants
  private static final String OVERWORLD = "overworld";
  private static final String UNDERGROUND = "underground";
  private static final String CASTLE = "castle";
  // <<

  private final Map<String, Map<MethodNode, Set<String>>> map = new HashMap<>();
  private final ClassNode rootNode = new ClassNode("Sprites");
  private final Project project;

  public GenSpritesTool(Project project) {
    this.project = project;
  }

  public static void main(String[] args) {
    String manifest = args[0];
    Project project = new Project();
    project.setFile(new File(manifest));
    List<String> list = new ArrayList<>();
    for (int i = 1; i < args.length; i++) {
      list.add(args[i]);
    }
    GenSpritesTool.gen(project, list);
    GenThemeTool.gen(project, list);
    WorldsCode.gen(project, list);
    CollisionResolverAdapterCode.gen(project);
    WorldFireworkCode.gen(project);
  }

  public static void gen(Project project, List<String> enumConstants) {
    new GenSpritesTool(project).performGen(enumConstants);
  }

  private void performGen(List<String> enumConstants) {

    List<String> files = project.getFiles().listFilesRecursively("", ".png");

    for (String themeName : enumConstants) {
      for (String file : files) {
        int index;
        while ((index = file.indexOf("/" + themeName + "/")) != -1) {
          String classNode = file.substring(0, index);
          String method = file.substring(index + themeName.length() + 2, file.length()).replaceAll(".png", "");
          Map<MethodNode, Set<String>> classNodeMethods = resolveClassNode(classNode);
          Set<String> cases = resolveCases(classNodeMethods, method, project.getFiles().findFile(file));
          cases.add(themeName);
          file = FileUtil.getDir(file);
        }
      }
    }

    for (Map.Entry<String, Map<MethodNode, Set<String>>> entry : map.entrySet()) {
      String className = entry.getKey();
      ClassNode classNode = retrieveClassNode(className, rootNode);
      for (Map.Entry<MethodNode, Set<String>> entries : entry.getValue().entrySet()) {
        MethodNode methodNode = entries.getKey();
        String fullPath = methodNode.name;
        String classNamePart = FileUtil.getDir(fullPath);
        String methodNamePart = FileUtil.getName(fullPath);
        ClassNode childNode = retrieveClassNode(classNamePart, classNode);
        childNode.methods.put(new MethodNode(methodNamePart, methodNode.file), entries.getValue());
      }
    }
    String code = buildCode(rootNode);

    File javaFile = new File(project.generatedFilesDir.getAbsolutePath(), project.pakage + "/Sprites.java");
    FileUtil.write(code, javaFile);
  }

  private String buildCode(ClassNode parent) {
    StringBuilder builder = new StringBuilder();
    if (rootNode == parent) {
      builder.append("package " + project.pakage + ";");
    }
    builder.append("public " + (rootNode != parent ? "static" : "") + " class " + upperCaseFirstLetter(Util.getPropertyName(parent.name)) + " {");
    if (rootNode == parent) {
      builder.append("public static Theme theme = Theme.overworld;");
    }

    for (Map.Entry<MethodNode, Set<String>> methodName : parent.methods.entrySet()) {
      builder.append(buildMethod(parent, methodName));
    }

    for (ClassNode child : parent.children) {
      builder.append(buildCode(child));
    }

    builder.append("}");
    return builder.toString();
  }

  private String buildMethod(ClassNode classNode, Map.Entry<MethodNode, Set<String>> methodDescription) {
    StringBuilder builder = new StringBuilder();

    MethodNode methodNode = methodDescription.getKey();
    Set<String> casesSet = methodDescription.getValue();
    List<String> cases = new ArrayList<>(casesSet);
    Collections.sort(cases, new Comparator<String>() {
      @Override
      public int compare(String case1, String case2) {
        if (case1.equals(case2)) {
          return 0;
        }
        if (OVERWORLD.equals(case1)) {
          return 1;
        }
        if (OVERWORLD.equals(case2)) {
          return -1;
        }
        return case1.compareTo(case2);
      }
    });

    // >> texture size
    Size size = getSize(methodNode.file);
    if (size != null) {
      builder.append("public static double " + methodNode.name + "Width = " + size.width + ";");
      builder.append("public static double " + methodNode.name + "Height = " + size.height + ";");
    }
    // <<

    builder.append("public static String " + methodNode.name + "(){ switch(theme) {");

    String fullName = classNode.getFullName();
    int index = fullName.indexOf("/");
    String restName = ".";
    String rootName = fullName;
    if (index != -1) {
      rootName = fullName.substring(0, index);
      restName = fullName.substring(index + 1, fullName.length());
      restName = restName.replaceAll("/", ".");
      restName = "." + restName + ".";
      restName = StringUtil.upperCaseEachLetterAfterEachDelimiter(restName, '.');
    }
    restName = Util.getPropertyName(restName);

    rootName = upperCaseFirstLetter(rootName);

    boolean hasDefault = false;

    for (String oneCase : cases) {
      String caseCode = project.pakage + ".Assets." + rootName + "." + upperCaseFirstLetter(oneCase) + restName + methodNode.name + ";";
      if (OVERWORLD.equals(oneCase) || cases.size() == 1) {
        hasDefault = true;
        builder.append("default: return " + caseCode);
      } else {
        if (UNDERGROUND.equals(oneCase) && !cases.contains(CASTLE)) {
          builder.append("case " + CASTLE + ": ");
        }
        builder.append("case " + oneCase + ": return " + caseCode);
      }
    }
    if (!hasDefault) {
      builder.append("default: return null;");
    }
    builder.append("}}");
    return builder.toString();
  }

  private Size getSize(File file) {
    if (file.isFile()) {
      if (file.getName().endsWith(".png")) {
        return FeatureaSwingUtil.getSize(file.getAbsolutePath());
      } else {
        return null;
      }
    } else {
      if (file.listFiles() == null) {
        return null;
      } else {
        boolean isSpritesheet = isSpriteSheet(file);
        if (isSpritesheet) {
          return getSize(file.listFiles()[0]);
        } else {
          return null;
        }
      }
    }
  }

  private boolean isSpriteSheet(File file) {
    File[] children = file.listFiles();
    if (children != null) {
      for (File child : children) {
        if (child.isDirectory() || !child.getName().endsWith(".png")) {
          return false;
        }
      }
    }
    return true;
  }

  private Map<MethodNode, Set<String>> resolveClassNode(String classNode) {
    Map<MethodNode, Set<String>> result = map.get(classNode);
    if (result == null) {
      result = new HashMap<>();
      map.put(classNode, result);
    }
    return result;
  }

  private Set<String> resolveCases(Map<MethodNode, Set<String>> classNodeMethods, String methodName, File file) {
    MethodNode methodNode = new MethodNode(methodName, file);
    Set<String> result = classNodeMethods.get(methodNode);
    if (result == null) {
      result = new HashSet<>();
      classNodeMethods.put(methodNode, result);
    }
    return result;
  }

  private ClassNode retrieveClassNode(String className, ClassNode parent) {
    String[] tokens = StringUtil.split(className, "/");
    for (String token : tokens) {
      ClassNode child = parent.get(token);
      if (child == null) {
        child = new ClassNode(token);
        parent.children.add(child);
        child.parent = parent;
      }
      parent = child;
    }
    return parent;
  }

  private final String upperCaseFirstLetter(String string) {
    return (string.charAt(0) + "").toUpperCase() + string.substring(1, string.length());
  }

}

package featurea.builder.java;

import featurea.app.Project;
import featurea.builder.java.block.ClassBody;
import featurea.builder.java.block.ClassName;
import featurea.builder.java.block.PackageXmlWrap;
import featurea.util.FileUtil;
import featurea.util.Files;
import featurea.xml.XmlContext;
import featurea.xml.XmlTag;

import java.io.File;
import java.util.List;

public class GenJavaTool {

  private final Project project;
  private Files resources;

  public GenJavaTool(Project project) {
    this.project = project;
    resources = new Files().add(project.resourcesDir);
  }

  public void performGen() {
    File assets = project.resourcesDir;
    File[] resPackages = assets.listFiles();
    if (resPackages != null) {
      for (File resPackage : resPackages) {
        if (resPackage.isDirectory()) {
          String resPackageName = resPackage.getName();
          List<String> xmlFiles = resources.listFilesRecursively(resPackageName, ".xml");
          if (!xmlFiles.isEmpty()) {
            for (String xmlFile : xmlFiles) {
              createJavaFile(xmlFile);
            }
          }
        }
      }
    }
  }

  private void createJavaFile(String xmlFile) {
    File genDir = project.generatedFilesDir;
    File file = resources.findFile(xmlFile);
    XmlContext xmlContext = project.resources.getContext(file);
    XmlTag tag = xmlContext.xmlTag;
    String genPackage = tag.getGenPackage();
    String classBody = new ClassBody(xmlContext).code(tag, project);
    classBody = PackageXmlWrap.code(genPackage, classBody);
    String dir = genPackage.replaceAll("\\.", "/");
    FileUtil.write(classBody, new File(genDir, dir + "/" + ClassName.getClassName(tag) + ".java"));
  }

}

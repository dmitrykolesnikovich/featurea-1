package genSprites;

import featurea.app.Project;
import featurea.util.FileUtil;
import featurea.util.ReflectionUtil;
import featurea.util.StringUtil;
import featurea.xml.XmlSchema;
import featurea.xml.XmlTag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class CollisionResolverAdapterCode {

  private final Project project;

  private CollisionResolverAdapterCode(Project project) {
    this.project = project;
  }

  public static void gen(Project project) {
    new CollisionResolverAdapterCode(project).performGen();
  }

  private void performGen() {
    String bodyClass = "featurea.platformer.physics.Body";
    String enemyClass = "mario.objects.enemies.Enemy";
    String bonusClass = "mario.objects.bonuses.Bonus";
    List<String> classes = new ArrayList<>();
    XmlSchema xmlSchema = project.xmlSchema;
    List<String> areas = XmlTag.retrieveAreaTagsFromSchema(xmlSchema);
    for (String area : areas) {
      String canonicalClassName = xmlSchema.getCanonicalClassName(area);
      String canonicalSuperClassName = xmlSchema.getCanonicalSuperClassName(area);
      if (canonicalSuperClassName.equals(bodyClass) || canonicalSuperClassName.equals(enemyClass) || canonicalSuperClassName.equals(bonusClass)) { // todo improve this shit
        classes.add(canonicalClassName);
      }
    }
    classes.remove(bodyClass); // todo improve this shit
    classes.remove(enemyClass); // todo improve this shit
    classes.remove(bonusClass); // todo improve this shit

    List<String> result = new ArrayList<>();
    for (String klass : classes) {
      boolean isTopOnHierarchy = true;
      for (String child : classes) {
        if (!klass.equals(child)) {
          if (xmlSchema.getCanonicalSuperClassName(xmlSchema.getTagName(child)).equals(klass)) {
            isTopOnHierarchy = false;
          }
        }
      }
      if (isTopOnHierarchy) {
        result.add(klass);
      }
    }
    Collections.sort(result, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return ReflectionUtil.getSimpleClassName(o1).compareTo(ReflectionUtil.getSimpleClassName(o2));
      }
    });
    StringBuilder builder = new StringBuilder();
    builder.append("package mario;" +
        "import featurea.platformer.CollisionResolver;" +
        "import featurea.platformer.physics.Body;" +
        "import featurea.platformer.Animation;" +
        "import mario.objects.enemies.KoopaTroopa;" +
        "import mario.objects.hero.Hero;" +
        "import mario.objects.landscape.Flagpole;" +
        "import mario.objects.landscape.Tube;" +
        "public abstract class CollisionResolverAdapter extends CollisionResolver {");

    builder.append("public abstract boolean filter(Animation body1, Animation body2);");

    builder.append("@Override public final boolean shouldDetectIntersection(Animation body1, Animation body2) {");
    builder.append("if(!filter(body1, body2)) { return false; }");
    for (int i = 0; i < result.size(); i++) {
      for (int j = i; j < result.size(); j++) {
        String klass1 = result.get(i);
        String klass2 = result.get(j);
        builder.append(getIfStatements(klass1, klass2));
      }
    }
    builder.append("return true; }");

    for (int i = 0; i < result.size(); i++) {
      for (int j = i; j < result.size(); j++) {
        String klass1 = result.get(i);
        String klass2 = result.get(j);
        builder.append(shouldDetectIntersectionStatements(klass1, klass2));
      }
    }

    builder.append("}");
    FileUtil.write(builder.toString(), new File(project.generatedFilesDir, project.pakage + "/CollisionResolverAdapter.java"));
  }

  private String shouldDetectIntersectionStatements(String klass1, String klass2) {
    String var1 = StringUtil.lowerFirstLetter(ReflectionUtil.getSimpleClassName(klass1)) + "1";
    String var2 = StringUtil.lowerFirstLetter(ReflectionUtil.getSimpleClassName(klass2)) + "2";
    return "public boolean shouldDetectIntersection(" + klass1 + " " + var1 + ", " + klass2 + " " + var2 + "){return true;}";
  }

  private String getIfStatements(String klass1, String klass2) {
    return "if (body1 instanceof " + klass1 + " && body2 instanceof " + klass2 + ") {" +
        "return shouldDetectIntersection((" + klass1 + ") body1, (" + klass2 + ") body2);" +
        "} else if (body2 instanceof " + klass1 + " && body1 instanceof " + klass2 + ") {" +
        "return shouldDetectIntersection((" + klass1 + ") body2, (" + klass2 + ") body1);" +
        "}";
  }

  public static void main(String[] args) {
    Project project = new Project();
    project.setFile(new File(args[0]));
    CollisionResolverAdapterCode.gen(project);
  }

}

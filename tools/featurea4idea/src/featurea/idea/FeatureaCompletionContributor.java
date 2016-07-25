package featurea.idea;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import featurea.app.Project;
import featurea.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class FeatureaCompletionContributor extends CompletionContributor {

  @Override
  public void fillCompletionVariants(@NotNull CompletionParameters parameters, final @NotNull CompletionResultSet resultSet) {
    PsiElement originalPosition = parameters.getOriginalPosition();
    if (originalPosition != null) {
      final PsiElement parent = originalPosition.getParent();
      final PsiFile file = originalPosition.getContainingFile();
      if (parent instanceof XmlTag) {
        XmlTag xmlTag = (XmlTag) parent;
        String name = xmlTag.getName();
        String xmlFile = file.getVirtualFile().getCanonicalPath();
        File projectFile = FileUtil.retrieveProjectFileByXmlFile(new File(xmlFile));
        Project project = new Project();
        project.setFile(projectFile);
        List<String> attributes = project.xmlSchema.getAttributes(name);
        for (XmlAttribute xmlAttribute : xmlTag.getAttributes()) {
          attributes.remove(xmlAttribute.getName());
        }
        for (String attribute : attributes) {
          resultSet.consume(LookupElementBuilder.create(attribute));
        }
      }
    }
  }

}

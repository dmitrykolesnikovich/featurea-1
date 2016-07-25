package featurea.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiFile;
import featurea.desktop.Simulator;

import java.io.File;

public class RunXmlFileAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent event) {
    PsiFile file = event.getData(LangDataKeys.PSI_FILE);
    if (file != null) {
      String xmlFile = file.getVirtualFile().getCanonicalPath();
      Simulator.startNewProcess(false, new File(xmlFile));
    }
  }

}

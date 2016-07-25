package featurea.app;

import featurea.audio.Audio;
import featurea.input.Input;
import featurea.opengl.Render;
import featurea.util.Files;
import featurea.util.Performance;
import featurea.util.Timer;
import featurea.xml.XmlSchema;

// container for app code
public class MediaPlayer {

  /*components*/
  public final Audio audio = new Audio(this);
  public final Render render = new Render(this);
  public final Input input = new Input(this);
  public final Application app = new Application(this);
  public final Timer timer = new Timer(this);
  public final Performance performance = new Performance(this);
  public final Loader loader = new Loader(this);
  public final Project project = new Project();

  /*options: project, production, synchronize, stop, play, pause, build*/

  public void stop() {
    project.setFile(project.file);
    project.setProduction(project.isProduction());
    project.resources.editor.reload();
  }

  public void play() {
    timer.scale = 1;
  }

  public void pause() {
    timer.scale = 0;
  }

  public boolean isPause() {
    return timer.scale == 0;
  }

  /*project components*/

  public Files getFiles() {
    return project.getFiles();
  }

  public ProjectClassLoader getClassLoader() {
    return project.classLoader;
  }

  public boolean isProduction() {
    return project.isProduction();
  }

  public XmlSchema getXmlSchema() {
    return project.xmlSchema;
  }

  public XmlResources getResources() {
    return project.resources;
  }


}

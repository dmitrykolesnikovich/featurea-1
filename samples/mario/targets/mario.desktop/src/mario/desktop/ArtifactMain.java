package mario.desktop;

import featurea.desktop.Simulator;
import featurea.platformer.config.Engine;
import featurea.util.Size;

import java.io.File;

public class ArtifactMain {

  public static void main(String[] args) {
    Simulator simulator = new Simulator();
    simulator.setProduction(false);
    simulator.setXmlFile(new File("d:/Featurea/samples/mario/res/levels/world1_1.xml"));
    simulator.setSize(new Size(Engine.cameraWidth, Engine.cameraHeight));
    simulator.setTitle("Mario");
    simulator.start();
  }

}

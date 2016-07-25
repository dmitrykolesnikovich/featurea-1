package test;

import featurea.app.MediaPlayer;
import featurea.app.Screen;
import featurea.desktop.Simulator;

import java.io.File;

public class AngleMainProd {

  public static void main(String[] args) {
    Simulator simulator = new Simulator() {
      @Override
      public void onCreate(MediaPlayer mediaPlayer) {
        super.onCreate(mediaPlayer);
        mediaPlayer.app.screen = new Screen().add(sandbox.res.Angle.value());
      }
    };
    simulator.setManifestFile(new File("E:/featurea/engines/dyn4j/project.xml"));
    simulator.setSize(400, 400);
    simulator.setTitle("AngleMainProd");
    simulator.start();
  }

}

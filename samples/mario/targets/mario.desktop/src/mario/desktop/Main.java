package mario.desktop;

import featurea.app.MediaPlayer;
import featurea.desktop.LwjglNatives;
import featurea.desktop.Simulator;
import mario.Navigation;
import mario.util.MarioPreferences;

import java.io.File;

public class Main {

  public static void main(String[] args) {
    MarioPreferences.dir = new File(LwjglNatives.PREFERENCES_PATH);
    Simulator simulator = new Simulator() {
      @Override
      public void onCreate(MediaPlayer mediaPlayer) {
        super.onCreate(mediaPlayer);
        Navigation.entryPoint(mediaPlayer);
      }
    };
    simulator.setProduction(false);
    simulator.setProject(new File("D:/workspace/featurea/samples/mario/project.xml"));
    simulator.setSize(600, 600);
    simulator.setTitle("Mario");
    simulator.start();
  }

}

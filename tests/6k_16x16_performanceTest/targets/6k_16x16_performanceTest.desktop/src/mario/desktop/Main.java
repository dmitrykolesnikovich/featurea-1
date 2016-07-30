package mario.desktop;

import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.desktop.Simulator;
import test.Navigation;

import java.io.File;

public class Main {

  public static void main(String[] args) {
    Simulator simulator = new Simulator() {
      @Override
      public void onCreate(MediaPlayer mediaPlayer) {
        super.onCreate(mediaPlayer);
        Navigation.entryPoint(mediaPlayer);
      }
    };
    simulator.setProduction(true);
    simulator.setProject(new File(Project.PROJECT_FILE_NAME));
    simulator.setSize(600, 600);
    simulator.setTitle("6k_16x16_performanceTest");
    simulator.start();
  }

}

package mario.desktop;

import featurea.app.MediaPlayer;
import featurea.desktop.Simulator;

import java.io.File;

public class Main {

  public static void main(String[] args) {
    Simulator simulator = new Simulator() {
      @Override
      public void onCreate(MediaPlayer mediaPlayer) {
        super.onCreate(mediaPlayer);
        /*Navigation.entryPoint(context);*/
      }
    };
    simulator.setProduction(false);
    simulator.setXmlFile(new File("E:/featurea/samples/mario/res/levels/world1_2.xml"));
    simulator.setSize(600, 600);
    simulator.setTitle("@@@");
    simulator.start();
  }

}

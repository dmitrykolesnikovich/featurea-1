package mario.desktop;

import featurea.desktop.Simulator;

import java.io.File;

public class TwoKoopaTroopas {

  public static void main(String[] args) {
    Simulator simulator = new Simulator();
    simulator.setXmlFile(new File("E:/featurea/samples/mario/res/test/twoKoopaTroopas.xml"));
    simulator.setSize(256 * 2, 224 * 2);
    simulator.setTitle("TwoKoopaTroopas");
    simulator.start();
  }

}

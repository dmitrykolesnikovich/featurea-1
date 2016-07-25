package mario.desktop;

import featurea.desktop.Simulator;

import java.io.File;

public class Test1 {

  public static void main(String[] args) {
    Simulator simulator = new Simulator();
    simulator.setProduction(false);
    simulator.setXmlFile(new File("E:/featurea/samples/mario/res/test1.xml"));
    simulator.setSize(600, 600);
    simulator.setTitle("@@@");
    simulator.start();
  }

}

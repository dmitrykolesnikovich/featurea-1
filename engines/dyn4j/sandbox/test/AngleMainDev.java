package test;

import featurea.desktop.Simulator;

import java.io.File;

public class AngleMainDev {

  public static void main(String[] args) {
    Simulator simulator = new Simulator();
    simulator.setXmlFile(new File("E:/featurea/engines/dyn4j/res/sandbox/angle.xml"));
    simulator.setSize(400, 400);
    simulator.setTitle("AngleMainDev");
    simulator.start();
  }

}

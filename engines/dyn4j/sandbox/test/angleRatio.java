package test;

import featurea.desktop.Simulator;

import java.io.File;

public class angleRatio {

  public static void main(String[] args) {
    Simulator simulator = new Simulator();
    simulator.setXmlFile(new File("E:/featurea/engines/dyn4j/res/sandbox/angleRatio.xml"));
    simulator.setSize(400, 400);
    simulator.setTitle("angleRatio");
    simulator.start();
  }

}

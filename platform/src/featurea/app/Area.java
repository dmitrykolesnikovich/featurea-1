package featurea.app;

import featurea.graphics.Graphics;

import java.util.List;

public interface Area {

  List<? extends Area> listAreas();

  void onTick(double elapsedTime);

  void onDraw(Graphics graphics);

}

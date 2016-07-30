package featurea.platformer.util;

import featurea.app.Camera;
import featurea.app.Context;
import featurea.app.Layer;
import featurea.graphics.Canvas;
import featurea.graphics.Font;
import featurea.graphics.Graphics;
import featurea.graphics.Text;
import featurea.platformer.Assets;
import featurea.util.Colors;
import featurea.util.Targets;

public class PerformanceInfo extends Layer {

  private Text text = new Text().setColor(Colors.red);
  private double totalTime;
  private static final double DELTA = 1200;

  public PerformanceInfo() {
    setCamera((Camera) new Camera().setRectangle(0, 0, 100, 100));
    text.font = new Font().setFntFile(Assets.Fonts.pressstart2p);
    this.isVisible = !Targets.isDesktop;
    setCanvas(new Canvas());
  }

  @Override
  public void onDraw(Graphics graphics) {
    totalTime += Context.getTimer().getElapsedTime();
    if (totalTime > DELTA) {
      totalTime %= DELTA;
      text.string = (int) Context.getPerformance().fps + "";
    }
  }

}

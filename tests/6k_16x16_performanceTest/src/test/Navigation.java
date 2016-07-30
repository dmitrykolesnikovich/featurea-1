package test;

import featurea.app.*;
import featurea.graphics.Graphics;

import java.util.List;

public class Navigation {

  public static void entryPoint(MediaPlayer mediaPlayer) {
    Context.getLoader().listener = new Loader.Listener() {
      @Override
      public void onLoad(double progress) {
        if (progress == 1) {
          setupApp();
        }
      }
    };
    Context.getLoader().load(Assets.fourStars);
  }

  private static void setupApp() {
    Layer layer = new Layer().setCamera((Camera) new Camera().setRectangle(0, 0, 320, 320));
    final int capacity = 6_000;
    MyCanvas canvas = (MyCanvas) new MyCanvas().setCapacity(capacity).setLayer(layer).build();
    layer.setCanvas(canvas);
    layer.add(new Area() {
      @Override
      public List<? extends Area> listAreas() {
        return null;
      }

      @Override
      public void onTick(double elapsedTime) {

      }

      @Override
      public void onDraw(Graphics graphics) {
        if (!graphics.containsDrawTexture()) {
          for (int i = 0; i < capacity; i++) {
            Camera camera = graphics.getLayer().getCamera();
            double x1 = Math.random() * camera.width();
            double y1 = Math.random() * camera.height();
            graphics.drawTexture(Assets.fourStars, x1, y1, x1 + 16, y1 + 16, null, 0, 0, null, false, false);
          }
        }
      }
    });
    Context.getApplication().screen = new Screen().add(layer.build());
    canvas.getGraphics().clearDrawTexture();
  }

}

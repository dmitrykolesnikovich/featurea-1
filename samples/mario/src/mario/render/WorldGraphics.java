package mario.render;

import featurea.app.Camera;
import featurea.graphics.Graphics;
import featurea.opengl.Batches;
import mario.objects.hero.World;

public class WorldGraphics extends Graphics {

  private final double leftX;
  private final double rightX;

  public WorldGraphics(double x1, double x2) {
    this.leftX = x1;
    this.rightX = x2;
    setDrawTextureBatch(Batches.newDrawTextureBatch());
    setDrawLineAndDrawRectangleBach(Batches.newDrawLineAndDrawRectangleBatch());
    setFillRectangleBatch(Batches.newFillRectangleBatch());
  }

  @Override
  public boolean isShown() {
    World world = (World) getLayer();
    Camera camera = world.getCamera();
    return camera.right() > leftX && camera.left() < rightX;
  }

}

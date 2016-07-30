package mario.features;

import featurea.app.Camera;
import featurea.app.Context;
import mario.objects.hero.World;

public class CameraFeature {

  public void make(World world) {
    if (world.getHero() != null) {
      positionCamera(world);
      positionBody(world);
    }
  }

  private void positionCamera(World world) {
    if (world.getSize() != null) {
      Camera camera = world.getCamera();
      double prevX = camera.getPosition().x;
      double currentX = world.getHero().position.x - world.getCameraOffset();
      double newCameraX;
      // >>
      // 1)
      if (Context.isProduction()) {
        newCameraX = Math.max(prevX, currentX);
      } else {
        newCameraX = currentX;
      }
      // 2)
      /*newCameraX = currentX;*/
      // <<
      camera.setPosition(newCameraX, 0);
      if (camera.left() < 0) {
        camera.setLeft(0);
      }
      if (camera.right() > world.getSize().width) {
        camera.setRight(world.getSize().width);
      }
    }
  }

  private void positionBody(World world) {
    if (world.getHero().left() < world.getCamera().left()) {
      world.getHero().setLeft(world.getCamera().left());
    }
    if (world.getHero().right() > world.getCamera().right()) {
      world.getHero().setRight(world.getCamera().right());
    }
  }

}

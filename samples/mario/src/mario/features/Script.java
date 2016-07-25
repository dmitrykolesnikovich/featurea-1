package mario.features;

import mario.objects.hero.World;

public class Script {

  private final World world;
  private final CameraFeature cameraFeature = new CameraFeature();

  public Script(World world) {
    this.world = world;
  }

  public void onTick(double elapsedTime) {
    cameraFeature.make(world);
  }

}

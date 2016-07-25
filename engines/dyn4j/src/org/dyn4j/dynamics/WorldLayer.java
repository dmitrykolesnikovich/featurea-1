package org.dyn4j.dynamics;

import featurea.app.Area;
import featurea.app.Camera;
import featurea.app.Context;
import featurea.app.Layer;
import featurea.graphics.Graphics;
import featurea.util.Colors;
import featurea.util.Vector;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;

public class WorldLayer extends Layer {

  private final World world = new World();

  public WorldLayer() {
    world.setGravity(new Vector2(0, 0));
  }

  public WorldLayer setGravity(Vector gravity) {
    world.setGravity(new Vector2(gravity.x, gravity.y));
    return this;
  }

  public WorldLayer add(Body body) {
    super.add((Area) body);
    world.addBody(body);
    return this;
  }

  public WorldLayer remove(Body body) {
    super.remove((Area) body);
    world.removeBody(body);
    return this;
  }

  public WorldLayer add(Joint joint) {
    super.add((Area) joint);
    world.addJoint(joint);
    return this;
  }

  public WorldLayer remove(Joint joint) {
    super.remove((Area) joint);
    world.removeJoint(joint);
    return this;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    world.update(elapsedTime / 1000d, 100_000);
  }

  @Override
  public void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    if (Context.isFeaturea()) {
      Camera camera = getCamera();
      graphics.drawRectangle(camera.left(), camera.top(), camera.right(), camera.bottom(), Colors.orange);
    }
  }

  /*wrappers*/

  public void setGravity(Vector2 gravity) {
    world.setGravity(gravity);
  }

}

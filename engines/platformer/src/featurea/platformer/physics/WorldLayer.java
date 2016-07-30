package featurea.platformer.physics;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Camera;
import featurea.app.Layer;
import featurea.platformer.Animation;
import featurea.platformer.CollisionResolver;
import featurea.platformer.engine.Script;
import featurea.platformer.util.BodyIndex;
import featurea.platformer.util.BufferedAreasAndBodiesProjection;
import featurea.platformer.util.MyLayer;
import featurea.util.Selection;
import featurea.util.Size;
import featurea.util.Vector;
import featurea.xml.XmlNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static featurea.platformer.config.Engine.cameraHeight;
import static featurea.platformer.config.Engine.cameraWidth;

public class WorldLayer extends MyLayer implements XmlNode {

  private final Script script = new Script();
  private CollisionResolver collisionFilter;
  private final BufferedAreasAndBodiesProjection bufferedAreas = new BufferedAreasAndBodiesProjection(this);
  private final Size size = new Size();
  public final Vector a = new Vector(0, featurea.platformer.config.Engine.gravity);
  public final BodyIndex bodyIndex = new BodyIndex(this).setStep(8);
  public final Set<Animation> overlapAnimations = new HashSet<>();

  public WorldLayer() {
    setCollisionFilter(new CollisionResolver());
    tickProjection = bufferedAreas;
    Camera camera = new Camera();
    camera.setRectangle(0, 0, cameraWidth, cameraHeight);
    setCamera(camera);
  }

  public CollisionResolver getCollisionFilter() {
    return collisionFilter;
  }

  public void setCollisionFilter(CollisionResolver collisionFilter) {
    this.collisionFilter = collisionFilter;
  }

  public void setSize(Size size) {
    this.size.setValue(size);
  }

  public Size getSize() {
    return size;
  }

  /*@Override
  public void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    if (!Context.getRender().isScreenMode) {
      Context.getRender().cropWithCamera(getCamera());
    }
  }*/

  public double getCameraOffset() {
    return featurea.platformer.config.Engine.cameraOffset;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (!isTimeStop()) {
      script.make(this);
    }
    script.make2(this);
  }

  public boolean isUnderwater() {
    return false;
  }

  public List<Animation> getBodies() {
    return bufferedAreas.getBodies();
  }

  @Override
  public WorldLayer add(Area area) {
    super.add(area);
    if (area instanceof Animation) {
      Animation animation = (Animation) area;
      animation.setLayer(this);
      animation.onAdd();
      animation.updateIndex();
    }
    return this;
  }


  @Override
  public Layer remove(Area area) {
    super.remove(area);
    if (area instanceof Animation) {
      Animation animation = (Animation) area;
      animation.onRemove();
    }
    return this;
  }

  public Iterable<? extends Animation> getBodies(Animation body) {
    return bodyIndex.getBodies(body);
  }

  public boolean isTimeStop() {
    return false;
  }

  public boolean isJoystickEnable() {
    return !isTimeStop();
  }

  public Script getScript() {
    return script;
  }

  public void onMotionFinish() {
    for (Animation animation : getBodies()) {
      animation.onMotionFinish();
    }
  }


  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    result.setRectangle(0, 0, size.width, size.height);
    result.isSelected = false;
  }

  @Override
  public String toString() {
    return "WorldLayer";
  }

}

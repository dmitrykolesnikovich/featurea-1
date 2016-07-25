package featurea.platformer;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.graphics.Sprite;
import featurea.motion.Motion;
import featurea.motion.Timeline;
import featurea.platformer.overlap.FeatureVerticalMotion;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.overlap.OverlapManager;
import featurea.platformer.physics.Body;
import featurea.platformer.physics.WorldLayer;
import featurea.platformer.util.Rectangle;
import featurea.util.*;
import featurea.xml.XmlNode;
import featurea.xml.XmlResource;

import java.util.ArrayList;
import java.util.List;

import static featurea.platformer.config.Engine.verticalOverlapTolerance;

public class Animation implements XmlNode, Area, XmlResource, TransformRotate, TransformMove {

  private static final double MAX_MOTION_STEP = 6;
  public final Vector position = new Vector();
  private Animation parent;
  private boolean isVisible = true;
  private WorldLayer layer;
  private final List<Animation> children = new ArrayList<>();
  public final Timeline timeline = new Timeline(this);
  public final Angle angle = new Angle();
  public Sprite sprite = new AnimationSprite(this);
  private final Rectangle rectangle = new Rectangle();
  public boolean isDirty = true;
  public OverlapManager overlapManager = new OverlapManager(this);
  private double lifeDistance;
  public boolean isCountinuesCollisionMode;
  private boolean isMotionableHorizontally = true;
  private boolean isMotionableVertically = true;
  public final Vector velocity = new Vector();

  public void setLifeDistance(double lifeDistance) {
    this.lifeDistance = lifeDistance;
  }

  public double getLifeDistance() {
    return lifeDistance;
  }

  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    // no op
  }

  public void updateIndex() {
    final WorldLayer layer = getLayer();
    if (layer != null) {
      Animation.this.isDirty = true;
      layer.bodyIndex.update(Animation.this);
    }
    for (Animation child : listAreas()) {
      child.updateIndex();
    }
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  public boolean shouldBeSwipedFromBuffer() {
    return true;
  }

  public final double z() {
    return position.z;
  }

  @Override
  public List<? extends Animation> listAreas() {
    return children;
  }

  public WorldLayer getLayer() {
    if (parent == null) {
      return layer;
    } else {
      return parent.getLayer();
    }
  }

  public void setLayer(WorldLayer layer) {
    this.layer = layer;
  }

  public void add(Animation child) {
    children.add(child);
    child.parent = this;
    child.onAdd();
  }

  public void remove(Animation child) {
    child.onRemove();
    children.remove(child);
    child.parent = null;
  }

  public final void removeSelf() {
    if (parent != null) {
      parent.remove(this);
    } else {
      if (layer != null) {
        layer.remove(this);
        onRemove(); // IMPORTANT
      }
    }
  }

  public void removeAllChildren() {
    for (Animation child : new ArrayList<>(children)) {
      child.removeSelf();
    }
  }

  public void onRemove() {
    WorldLayer layer = getLayer();
    if (layer != null) {
      layer.bodyIndex.clear(this);
    }
  }

  public void onAdd() {
    // no op
  }

  public Animation setPosition(double x, double y, double z) {
    position.x = x;
    position.y = y;
    position.z = z;
    return this;
  }

  public Animation setPosition(Vector position) {
    return setPosition(position.x, position.y, position.z);
  }

  public Animation setPosition(double x, double y) {
    setPosition(x, y, position.z);
    return this;
  }

  @Override
  public Animation build() {
    return this;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public void rotate(Angle angle) {
    rotate(ox(), oy(), angle);
  }

  public void rotate(double ox, double oy, Angle angle) {
    if (angle.getValue() != 0) {
      this.rectangle.rotate(ox, oy, angle);
      updateIndex();
      this.angle.plus(angle);

      // >> just for now todo avoid this
      Iterable<? extends Animation> bodies = layer.getBodies(this);
      for (Animation target : bodies) {
        if (this != target) {
          if (layer.getCollisionFilter().shouldDetectIntersection(this, target)) {
            FeatureVerticalMotion.resolveShiftVertical(target, this);
          }
        }
      }
      // <<
    }
  }

  public final Vector moveVector = new Vector();

  @Override
  public void move(double dx, double dy, double dz) {
    moveVector.x += dx;
    moveVector.y += dy;
    moveVector.z += dz;

    if (moveVector.x > MAX_MOTION_STEP) {
      moveVector.x = MAX_MOTION_STEP;
    }
    if (moveVector.x < -MAX_MOTION_STEP) {
      moveVector.x = -MAX_MOTION_STEP;
    }

    if (moveVector.y > MAX_MOTION_STEP) {
      moveVector.y = MAX_MOTION_STEP;
    }
    if (moveVector.y < -MAX_MOTION_STEP) {
      moveVector.y = -MAX_MOTION_STEP;
    }

    // IMPORTANT
    for (Animation child : children) {
      child.moveVector.x += dx;
      child.moveVector.y += dy;
      child.moveVector.z += dz;
    }
  }

  public void moveNow(double dx, double dy) {
    position.x += dx;
    position.y += dy;
    updateIndex();
  }

  public void shift(double dx, double dy, Body body) {
    moveNow(dx, dy);
  }

  public Animation setRectangle(double x1, double y1, double x2, double y2) {
    this.rectangle.setValue(x1, y1, x2, y2);
    if (sprite.size.isEmpty()) {
      setSize(x2 - x1, y2 - y1);
    }
    updateIndex();
    return this;
  }

  public final Animation setRectangle(featurea.platformer.util.Rectangle rectangle) {
    return setRectangle(rectangle.x1, rectangle.y1, rectangle.x2, rectangle.y2);
  }

  public Animation setSize(double width, double height) {
    sprite.size.setValue(width, height);
    return this;
  }

  public final Animation setSize(Size size) {
    return setSize(size.width, size.height);
  }

  public Size getSize() {
    return sprite.size;
  }

  public void setFps(double fps) {
    sprite.setFps(fps);
  }

  public Animation setSprite(String sprite) {
    this.sprite.setFile(sprite);
    return this;
  }

  public Animation setSprite(String... sprite) {
    this.sprite.setFile(sprite);
    return this;
  }

  public String onUpdateSprite() {
    return null;
  }

  public double width() {
    return rectangle.width();
  }

  public double height() {
    return rectangle.height();
  }

  @Override
  public void onTick(double elapsedTime) {
    sprite.onTick(elapsedTime);
    if (!getLayer().isTimeStop()) {
      timeline.onTick(elapsedTime);
    }
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (isVisible) {
      String file = onUpdateSprite();
      if (file != null) {
        sprite.setFile(file);
      }
      onDrawSpriteIfVisible(graphics);
    }
  }

  protected void onDrawSpriteIfVisible(Graphics graphics) {
    double x1 = position.x;
    double y1 = position.y;
    double x2 = position.x + sprite.getWidth();
    double y2 = position.y + sprite.getHeight();
    double ox = (x1 + x2) / 2;
    double oy = (y1 + y2) / 2;
    sprite.draw(graphics, x1, y1, x2, y2, ox, oy, angle, isFlipX(), isFlipY());
  }

  public boolean isFlipY() {
    return false;
  }

  public boolean isFlipX() {
    return false;
  }

  private final Vector overlap = new Vector();

  public Vector getOverlap(Animation animation) {
    overlap.setValue(0, 0);
    Rectangle.calculateOverlap(overlap, rectangle, position, animation.rectangle, animation.position);
    if (Math.abs(overlap.x) < verticalOverlapTolerance || Math.abs(overlap.y) < verticalOverlapTolerance) {
      overlap.setValue(0, 0);
    }
    return overlap;
  }

  public double left() {
    return position.x + rectangle.left();
  }

  public double right() {
    return position.x + rectangle.right();
  }

  public double top() {
    return position.y + rectangle.top();
  }

  public double bottom() {
    return position.y + rectangle.bottom();
  }

  public double ox() {
    return (left() + right()) / 2;
  }

  public double oy() {
    return (top() + bottom()) / 2;
  }

  public void setBottom(double bottom) {
    double dy = bottom - this.bottom();
    position.y += dy;
    updateIndex();
  }

  public void setTop(double top) {
    double dy = top - this.top();
    position.y += dy;
    updateIndex();
  }

  public void setLeft(double left) {
    double dx = left - this.left();
    position.x += dx;
    updateIndex();
  }

  public void setRight(double right) {
    double dx = right - this.right();
    position.x += dx;
    updateIndex();
  }

  public Rectangle getRectangle() {
    return rectangle;
  }

  public void onMotionFinish() {
    moveVector.setValue(0, 0);
  }

  public void setMotionableVertically(boolean isMotionableVertically) {
    this.isMotionableVertically = isMotionableVertically;
  }

  public boolean isMotionableVertically() {
    return isMotionableVertically;
  }

  public void setMotionableHorizontally(boolean isMotionableHorizontally) {
    this.isMotionableHorizontally = isMotionableHorizontally;
  }

  public boolean isMotionableHorizontally() {
    return isMotionableHorizontally;
  }

  public void onMotionVerticallyBefore() {
    double seconds = Context.getTimer().getElapsedTime() / 1000;
    move(0, velocity.y * seconds, 0);
  }

  public void onMotionHorizontallyBefore() {
    double seconds = Context.getTimer().getElapsedTime() / 1000;
    move(velocity.x * seconds, 0, 0);
  }

  public void add(Motion motion) {
    timeline.add(motion);
  }

  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    result.setRectangle(left(), top(), right(), bottom());
    result.color = Colors.black;
    if (position != null) {
      result.isSelected = position.x > left() && position.x < right() && position.y > top() && position.y < bottom();
    }
    result.position.setValue(this.position);
  }

}

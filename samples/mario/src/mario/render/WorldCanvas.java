package mario.render;

import featurea.app.Area;
import featurea.app.Layer;
import featurea.graphics.Canvas;
import featurea.graphics.DefaultGraphics;
import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.View;
import featurea.platformer.physics.Body;
import featurea.util.Size;
import featurea.util.Vector;
import featurea.xml.XmlResource;
import mario.objects.bonuses.Bonus;
import mario.objects.bonuses.StarBonus;
import mario.objects.enemies.PiranhaPlant;
import mario.objects.hero.Hero;
import mario.objects.hero.World;
import mario.objects.landscape.Flagpole;
import mario.objects.landscape.Platform;
import mario.objects.landscape.Stairs;

import java.util.ArrayList;
import java.util.List;

public class WorldCanvas extends Canvas implements XmlResource {

  private World world;
  private int partsCount;
  private double partWidth;
  private final List<Graphics> allGraphics = new ArrayList<>();
  private final List<Graphics> staticBodiesGraphics = new ArrayList<>();
  private Graphics staticBigAnimations;
  private final List<Graphics> dynamicBodiesBackgroundGraphics = new ArrayList<>();
  private final List<Graphics> dynamicBodiesForegroundGraphics = new ArrayList<>();
  private final List<Graphics> animationGraphics = new ArrayList<>();
  private Graphics worldGraphics;

  public WorldCanvas setPartsCount(int partsCount) {
    this.partsCount = partsCount;
    return this;
  }

  public WorldCanvas setWorld(World world) {
    this.world = world;
    return this;
  }

  @Override
  public WorldCanvas build() {
    validateAttributes();
    Size size = world.getSize();
    double width = size.width;
    partWidth = width / partsCount;
    for (int i = 0; i < partsCount; i++) {
      addGraphicsPart(i * partWidth, (i + 1) * partWidth);
    }

    worldGraphics = new DefaultGraphics() {
      @Override
      public String toString() {
        return "worldGraphics";
      }
    }.setLayer(world).build();
    allGraphics.add(worldGraphics);
    allGraphics.addAll(animationGraphics);
    allGraphics.addAll(dynamicBodiesBackgroundGraphics);
    allGraphics.addAll(staticBodiesGraphics);

    // staticBigBodies
    staticBigAnimations = new DefaultGraphics() {
      @Override
      public String toString() {
        return "staticBigAnimations";
      }
    }.setLayer(world).build();
    allGraphics.add(staticBigAnimations);

    allGraphics.addAll(dynamicBodiesForegroundGraphics);
    return this;
  }

  @Override
  public Graphics getGraphics(Area area) {
    if (area instanceof World) {
      return worldGraphics;
    }
    Animation animation = (Animation) area;
    Vector position = animation.position;
    if (isBig(animation)) {
      return staticBigAnimations;
    }
    if (area instanceof Body) {
      Body body = (Body) area;
      if (body.isSolid()) {
        return getStaticBodiesGraphics(position);
      } else {
        if (isBackgroundBody(animation)) {
          return getDynamicBodiesBackgroundGraphics(position);
        } else {
          return getDynamicBodiesForegroundGraphics(position);
        }
      }
    } else {
      return getAnimationGraphics(position);
    }
  }

  private boolean isBig(Animation animation) {
    if (animation instanceof Platform) {
      return true;
    }
    if (animation instanceof Stairs) {
      return true;
    }
    return false;
  }

  private boolean isBackgroundBody(Animation animation) {
    if (animation instanceof Bonus) {
      if (animation instanceof StarBonus) {
        StarBonus starBonus = (StarBonus) animation;
        return !starBonus.isCreate;
      }
      return true;
    }
    if (animation instanceof Hero) {
      Hero hero = (Hero) animation;
      return hero.getView() == View.sit;
    }
    if (animation instanceof Flagpole) {
      return true;
    }
    if (animation instanceof PiranhaPlant) {
      return true;
    }
    return false;
  }

  public List<Graphics> getAllGraphics() {
    return allGraphics;
  }

  @Override
  public void onDrawBuffers(Layer layer) {
    Slices.textures.draw(this);
    Slices.shapes.draw(this);
    clearAllBuffers(); // todo this line of code should be removed to improve performance
  }

  /*private API*/

  private void validateAttributes() {
    if (partsCount == 0) {
      throw new IllegalStateException("partsCount == 0");
    }
    if (world == null) {
      throw new IllegalStateException("world == null");
    }
  }

  private void addGraphicsPart(double x1, double x2) {
    animationGraphics.add(new WorldGraphics(x1, x2).setLayer(world).build());
    staticBodiesGraphics.add(new WorldGraphics(x1, x2).setLayer(world).build());
    dynamicBodiesForegroundGraphics.add(new WorldGraphics(x1, x2).setLayer(world).build());
    dynamicBodiesBackgroundGraphics.add(new WorldGraphics(x1, x2).setLayer(world).build());
  }

  private Graphics getAnimationGraphics(Vector position) {
    int index = (int) (position.x / partWidth);
    if (index < 0) {
      index = 0;
    }
    if (index < animationGraphics.size()) {
      return animationGraphics.get(index);
    } else {
      throw new IllegalArgumentException("position == " + position);
    }
  }

  private Graphics getStaticBodiesGraphics(Vector position) {
    int index = (int) (position.x / partWidth);
    if (index < 0) {
      index = 0;
    }
    if (index < staticBodiesGraphics.size()) {
      return staticBodiesGraphics.get(index);
    } else {
      throw new IllegalArgumentException("position == " + position);
    }
  }

  private Graphics getDynamicBodiesForegroundGraphics(Vector position) {
    int index = (int) (position.x / partWidth);
    if (index < 0) {
      index = 0;
    }
    if (index < dynamicBodiesForegroundGraphics.size()) {
      return dynamicBodiesForegroundGraphics.get(index);
    } else {
      throw new IllegalArgumentException("position == " + position);
    }
  }

  private Graphics getDynamicBodiesBackgroundGraphics(Vector position) {
    int index = (int) (position.x / partWidth);
    if (index < 0) {
      index = 0;
    }
    if (index < dynamicBodiesBackgroundGraphics.size()) {
      return dynamicBodiesBackgroundGraphics.get(index);
    } else {
      throw new IllegalArgumentException("position == " + position);
    }
  }

  private void clearAllBuffers() {
    for (Graphics graphics : allGraphics) {
      clearGraphicsAll(graphics);
    }
  }

}

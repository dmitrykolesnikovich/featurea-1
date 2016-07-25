package mario.objects.bonuses;

import featurea.app.Layer;
import featurea.graphics.Graphics;
import featurea.motion.Motion;
import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.objects.hero.World;
import mario.objects.landscape.Block;

public class Bonus extends Body {

  private Block block;
  public boolean isAppear;
  public boolean isCreate;

  public Bonus setBlock(Block block) {
    this.block = block;
    return this;
  }

  public Block getBlock() {
    return block;
  }

  public boolean work(Layer layer) {
    layer.add(this);
    if (isAppear) {
      Motion motion = new Movement() {
        @Override
        public void onTick(double elapsedTime) {
          super.onTick(elapsedTime);
          if (bottom() <= block.top()) {
            stop();
            onCreate();
          }
        }
      }.setGraph(0, -1, 0).setVelocity(0.02).setLoop(true);
      add(motion);
    } else {
      onCreate();
    }
    return true;
  }

  public World getWorld() {
    return (World) super.getLayer();
  }

  public static Bonus valueOf(String primitive) {
    if (primitive.startsWith("becomeBig")) {
      return new BecomeBigBonus();
    } else if (primitive.startsWith("becomeFiery")) {
      return new BecomeFieryBonus();
    } else if (primitive.startsWith("coinRotate")) {
      return CoinRotateBonus.valueOf(primitive);
    } else if (primitive.startsWith("star")) {
      return new StarBonus();
    } else if (primitive.startsWith("liana")) {
      return new LianaBonus();
    } else if (primitive.startsWith("levelUp")) {
      return new LevelUpBonus();
    } else {
      return null;
    }
  }

  public void debugDraw(Graphics graphics) {
    double x1 = position.x;
    double y1 = position.y;
    y1 -= sprite.getHeight();
    double x2 = x1 + sprite.getWidth();
    double y2 = y1 + sprite.getHeight();
    double ox = (x1 + x2) / 2;
    double oy = (y1 + y2) / 2;
    sprite.draw(graphics, x1, y1, x2, y2, ox, oy, angle, isFlipX(), isFlipY());
  }

  public void onCreate() {
    this.position.z = block.position.z - 1;
    isCreate = true;
    setBottom(block.top());
    if (isAppear) {
      velocity.x = 60;
    }
  }

  @Override
  public boolean isFlipX() {
    return false;
  }

  @Override
  public double getLifeDistance() {
    return 0;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (hasPlatform()) {
      if (horizontal != null) {
        if (animation instanceof Body) {
          Body body = (Body) animation;
          if (body.isSolid()) {
            Body.bounce(this, body);
          }
        }
      }
    }
  }

}


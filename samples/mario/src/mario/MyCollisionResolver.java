package mario;

import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import mario.objects.bonuses.Bonus;
import mario.objects.bonuses.StarBonus;
import mario.objects.enemies.*;
import mario.objects.hero.Hero;
import mario.objects.landscape.*;

public class MyCollisionResolver extends CollisionResolverAdapter {

  @Override
  public boolean filter(Animation animation1, Animation animation2) {
    // Fish & Octopus
    if (animation1 instanceof Fish || animation1 instanceof Octopus) {
      if (animation2 instanceof Body) {
        Body body2 = (Body) animation2;
        if (body2.isSolid()) {
          return body2 instanceof Platform && body2.oy() > 200;
        }
      }
    }
    if (animation2 instanceof Fish || animation2 instanceof Octopus) {
      if (animation1 instanceof Body) {
        Body body1 = (Body) animation1;
        if (body1.isSolid()) {
          return body1 instanceof Platform && body1.oy() > 200;
        }
      }
    }

    // Liana
    if (animation1 instanceof Liana) {
      return animation2 instanceof Hero && !((Hero) animation2).isDead();
    }
    if (animation2 instanceof Liana) {
      return animation1 instanceof Hero && !((Hero) animation1).isDead();
    }

    // FishFly
    if (animation1 instanceof FishFly) {
      return animation2 instanceof Hero && !((Hero) animation2).isDead();
    }
    if (animation2 instanceof FishFly) {
      return animation1 instanceof Hero && !((Hero) animation1).isDead();
    }

    // FireStick
    if (animation1 instanceof FireStick) {
      return animation2 instanceof Hero && !((Hero) animation2).isDead();
    }
    if (animation2 instanceof FireStick) {
      return animation1 instanceof Hero && !((Hero) animation1).isDead();
    }

    // DragonFire
    if (animation1 instanceof DragonFire) {
      return animation2 instanceof Hero && !((Hero) animation2).isDead();
    }
    if (animation2 instanceof DragonFire) {
      return animation1 instanceof Hero && !((Hero) animation1).isDead();
    }

    if (animation1 instanceof Block && animation2 instanceof Block) {
      return false;
    }

    if (animation1 instanceof Block && animation2 instanceof Body) {
      if (((Block) animation1).isDead()) {
        return false;
      }
    }
    if (animation2 instanceof Block && animation1 instanceof Body) {
      if (((Block) animation2).isDead()) {
        return false;
      }
    }

    if (animation1 instanceof Enemy && animation2 instanceof Body) {
      if (((Enemy) animation1).isDeadBecauseOfFire()) {
        return false;
      }
    }
    if (animation2 instanceof Enemy && animation1 instanceof Body) {
      if (((Enemy) animation2).isDeadBecauseOfFire()) {
        return false;
      }
    }

    if (animation1 instanceof Hero && animation2 instanceof Body) {
      return !((Hero) animation1).isDead() && !((Hero) animation1).isBack;
    }
    if (animation2 instanceof Hero && animation1 instanceof Body) {
      return !((Hero) animation2).isDead() && !((Hero) animation2).isBack;
    }

    if (animation1 instanceof Bonus && animation2 instanceof Enemy) {
      return false;
    }
    if (animation2 instanceof Bonus && animation1 instanceof Enemy) {
      return false;
    }

    if (animation1 instanceof StarBonus && animation2 instanceof Body) {
      if (animation2 instanceof Block) {
        Block block = (Block) animation2;
        return block.getState() == Block.State.stone;
      }
    }
    if (animation2 instanceof StarBonus && animation1 instanceof Body) {
      if (animation1 instanceof Block) {
        Block block = (Block) animation1;
        return block.getState() == Block.State.stone;
      }
    }

    if (animation1 instanceof Bonus && animation2 instanceof Body) {
      return ((Bonus) animation1).getMass() != 0;
    }
    if (animation2 instanceof Bonus && animation1 instanceof Body) {
      return ((Bonus) animation2).getMass() != 0;
    }

    return true;
  }

  @Override
  public boolean shouldDetectIntersection(Hero hero, KoopaTroopa koopaTroopa) {
    return true;
  }

  @Override
  public boolean shouldDetectIntersection(Hero hero, Tube tube) {
    return GameplayScreen.isJoystickEnable();
  }

  @Override
  public boolean shouldDetectIntersection(Goomba goomba1, Platform platform2) {
    return !goomba1.isDeadBecauseOfFire();
  }

  @Override
  public boolean shouldDetectIntersection(KoopaTroopa koopaTroopa1, Platform platform2) {
    return !koopaTroopa1.isDeadBecauseOfFire();
  }

  @Override
  public boolean shouldDetectIntersection(PiranhaPlant piranhaPlant1, Tube tube2) {
    return false;
  }

}

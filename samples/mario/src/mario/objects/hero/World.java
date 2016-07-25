package mario.objects.hero;

import featurea.app.Context;
import featurea.app.Screen;
import featurea.graphics.Graphics;
import featurea.input.InputAdapter;
import featurea.input.Key;
import featurea.motion.Motion;
import featurea.platformer.Animation;
import featurea.platformer.config.Engine;
import featurea.platformer.physics.WorldLayer;
import featurea.platformer.physics.WorldZOrder;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Preferences;
import featurea.util.Targets;
import mario.*;
import mario.config.Gameplay;
import mario.features.Script;
import mario.objects.landscape.Tube;
import mario.util.TubeProjection;
import mario.util.UnderwaterFeatures;

public class World extends WorldLayer {

  private Hero hero;
  private Theme theme = Theme.overworld;
  private Script script = new Script(this);
  private String assets;

  public World() {
    setzOrder(new WorldZOrder());
    setCollisionFilter(new MyCollisionResolver());
    /*getCamera().setResizeAnchorHorizontal(ResizeAnchorHorizontal.center);*/
    /*getCamera().setResizeAnchorVertical(ResizeAnchorVertical.center);*/

    if (Targets.isDesktop) {
      inputListeners.add(new InputAdapter() {
        @Override
        public boolean onTouchDown(double x, double y, int id) {
          if (Context.getInput().keyboard.isDown(Key.CONTROL_LEFT)) {
            hero.setPosition(x, y);
          }
          return true;
        }
      });
    }
  }

  public World setAssets(String assets) {
    this.assets = assets;
    return this;
  }

  public World setHero(Hero hero) {
    if (this.hero != null) {
      this.hero.removeSelf();
    }
    this.hero = hero;
    add(this.hero);
    return this;
  }

  public Hero getHero() {
    return hero;
  }

  public void setTheme(Theme theme) {
    if (theme == null) {
      theme = Theme.overworld;
    }
    this.theme = theme;
    if (theme == Theme.underwater) {
      a.setValue(0, Engine.underwaterGravity);
    } else {
      a.setValue(0, Engine.gravity);
    }
    if (isCurrent()) {
      Sprites.theme = theme;
      if (!Context.isFeaturea() && !Context.isProduction()) {
        if (assets == null) {
          assets = getAssetsForLoad(theme) + ", " + getAssetsForLoad(Theme.overworld);
        }
        Context.getLoader().load(assets);
      }
    }
  }

  private String getAssetsForLoad(Theme theme) {
    return "background/" + theme + ", enemies/" + theme + ", items/" + theme + ", mario/" + theme;
  }

  public Theme getTheme() {
    return theme;
  }

  @Override
  protected void onAttachToScreen(final Screen screen) {
    super.onAttachToScreen(screen);
    if (hero != null) {
      hero.foreground();
    } else {
      System.out.println("No hero found in World");
    }
    if (!Context.isFeaturea()) {
      if (theme == Theme.overworld) {
        screen.background = new Color().setValue(Gameplay.overworldColor);
      } else if (theme == Theme.castle) {
        screen.background = Colors.black;
      } else if (theme == Theme.underground) {
        screen.background = Colors.black;
      } else if (theme == Theme.underwater) {
        UnderwaterFeatures.instance.onAttach(this);
      }
    }
  }

  @Override
  public boolean isUnderwater() {
    return theme == Theme.underwater;
  }

  @Override
  public void onTick(double elapsedTime) {
    GameplayScreen current = (GameplayScreen) getScreen();
    current.getJoystick().update();
    super.onTick(elapsedTime);
    script.onTick(elapsedTime);
  }

  public void onFinish() {
    World nextWorld = Worlds.next(1);
    Navigation.setWorld(nextWorld);
  }

  @Override
  public boolean isTimeStop() {
    return hero.isTimeStop();
  }

  @Override
  public boolean isJoystickEnable() {
    return hero.isJoystickEnable();
  }

  public static boolean isHeroLefter(Animation animation) {
    World world = (World) animation.getLayer();
    return world.hero.ox() < animation.ox();
  }

  private final TubeProjection tubes = new TubeProjection();

  public Tube getTubeByOut(String outTubeMarker) {
    tubes.project(this);
    for (Tube tube : tubes) {
      if (outTubeMarker.equals(tube.getOut())) {
        return tube;
      }
    }
    return null;
  }

  @Override
  public void onApplyPreferences(Preferences preferences) {
    if (this.hero != null) {
      this.hero.onApplyPreferences(preferences);
    }
  }

  /*comebackWorld API*/

  private World comebackWorld;

  public void setComebackWorld(World comebackWorld) {
    this.comebackWorld = comebackWorld;
  }

  public World getComebackWorld() {
    return comebackWorld;
  }

  public void comeback(final String in, Motion motion) {
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            // >> todo improve this shit
            if (comebackWorld == null) {
              comebackWorld = misc.res.MarioUndergroundEnd.value();
            }
            // <<
            Navigation.setOutWorld(in, comebackWorld);
          }
        });
      }
    };
  }

  @Override
  public World build() {
    super.build();
    if (hero == null) {
      System.err.println("hero == null");
    }
    return this;
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (theme == Theme.underwater) {
      UnderwaterFeatures.instance.onDraw(this);
    }
    super.onDraw(graphics);
  }

  @Override
  public void onMotionFinish() {
    super.onMotionFinish();
    if (theme == Theme.underwater) {
      UnderwaterFeatures.instance.onMotionFinish(this);
    }
  }

  private boolean isCurrent() {
    return Context.getApplication().screen == getScreen();
  }

}

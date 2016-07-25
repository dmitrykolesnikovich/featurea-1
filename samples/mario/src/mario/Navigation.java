package mario;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.Loader;
import featurea.app.MediaPlayer;
import featurea.motion.Motion;
import featurea.platformer.View;
import featurea.util.Colors;
import mario.objects.hero.Hero;
import mario.objects.hero.World;
import mario.objects.landscape.Tube;
import mario.util.MarioPreferences;

public class Navigation {

  public static final MarioPreferences preferences = new MarioPreferences();
  private static final Runnable hideLoadingLayer = new Runnable() {
    @Override
    public void run() {
      final Layer loadingLayer = GameplayScreen.getLoadingLayer();
      if (loadingLayer != null) {
        loadingLayer.isVisible = false;
      }
    }
  };

  public static void entryPoint(final MediaPlayer mediaPlayer) {
    if (Context.isProduction()) {
      mediaPlayer.loader.listener = new Loader.Listener() {
        @Override
        public void onLoad(double progress) {
          if (progress == 1) {
            setWorld(Worlds.current());
          } else {
            Context.getRender().fillRectangle(0, 0, (float) (progress * Context.getRender().size.width), 2, Colors.blue);
          }
        }
      };
      mediaPlayer.loader.load("");
    } else {
      mediaPlayer.loader.listener = new Loader.Listener() {
        @Override
        public void onLoad(double progress) {
          final Layer loadingLayer = GameplayScreen.getLoadingLayer();
          if (loadingLayer != null) {
            boolean isVisible = progress != 1;
            if (isVisible) {
              Context.getTimer().remove(hideLoadingLayer);
              loadingLayer.isVisible = true;
            } else {
              Context.getTimer().delay(hideLoadingLayer, 1000);
            }
          }
        }
      };
      setWorld(Worlds.current());
    }
  }

  public static void setWorld(World world) {
    preferences.save(); // IMPORTANT the only place where preferences are saved
    Context.getApplication().screen = screens.res.GameplayScreen.value().setWorld(world);
    Sprites.theme = world.getTheme();
  }

  public static void setBonusWorld(String bonusWorld, World comebackWorld) {
    World world = Worlds.getBonusWorld(bonusWorld);
    world.setComebackWorld(comebackWorld);
    comebackWorld.getHero().joystickRight(); // todo improve this
    Navigation.setWorld(world);
  }

  public static void setOutWorld(String tubeOut, World world) {
    final Tube tube = world.getTubeByOut(tubeOut);
    // >> todo improve this shit
    final Hero hero = world.getHero();
    hero.isBack = true;
    hero.setView(View.stay);
    hero.setPlatform(tube);
    // <<
    hero.setBottom(tube.top());
    hero.position.x = tube.ox() - hero.getSize().width / 2;
    Navigation.setWorld(world);
    // enter animation
    hero.background();
    Motion motion = tube.getExitMotion(hero);
    hero.timeline.add(motion);
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        hero.isBack = false;
        hero.foreground();
      }
    };

    Context.getAudio().play(Assets.Audio.Sounds.Pipe);
  }

}

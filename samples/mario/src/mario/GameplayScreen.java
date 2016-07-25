package mario;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.Screen;
import featurea.platformer.util.Joystick;
import mario.objects.hero.World;
import mario.util.MyScreen;

/**
 * layers sequence: World, Joystick, Performance
 */
public class GameplayScreen extends MyScreen {

  private Joystick joystick;
  private World world;
  private Layer loadingLayer;

  public GameplayScreen setJoystick(Joystick joystick) {
    this.joystick = joystick;
    this.add(joystick);
    return this;
  }

  public Joystick getJoystick() {
    return joystick;
  }

  public GameplayScreen setWorld(World world) {
    this.world = world;
    super.add(world, 0);
    return this;
  }

  public static World getWorld() {
    GameplayScreen current = current();
    if (current != null) {
      return current.world;
    } else {
      return null;
    }
  }

  public void setLoadingLayer(Layer loadingLayer) {
    this.loadingLayer = loadingLayer;
    this.loadingLayer.isVisible = false;
    this.add(loadingLayer);
  }

  public static Layer getLoadingLayer() {
    GameplayScreen current = current();
    if (current != null) {
      return current.loadingLayer;
    } else {
      return null;
    }
  }

  public static void disableJoystick() {
    GameplayScreen current = current();
    if (current != null) {
      current.joystick.isEnable = false;
    }
  }

  public static void enableJoystick() {
    GameplayScreen current = current();
    if (current != null) {
      current.joystick.isEnable = true;
    }
  }

  public static boolean isJoystickEnable() {
    GameplayScreen current = current();
    if (current != null) {
      return current.joystick.isEnable;
    } else {
      return false;
    }
  }

  public static GameplayScreen current() {
    return (GameplayScreen) Context.getApplication().screen;
  }

  //

  @Override
  public Screen add(Layer layer) {
    if (layer instanceof World) {
      return setWorld((World) layer);
    } else {
      return super.add(layer);
    }
  }

  @Override
  public Screen add(Layer layer, int index) {
    if (layer instanceof World) {
      return setWorld((World) layer);
    } else {
      return super.add(layer, index);
    }
  }

}

package featurea.platformer.util;

import featurea.app.Camera;
import featurea.app.Context;
import featurea.graphics.Canvas;
import featurea.input.Keyboard;
import featurea.platformer.config.Engine;
import featurea.platformer.physics.HeroBody;
import featurea.ui.Button;
import featurea.ui.UILayer;
import featurea.util.ResizeAnchorVertical;
import featurea.util.Size;
import featurea.util.Targets;
import featurea.util.ZoomAnchorHorizontal;

public class Joystick extends UILayer {

  public static Runnable buttonSettingsClick = new Runnable() {
    @Override
    public void run() {

    }
  };

  public static Runnable buttonConfigClick = new Runnable() {
    @Override
    public void run() {

    }
  };

  public final KeyboardController keyboardController = new KeyboardController() {
    @Override
    public HeroBody getHero() {
      return Joystick.this.getHero();
    }
  };
  public boolean isEnable = true;

  public Joystick() {
    Camera camera = new Camera();
    camera.setRectangle(0, 0, Engine.joystickCameraWidth, Engine.joystickCameraHeight);
    camera.setZoomAnchorHorizontal(ZoomAnchorHorizontal.bottom);
    camera.setResizeAnchorVertical(ResizeAnchorVertical.bottom);
    setCamera(camera);
    setCanvas(new Canvas());
    this.isVisible = !Targets.isDesktop;
  }

  public void buttonRightDown(Button button) {
    keyboardController.rightButtonDown();
  }

  public void buttonRightUp(Button button) {
    keyboardController.rightButtonUp();
  }

  public void buttonLeftDown(Button button) {
    keyboardController.leftButtonDown();
  }

  public void buttonLeftUp(Button button) {
    keyboardController.leftButtonUp();
  }

  public void buttonUpDown(Button button) {
    keyboardController.topButtonDown();
  }

  public void buttonUpUp(Button button) {
    keyboardController.topButtonUp();
  }

  public void buttonDownDown(Button button) {
    keyboardController.bottomButtonDown();
  }

  public void buttonDownUp(Button button) {
    keyboardController.bottomButtonUp();
  }

  public void buttonJumpDown(Button button) {
    keyboardController.jumpButtonDown();
  }

  public void buttonJumpUp(Button button) {
    keyboardController.jumpButtonUp();
  }

  public void buttonFireDown(Button button) {
    keyboardController.fireButtonDown();
  }

  public void buttonFireUp(Button button) {
    keyboardController.fireButtonUp();
  }

  public void buttonSettingsClick(Button button) {
    buttonSettingsClick.run();
  }

  protected void buttonConfigClick(Button button) {
    buttonConfigClick.run();
  }

  @Override
  public void onResize(Size size) {
    Camera camera = getCamera();
    /*buttonSettings.setPosition(camera.left() + 40, camera.top() + 40 - camera.zoom.y);
    buttonConfig.setPosition(camera.right() - 32, camera.top() + 32 - camera.zoom.y);*/
  }



  public void update() {
    if (isEnable) {
      Keyboard keyboard = Context.getInput().keyboard;
      keyboard.update(keyboardController);
      keyboardController.update();
    }
  }

  public HeroBody getHero() {
    return null;
  }

}

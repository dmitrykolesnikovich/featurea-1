package featurea.platformer.util;

import featurea.input.InputAdapter;
import featurea.input.Key;
import featurea.platformer.config.Engine;

public abstract class KeyboardController extends InputAdapter implements HeroHolder {

  private boolean isButtonLeftDown;
  private boolean isButtonLeftUp;
  private boolean isButtonRightDown;
  private boolean isButtonRightUp;
  private boolean isButtonTopDown;
  private boolean isButtonTopUp;
  private boolean isButtonBottomDown;
  private boolean isButtonBottomUp;
  private boolean isButtonFireDown;
  private boolean isButtonFireUp = true;
  private boolean isButtonJumpDown;
  private boolean isButtonJumpUp;

  public void rightButtonDown() {
    if (!getHero().isClimb()) {
      this.isButtonRightDown = true;
      this.isButtonRightUp = false;
    } else {
      this.isButtonRightDown = false;
      if (isButtonRightUp) {
        this.isButtonRightDown = true;
        this.isButtonRightUp = false;
      }
    }
  }

  public void rightButtonUp() {
    this.isButtonRightDown = false;
    this.isButtonRightUp = true;
  }

  public void leftButtonDown() {
    if (!getHero().isClimb()) {
      this.isButtonLeftDown = true;
      this.isButtonLeftUp = false;
    } else {
      this.isButtonLeftDown = false;
      if (isButtonLeftUp) {
        this.isButtonLeftDown = true;
        this.isButtonLeftUp = false;
      }
    }
  }

  public void leftButtonUp() {
    this.isButtonLeftDown = false;
    this.isButtonLeftUp = true;
  }

  public void topButtonDown() {
    this.isButtonTopDown = true;
    this.isButtonTopUp = false;
  }

  public void topButtonUp() {
    this.isButtonTopDown = false;
    this.isButtonTopUp = true;
  }

  public void bottomButtonDown() {
    this.isButtonBottomDown = true;
    this.isButtonBottomUp = false;
  }

  public void bottomButtonUp() {
    this.isButtonBottomDown = false;
    this.isButtonBottomUp = true;
  }

  public void jumpButtonDown() {
    this.isButtonJumpDown = false;
    if (isButtonJumpUp) {
      this.isButtonJumpDown = true;
      this.isButtonJumpUp = false;
    }
  }

  public void jumpButtonUp() {
    this.isButtonJumpDown = false;
    this.isButtonJumpUp = true;
  }

  public void fireButtonDown() {
    this.isButtonFireDown = false;
    if (isButtonFireUp) {
      this.isButtonFireDown = true;
      this.isButtonFireUp = false;
    }
  }

  public void fireButtonUp() {
    this.isButtonFireDown = false;
    this.isButtonFireUp = true;
  }

  @Override
  public void onKeyDown(Key key) {
    if (key == Key.D) {
      rightButtonDown();
    }
    if (key == Key.A) {
      leftButtonDown();
    }
    if (key == Key.SPACE) {
      jumpButtonDown();
    }
    if (key == Key.M) {
      fireButtonDown();
    }
    if (key == Key.W) {
      topButtonDown();
    }
    if (key == Key.S) {
      bottomButtonDown();
    }
  }

  @Override
  public void onKeyUp(Key key) {
    if (key == Key.D) {
      rightButtonUp();
    }
    if (key == Key.A) {
      leftButtonUp();
    }
    if (key == Key.SPACE) {
      jumpButtonUp();
    }
    if (key == Key.M) {
      fireButtonUp();
    }
    if (key == Key.W) {
      topButtonUp();
    }
    if (key == Key.S) {
      bottomButtonUp();
    }
  }

  private boolean isAccelerationLeft;
  private boolean isAccelerationRight;
  private double stepRightCounter;
  private double stepLeftCounter;

  public void update() {
    if (getHero().isClimb() || !getHero().getLayer().isTimeStop()) {
      if (isButtonLeftDown) {
        if (!isAccelerationLeft) {
          stepLeftCounter++;
          if (stepLeftCounter >= Engine.accelerationStepCount) {
            stepLeftCounter = Engine.accelerationStepCount * Engine.accelerationFactor;
            isAccelerationLeft = true;
          }
        }
      } else {
        if (!isAccelerationLeft) {
          stepLeftCounter = 0;
        }
      }
      if (isButtonRightDown) {
        if (!isAccelerationRight) {
          stepRightCounter++;
          if (stepRightCounter >= Engine.accelerationStepCount) {
            stepLeftCounter = Engine.accelerationStepCount * Engine.accelerationFactor;
            isAccelerationRight = true;
          }
        }
      } else {
        if (!isAccelerationRight) {
          stepRightCounter = 0;
        }
      }
      if (isButtonLeftDown) {
        getHero().joystickLeft();
      } else if (isButtonRightDown) {
        getHero().joystickRight();
      } else if (isButtonTopDown) {
        getHero().joystickUp();
      } else if (isButtonBottomDown) {
        getHero().joystickDown();
      } else {
        getHero().joystickNothing();
      }
      // >> acceleration logic
      /*if (isAccelerationLeft && !isButtonLeftDown) {
        boolean isSit = getView() == View.sit;
        joystickLeft();
        if (!isJump()) {
          if (isSit) {
            stepLeftCounter -= 0.125;
          } else {
            stepLeftCounter -= 1;
          }
          if (stepLeftCounter <= 0) {
            isAccelerationLeft = false;
          }
        }
      }
      if (isAccelerationRight && !isButtonRightDown) {
        boolean isSit = getView() == View.sit;
        joystickRight();
        if (!isJump()) {
          if (isSit) {
            stepRightCounter -= 0.125;
          } else {
            stepRightCounter -= 1;
          }
          if (stepRightCounter <= 0) {
            isAccelerationRight = false;
          }
        }
      }*/
      // <<
      if (isButtonJumpDown) {
        if (getHero().getLayer().isUnderwater()) {
          getHero().swim();
        } else {
          getHero().jump();
        }
      }
      if (isButtonFireDown) {
        getHero().fire();
      }
    }
  }

  @Override
  public void clear() {
    isButtonLeftDown = false;
    isButtonLeftUp = false;
    isButtonRightDown = false;
    isButtonRightUp = false;
    isButtonTopDown = false;
    isButtonTopUp = false;
    isButtonBottomDown = false;
    isButtonBottomUp = false;
    isButtonFireDown = false;
    isButtonFireUp = true;
    isButtonJumpDown = false;
    isButtonJumpUp = false;
  }

}

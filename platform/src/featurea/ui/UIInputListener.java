package featurea.ui;

import featurea.input.InputAdapter;

public class UIInputListener extends InputAdapter {

  private final UILayer layer;
  private boolean isPin;

  public UIInputListener(UILayer layer) {
    this.layer = layer;
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    return down(layer, x, y, id) != null;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    Button pinButton = down(layer, x, y, id);
    if (pinButton != null) {
      pinButton.performMove();
    }
    return pinButton != null;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    up(layer, x, y, id);
    return false;
  }

  private Button down(UILayer layer, double x, double y, int id) {
    Button pinButton = null;
    for (Button button : layer.buttons) {
      if (button.contains(x, y)) {
        isPin = true;
        button.pin(id);
        pinButton = button;
      } else {
        button.unpin(id);
      }
    }
    return pinButton;
  }

  private void up(UILayer layer, double x, double y, int id) {
    for (Button button : layer.buttons) {
      button.unpin(id);
      if (button.contains(x, y)) {
        isPin = true;
        button.performClick();
      }
    }
  }

  @Override
  public void clear() {
    isPin = false;
  }

}

package featurea.ui;

import featurea.graphics.Graphics;
import featurea.graphics.Sprite;
import featurea.util.Size;

public class Button extends TextView {

  private int pinId = -1;
  public ClickListener clickListener;
  private final Sprite sprite = new Sprite();

  /*package*/ void performClick() {
    if (clickListener != null) {
      clickListener.click(Button.this);
    }
  }

  /*package*/ void performMove() {
    if (clickListener != null) {
      clickListener.move(Button.this);
    }
  }

  /*package*/ void pin(int id) {
    if (pinId != id) {
      pinId = id;
      performDown();
    }
  }

  /*package*/ void unpin(int id) {
    if (pinId == id) {
      pinId = -1;
      performUp();
    }
  }

  /*private API*/

  private void performUp() {
    if (clickListener != null) {
      clickListener.up(Button.this);
    }
  }

  private void performDown() {
    if (clickListener != null) {
      clickListener.down(Button.this);
    }
  }

  public boolean isDown() {
    return pinId != -1;
  }

  public void setSprite(String sprite) {
    this.sprite.setFile(sprite);
  }

  public void setSize(Size size) {
    this.sprite.size.setValue(size);
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (isVisible()) {
      sprite.draw(graphics, getPosition().x, getPosition().y, angle,
          isFlipX(), isFlipY(), getScaleX(), getScaleY());
    }
    super.onDraw(graphics);
  }

}

package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.motion.Movement;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;
import mario.config.Gameplay;

public class Liana extends Body {

  private double initialY;
  private int length;
  private boolean isGrowUp;

  public Liana() {
    setSize(Sprites.Items.lianaWidth, Sprites.Items.lianaHeight);
    setLadder(true);
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.liana();
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  @Override
  public Liana build() {
    initialY = position.y;
    setShape(length);
    return this;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    for (int i = 0; i < length; i++) {
      double x1 = position.x;
      double x2 = x1 + Sprites.Items.lianaWidth;
      double y1 = position.y + i * Sprites.Items.lianaHeight;
      double y2 = y1 + Sprites.Items.lianaHeight;
      graphics.drawTexture(Sprites.Items.liana(), x1, y1, x2, y2, null, 0, 0, Colors.white, false, false, null);
    }
  }

  private void setShape(int length) {
    setRectangle(Sprites.Items.lianaWidth / 2 - 1, 0, Sprites.Items.lianaWidth / 2 + 1, length * Sprites.Items.lianaHeight);
  }

  public void growUp() {
    if (!isGrowUp) {
      isGrowUp = true;
      timeline.add(new Movement() {
        @Override
        public void onTick(double elapsedTime) {
          super.onTick(elapsedTime);
          if (top() < getLayer().getCamera().top()) {
            stop();
          }
        }
      }.setGraph(0, -1, 0).setVelocity(Gameplay.lianaVelocity).setLoop(true));
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    while (Math.abs(initialY - position.y) / Sprites.Items.lianaHeight > length) {
      length++;
      setShape(length);
    }
  }

}

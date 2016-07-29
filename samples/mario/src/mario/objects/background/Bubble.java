package mario.objects.background;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;

public class Bubble extends Animation {

  private static final String texture = Sprites.Items.bubble();
  private static final double width = Sprites.Items.bubbleWidth;
  private static final double height = Sprites.Items.bubbleHeight;

  public static void make(Body body) {
    double x = body.position.x + body.width() / 2;
    double y = body.position.y + body.height() / 2;
    Bubble bubble = new Bubble() {
      @Override
      public void onTick(double elapsedTime) {
        super.onTick(elapsedTime);
        if (position.y <= 32) {
          Context.getTimer().delay(new Runnable() {
            @Override
            public void run() {
              removeSelf();
            }
          });
        }
      }
    };
    bubble.setPosition(x, y, 0);
    bubble.timeline.add(new Movement().setGraph(0, -1, 0).setVelocity(0.025).setLoop(true));
    body.getLayer().add(bubble);
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    if (!graphics.containsDrawTexture()) {
      double x1 = position.x - width / 2;
      double x2 = position.x + width / 2;
      double y1 = position.y - height / 2;
      double y2 = position.y + height / 2;
      graphics.drawTexture(texture, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false);
    }
  }

  @Override
  public String toString() {
    return "Bubble";
  }

}

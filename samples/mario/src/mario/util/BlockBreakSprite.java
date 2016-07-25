package mario.util;

import featurea.graphics.Graphics;
import featurea.graphics.Sprite;
import featurea.util.Angle;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.World;
import mario.objects.landscape.Block;

public class BlockBreakSprite extends Sprite {

  private double distance;

  private Block block;

  public BlockBreakSprite() {
    setFile(Sprites.Items.Brick.dead());
    setSize(Sprites.Items.Brick.deadWidth, Sprites.Items.Brick.deadHeight);
  }

  public BlockBreakSprite setBlock(Block block) {
    this.block = block;
    return this;
  }

  @Override
  public void onTick(double elapsedTime) {
    // >> todo improve this shit
    World world = (World) block.getLayer();
    if (world.isTimeStop()) {
      world.getScript().featureVerticalMotion.makeBody(block);
      block.onMotionFinish();
    }
    // <<
    distance += Gameplay.blockBreakSpreadVelocity * elapsedTime;
  }

  @Override
  public void draw(Graphics graphics, double x1, double y1, double x2, double y2, double ox, double oy, Angle angle, boolean isFlipX, boolean isFlipY) {
    drawPart(graphics, distance, distance);
    drawPart(graphics, -distance, distance);
    drawPart(graphics, distance, -distance);
    drawPart(graphics, -distance, -distance);
    if (block.oy() >= 300) {
      block.removeSelf();
    }
  }

  private void drawPart(Graphics graphics, double dx, double dy) {
    double x1 = block.ox() - size.width / 2 + dx;
    double x2 = block.ox() + size.width / 2 + dx;
    double y1 = block.oy() - size.height / 2 + dy;
    double y2 = block.oy() + size.height / 2 + dy;
    super.draw(graphics, x1, y1, x2, y2, block.ox(), block.oy(), null, false, false);
  }

}

package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.physics.Body;
import mario.Sprites;

public class BlockTile extends Body {

  public BlockTile() {
    setSolid(true);
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    graphics.drawTile(Sprites.Items.Brick.show(), left(), top(), right(), bottom(), null, 0, 0, null, false, false, null);
  }

}

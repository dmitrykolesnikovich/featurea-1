package mario.layers;

import mario.Sprites;
import mario.objects.landscape.Block;

public class StoneLayer extends BodyLayer {

  private int[] xy;
  private double[] blocks;

  public void setXy(int[] xy) {
    this.xy = xy;
  }

  // preview support
  public void setBlocks(double... blocks) {
    removeAllChildren();
    this.blocks = blocks;
    if (blocks != null) {
      for (int i = 0; i < blocks.length; i += 4) {
        double x1 = blocks[i];
        double y1 = blocks[i + 1];
        double x2 = blocks[i + 2];
        double y2 = blocks[i + 3];
        for (double x = x1; x < x2; x += Sprites.Items.stoneWidth) {
          for (double y = y1; y < y2; y += Sprites.Items.stoneHeight) {
            addStone(x, y);
          }
        }
      }
    }
  }

  @Override
  public StoneLayer build() {
    super.build();
    if (xy != null) {
      for (int i = 0; i < xy.length; i += 2) {
        int x = xy[i];
        int y = xy[i + 1];
        addStone(x, y);
      }
    }
    return this;
  }

  private void addStone(double x, double y) {
    Block stone = new Block();
    stone.setState(Block.State.stone);
    stone.setPosition(x, y);
    add(stone.build());
  }

}

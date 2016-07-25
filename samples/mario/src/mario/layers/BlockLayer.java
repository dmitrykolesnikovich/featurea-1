package mario.layers;

import featurea.util.Vector;
import mario.Sprites;
import mario.objects.bonuses.*;
import mario.objects.landscape.Block;
import mario.objects.landscape.BlockTile;

public class BlockLayer extends BodyLayer {

  public enum Layout {
    full, divide
  }

  private int[] flower;
  private int[] star;
  private int[] levelUp;
  private int[] coin;
  private int[] liana;
  private int[] hidden;
  private int[] brick;
  private Layout layout = Layout.divide;
  private int length;
  private int highness;

  @Override
  public BlockLayer build() {
    super.build();
    removeAllChildren();
    setBlocks(0, 0, (int) (length * Sprites.Items.Brick.showWidth), (int) (highness * Sprites.Items.Brick.showHeight));
    return this;
  }

  public BlockLayer setLength(int length) {
    this.length = length;
    return this;
  }

  public BlockLayer setHighness(int highness) {
    this.highness = highness;
    return this;
  }

  public void setBlocks(int x1, int y1, int x2, int y2) {
    int xMin = Math.min(x1, x2);
    int xMax = Math.max(x1, x2);
    int yMin = Math.min(y1, y2);
    int yMax = Math.max(y1, y2);
    x1 = xMin;
    y1 = yMin;
    x2 = xMax;
    y2 = yMax;

    if (layout == Layout.divide) {
      for (int x = x1; x < x2; x += Sprites.Items.Brick.showWidth) {
        for (int y = y1; y < y2; y += Sprites.Items.Brick.showHeight) {
          addBlock(x, y);
        }
      }
    } else if (layout == Layout.full) {
      BlockTile blockTile = new BlockTile();
      blockTile.setRectangle(x1, y1, x2, y2);
      add(blockTile);
    }

  }

  public void setFlower(int[] flower) {
    this.flower = flower;
  }

  public void setStar(int[] star) {
    this.star = star;
  }

  public void setLevelUp(int[] levelUp) {
    this.levelUp = levelUp;
  }

  public void setCoin(int[] coin) {
    this.coin = coin;
  }

  public void setHidden(int[] hidden) {
    this.hidden = hidden;
  }

  public void setBrick(int[] brick) {
    this.brick = brick;
  }

  public void setLiana(int[] liana) {
    this.liana = liana;
  }

  public void setLayout(Layout layout) {
    this.layout = layout;
  }

  private void addBlock(int x, int y) {
    Block block = new Block();
    block.setPosition(x, y);

    Vector flowerVector = getVector(block, flower);
    if (flowerVector != null) {
      block.setBonus((Bonus) new BecomeBigBonus().setPosition(flowerVector.x, flowerVector.y));
    }

    Vector starVector = getVector(block, star);
    if (starVector != null) {
      block.setBonus((Bonus) new StarBonus().setPosition(starVector.x, starVector.y));
    }

    Vector levelUpVector = getVector(block, levelUp);
    if (levelUpVector != null) {
      block.setBonus((Bonus) new LevelUpBonus().setPosition(levelUpVector.x, levelUpVector.y));
    }

    Vector coinVector = getVector(block, coin, 3);
    if (coinVector != null) {
      block.setBonus((Bonus) new CoinRotateBonus().setCount((int) coinVector.z).setPosition(coinVector.x, coinVector.y));
    }

    Vector lianaVector = getVector(block, liana);
    if (lianaVector != null) {
      block.setBonus((Bonus) new LianaBonus().setPosition(lianaVector.x, lianaVector.y));
    }

    Vector hiddenVector = getVector(block, hidden);
    if (hiddenVector != null) {
      block.setVisible(false);
    }

    if (getVector(block, brick) != null) {
      block.setState(Block.State.brick);
    }

    add(block.build());
  }

}

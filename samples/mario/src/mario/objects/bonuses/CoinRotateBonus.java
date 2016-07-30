package mario.objects.bonuses;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.motion.Motion;
import featurea.motion.Movement;
import featurea.util.StringUtil;
import mario.Assets;
import mario.Sprites;
import mario.config.Gameplay;

public class CoinRotateBonus extends Bonus {

  private int count;
  private static final double width = Sprites.Items.coinRotateWidth;
  private static final double height = Sprites.Items.coinRotateHeight;
  private static final double jumpHeight = 2 * height;

  public CoinRotateBonus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    sprite.setFps(14);
  }

  public int getCount() {
    return count;
  }

  public CoinRotateBonus setCount(int count) {
    this.count = count;
    return this;
  }

  @Override
  public boolean work(final Layer layer) {
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        workSync(layer);
      }
    });
    count--;
    return count <= 0;
  }

  private void workSync(Layer layer) {
    CoinRotateBonus.this.position.z = Gameplay.coinRotateBonusWorkdZ;
    Context.getAudio().play(Assets.Audio.Sounds.Coin);
    sprite.setProgress(0);
    CoinRotateBonus.super.work(layer);
    Motion motion = new Movement().setGraph(0, -jumpHeight, 0, 0, jumpHeight, 0).setVelocity(0.14);
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        CoinRotateBonus.this.removeSelf();
      }
    };
    add(motion);
  }

  public static CoinRotateBonus valueOf(String primitive) {
    CoinRotateBonus result = new CoinRotateBonus();
    String[] tokens = StringUtil.split(primitive, ",");
    result.setCount(Integer.valueOf(tokens[1]));
    return result;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.coinRotate();
  }

}

package mario.objects.hero;

import featurea.app.Context;
import featurea.graphics.Sprite;
import mario.Assets;
import mario.config.Gameplay;

public class HeroSprite extends Sprite {

  private static final String[] BECOME_BIG = {
      Assets.Mario.Overworld.Small.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle,
      Assets.Mario.Overworld.Small.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle,
      Assets.Mario.Overworld.Small.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Big.Color0.stay,
      Assets.Mario.Overworld.Small.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Big.Color0.stay,
  };
  private static final String[] BECOME_SMALL = {
      Assets.Mario.Overworld.Big.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Small.Color0.stay,
      Assets.Mario.Overworld.Big.Color0.stay, Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Small.Color0.stay,
      Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Small.Color0.stay,
      Assets.Mario.Overworld.Big.Color0.middle, Assets.Mario.Overworld.Small.Color0.stay,
  };

  private boolean isColor;
  private boolean isSheet = true;
  private boolean isBecomeSmall;
  private boolean isBecomeBig;
  private boolean isBecomeFiery;

  private final Hero hero;
  private static final double fps = Gameplay.heroFps;
  private static final double becomeBigFps = Gameplay.heroBecomeBigFps;

  private static final double colorSize = 4;

  private int colorIndex;
  private double colorCurrentTime;
  private Runnable isBecomeSmallCompleteListener;

  public boolean isBlink;
  private double currentBlinkTime;

  public HeroSprite(Hero hero) {
    this.hero = hero;
    setFps(fps);
    reset();
  }

  @Override
  public boolean isLoop() {
    if (isBecomeBig || isBecomeSmall) {
      return false;
    } else {
      return super.isLoop();
    }
  }

  @Override
  public void reset() {
    // IMPORTANT no super call
    setStop(false);
    colorIndex = 0;
    colorCurrentTime = 0;
  }

  @Override
  public String getCurrentFile() {
    String file = super.getCurrentFile();
    if (isColor) {
      if (hero.isFiery() && colorIndex != 0) {
        file = file.replaceAll("fiery", "big");
      }

      file = file.replaceAll("color0", "color" + colorIndex);
      file = file.replaceAll("color1", "color" + colorIndex);
      file = file.replaceAll("color2", "color" + colorIndex);
      file = file.replaceAll("color3", "color" + colorIndex);

      if (!Context.getFiles().exists(file)) {
        file = file.replaceAll("castle", "underground");
      }

      if (!Context.getFiles().exists(file)) {
        file = file.replaceAll("castle", "overworld");
        file = file.replaceAll("underground", "overworld");
        file = file.replaceAll("night", "overworld");
        file = file.replaceAll("underwater", "overworld");
      }
    }
    return file;
  }

  @Override
  public void onTick(double elapsedTime) {
    if (isBecomeBig && isStop()) {
      isBecomeBig = false;
      setFps(fps);
    }
    if (isBecomeSmall && isStop()) {
      isBecomeSmall = false;
      if (isBecomeSmallCompleteListener != null) {
        isBecomeSmallCompleteListener.run();
      }
    }
    if (hero.isClimb()) {
      return;
    }

    if (isBlink) {
      currentBlinkTime += elapsedTime;
      if (currentBlinkTime > Gameplay.blinkPeriod) {
        currentBlinkTime %= Gameplay.blinkPeriod;
        if (color.a == 1) {
          color.a = 0.2f;
          redraw();
        } else {
          color.a = 1;
          redraw();
        }
      }
    } else {
      if (color.a != 1) {
        redraw();
      }
      color.a = 1;
    }

    if (isSheet) {
      int startIndex = sheetIndex;
      super.onTick(elapsedTime);
      int finishIndex = sheetIndex;
      if (startIndex != finishIndex) {
        redraw();
      }
    }
    if (isColor) {
      onColorTick(elapsedTime);
    }
  }

  private void redraw() {
    hero.redraw();
  }

  private void onColorTick(double elapsedTime) {
    colorCurrentTime += elapsedTime;
    double oneFrameDuration = 1000f / Gameplay.colorFps;
    double allFramesDuration = oneFrameDuration * colorSize;
    colorCurrentTime %= allFramesDuration;
    int startIndex = colorIndex;
    colorIndex = (int) (colorCurrentTime / oneFrameDuration);
    int finishIndex = colorIndex;
    if (startIndex != finishIndex) {
      redraw();
    }
  }

  public void becomeSmall(Runnable completeListener) {
    isBecomeSmall = true;
    isBecomeSmallCompleteListener = completeListener;
    setFile(BECOME_SMALL);
  }

  public void becomeBig() {
    setFps(becomeBigFps);
    isBecomeBig = true;
    setFile(BECOME_BIG);
  }

  public void becomeFiery() {
    isColor = true;
    isSheet = false;
    isBecomeFiery = true;
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        isColor = false;
        isSheet = true;
        isBecomeFiery = false;
      }
    }, 1200);
  }

  public void becomeInvisibility(double time, final Runnable completeListener) {
    isColor = true;
    isSheet = true;
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        isColor = false;
        isSheet = true;
        completeListener.run();
      }
    }, time);
  }

  public boolean isTimeStop() {
    return isBecomeFiery || isBecomeBig;
  }

  public boolean isTransform() {
    return isBecomeSmall || isBecomeFiery || isBecomeBig;
  }


}

package mario.util;

import featurea.util.FileUtil;
import featurea.util.Preferences;
import featurea.util.Vector;
import mario.GameplayScreen;
import mario.objects.hero.HeroState;
import mario.objects.hero.World;

import java.io.File;

public class MarioPreferences extends Preferences {

  public static File dir;
  public int levelIndex = -1;
  public Vector position;
  public int score = -1;
  public int time = -1;
  public HeroState state;

  public MarioPreferences() {
    super(new File(dir, "mario.properties"));
    String positionValue = getValue("position");
    if (positionValue != null) {
      this.position = Vector.valueOf(positionValue);
    }

    String levelIndexValue = getValue("levelIndex");
    if (levelIndexValue != null) {
      this.levelIndex = Integer.valueOf(levelIndexValue);
    }

    String scoreValue = getValue("score");
    if (scoreValue != null) {
      this.score = Integer.valueOf(scoreValue);
    }

    String timeValue = getValue("time");
    if (timeValue != null) {
      this.time = Integer.valueOf(timeValue);
    }

    String stateValue = getValue("state");
    if (stateValue != null) {
      this.state = HeroState.get(stateValue);
    }
  }

  public void save() {
    // IMPORTANT no super call
    fetch();

    // save string to file
    StringBuilder stringBuilder = new StringBuilder();
    if (position != null) {
      stringBuilder.append("position=" + position.x + ", " + position.y + "\n");
    }
    if (levelIndex != -1) {
      stringBuilder.append("levelIndex=" + levelIndex + "\n");
    }
    if (score != -1) {
      stringBuilder.append("score=" + score + "\n");
    }
    if (time != -1) {
      stringBuilder.append("time=" + time + "\n");
    }
    if (state != null) {
      stringBuilder.append("state=" + state.id + "\n");
    }
    FileUtil.write(stringBuilder.toString(), file);
  }

  private void fetch() {
    World world = GameplayScreen.getWorld();
    if (world != null) {
      this.state = world.getHero().state;
    }
  }

}

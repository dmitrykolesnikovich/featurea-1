package mario;

import featurea.app.Context;
import mario.objects.hero.World;

public class Worlds {
  private static int index;

  public static World current() {
    return getWorld(index);
  }

  public static World next(int delta) {
    index += delta;
    return getWorld(index);
  }

  public static World getWorld(int index) {
    switch (index) {
      case 0: {
        return levels.res.Level1.value();
      }
      case 1: {
        return levels.res.World1_1.value();
      }
      case 2: {
        return levels.res.World1_2.value();
      }
    }
    return null;
  }

  public static World getBonusWorld(String marker) {
    if (!Context.isProduction()) {
      return Context.getResources().getResource("/bonusLevels/" + marker);
    } else {
      switch (marker) {
        case "a":
          return bonusLevels.res.A.value();
        case "b":
          return bonusLevels.res.B.value();
        case "c":
          return bonusLevels.res.C.value();
        case "d":
          return bonusLevels.res.D.value();
        case "e":
          return bonusLevels.res.E.value();
        case "f":
          return bonusLevels.res.F.value();
        case "g":
          return bonusLevels.res.G.value();
        case "h":
          return bonusLevels.res.H.value();
        case "i":
          return bonusLevels.res.I.value();
        case "j":
          return bonusLevels.res.J.value();
        case "k":
          return bonusLevels.res.K.value();
        case "marioUndergroundEnd":
          return misc.res.MarioUndergroundEnd.value();
      }
      throw new IllegalArgumentException("marker = " + marker);
    }
  }

  public static int getIndex() {
    return index;
  }
}
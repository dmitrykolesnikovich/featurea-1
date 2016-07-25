package featurea.platformer;

public enum CollisionResult {

  REVERSE, SHIFTS_TARGET, NULL;

  public boolean isReverse() {
    return this == REVERSE;
  }

  public boolean isSolid() {
    return isReverse() || this == SHIFTS_TARGET;
  }

}

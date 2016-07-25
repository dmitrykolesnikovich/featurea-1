package mario.objects.hero;

public enum HeroState {

  small("small"), big("big"), fiery("fiery");

  public final String id;

  HeroState(String id) {
    this.id = id;
  }

  public static HeroState get(String id) {
    for (HeroState state : HeroState.values()) {
      if (id.equals(state.id)) {
        return state;
      }
    }
    return null;
  }

}

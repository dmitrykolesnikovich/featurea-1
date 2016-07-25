package featurea.input;

public class KeyboardEvent {

  public Key key;
  public KeyboardEventType type;

  @Override
  public String toString() {
    return type + ", " + key;
  }

}

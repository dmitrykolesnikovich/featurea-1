package featurea.input;

public class DisplayEvent {

  public DisplayEventType type;
  public int pointerId; // left mouse button = 0, wheel mouse button = 1, right mouse button = 2
  public double x;
  public double y;
  public int count = 1;

  @Override
  public String toString() {
    return type + ", " + pointerId + ", " + x + ", " + y + ", ";
  }

}

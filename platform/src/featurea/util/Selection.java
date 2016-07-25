package featurea.util;

public class Selection extends Rectangle {

  public Color color = Colors.black;
  public boolean isSelected;
  public final Vector position = new Vector();

  @Override
  public String toString() {
    return left() + ", " + top() + ", " + right() + ", " + bottom();
  }

  public Selection reset() {
    setRectangle(0, 0, 0, 0);
    color = Colors.black;
    isSelected = false;
    position.setValue(0, 0, 0);
    return this;
  }

}

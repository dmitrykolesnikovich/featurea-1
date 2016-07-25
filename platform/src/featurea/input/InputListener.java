package featurea.input;

public interface InputListener {

  boolean onTouchDown(double x, double y, int id);

  boolean onTouchDrag(double x, double y, int id);

  boolean onTouchMove(double x, double y, int id);

  boolean onTouchUp(double x, double y, int id);

  void onKeyDown(Key key);

  void onKeyUp(Key key);

  void clear();

  boolean onMouseWheel(int count, double x, double y);

}

package featurea.input;

public class InputAdapter implements InputListener {

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    return false;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    return false;
  }

  @Override
  public boolean onTouchMove(double x, double y, int id) {
    return false;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    return false;
  }


  @Override
  public boolean onMouseWheel(int count, double x, double y) {
    return false;
  }

  @Override
  public void onKeyDown(Key key) {
    // no op
  }

  @Override
  public void onKeyUp(Key key) {
    // no op
  }

  @Override
  public void clear() {
    // no op
  }


}

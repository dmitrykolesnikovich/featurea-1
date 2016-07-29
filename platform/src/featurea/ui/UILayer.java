package featurea.ui;

import featurea.app.Area;
import featurea.app.Layer;
import featurea.app.Projection;

import java.util.Collections;
import java.util.Comparator;

public class UILayer extends Layer {

  private static final Comparator<Button> BUTTONS_SORT_ORDER = new Comparator<Button>() {
    @Override
    public int compare(Button button1, Button button2) {
      if (button2.getPosition().z > button1.getPosition().z) {
        return 1;
      }
      if (button2.getPosition().z < button1.getPosition().z) {
        return -1;
      }
      return 0;
    }
  };

  private class ButtonsProjection extends Projection<Button> {
    @Override
    public boolean onFilter(Area area) {
      return area instanceof Button;
    }
  }

  public final ButtonsProjection buttons = new ButtonsProjection();

  public UILayer() {
    inputListeners.add(new UIInputListener(this));
    projection = buttons;
  }

  @Override
  public void onTraverse() {
    super.onTraverse();
    Collections.sort(buttons, BUTTONS_SORT_ORDER);
  }

  @Override
  public UILayer add(Area area) {
    super.add(area);
    if (area instanceof TextView) { // this will be improved during UI engine dev
      TextView textView = (TextView) area;
      textView.setLayer(this);
      textView.onAdd();
    }
    return this;
  }


  @Override
  public UILayer remove(Area area) {
    super.remove(area);
    if (area instanceof TextView) { // this will be improved during UI engine dev
      TextView textView = (TextView) area;
      textView.onRemove();
      textView.setLayer(null);
    }
    return this;
  }


}

package mario.util;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.Screen;
import featurea.platformer.util.MyLayer;
import mario.Navigation;

public class MyScreen extends Screen {

  @Override
  public Screen add(Layer layer, int index) {
    super.add(layer, index);
    preferencesApply(layer);
    return this;
  }

  @Override
  public Screen add(Layer layer) {
    super.add(layer);
    preferencesApply(layer);
    return this;
  }

  private void preferencesApply(Layer layer) {
    if (Context.isFeaturea()) {
      return;
    }
    if (layer instanceof MyLayer) {
      MyLayer myLayer = (MyLayer) layer;
      myLayer.onApplyPreferences(Navigation.preferences);
    }
  }

}

package featurea.util;

import featurea.app.Camera;
import featurea.app.Context;

public interface ResizeAnchorVertical extends ResizeAnchor {

  ResizeAnchorVertical top = new ResizeAnchorVertical() {
    @Override
    public void resize(Camera shape) {
      double top = shape.top();
      shape.setHeight(Context.getRender().size.height / (Context.getRender().size.width / shape.width()));
      shape.setTop(top);
    }
  };

  ResizeAnchorVertical bottom = new ResizeAnchorVertical() {
    @Override
    public void resize(Camera shape) {
      double bottom = shape.bottom();
      shape.setHeight(Context.getRender().size.height / (Context.getRender().size.width / shape.width()));
      shape.setBottom(bottom);
    }
  };

  ResizeAnchorVertical center = new ResizeAnchorVertical() {
    @Override
    public void resize(Camera shape) {
      shape.setHeight(Context.getRender().size.height / (Context.getRender().size.width / shape.width()));
    }
  };

}

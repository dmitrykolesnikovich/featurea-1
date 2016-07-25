package featurea.util;

import featurea.app.Camera;
import featurea.app.Context;

public interface ResizeAnchorHorizontal extends ResizeAnchor {

  ResizeAnchorHorizontal left = new ResizeAnchorHorizontal() {
    @Override
    public void resize(Camera shape) {
      double left = shape.left();
      shape.setWidth(Context.getRender().size.width / (Context.getRender().size.height / shape.height()));
      shape.setLeft(left);
    }
  };

  ResizeAnchorHorizontal right = new ResizeAnchorHorizontal() {
    @Override
    public void resize(Camera shape) {
      double right = shape.right();
      shape.setWidth(Context.getRender().size.width / (Context.getRender().size.height / shape.height()));
      shape.setRight(right);
    }
  };

  ResizeAnchorHorizontal center = new ResizeAnchorHorizontal() {
    @Override
    public void resize(Camera shape) {
      shape.setWidth(Context.getRender().size.width / (Context.getRender().size.height / shape.height()));
    }
  };

}

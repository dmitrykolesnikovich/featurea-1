package featurea.util;

import featurea.app.Context;

public interface ZoomAnchorVertical extends ZoomAnchor {

  ZoomAnchorVertical left = new ZoomAnchorVertical() {
    @Override
    public void stretch(final Zoom zoom) {
      Size size = Context.getRender().size;
      double layerHeight = size.height - 2 * zoom.y;
      double scale = size.height / layerHeight;
      zoom.scale *= scale;
      zoom.x = 0;
      zoom.y = 0;
    }
  };

  ZoomAnchorVertical center = new ZoomAnchorVertical() {
    @Override
    public void stretch(final Zoom zoom) {
      Size size = Context.getRender().size;
      double layerHeight = size.height - 2 * zoom.y;
      double scale = size.height / layerHeight;
      zoom.scale *= scale;
      double dWidth = scale * size.width - size.width;
      zoom.x = -dWidth / 2;
      zoom.y = 0;
    }
  };

  ZoomAnchorVertical right = new ZoomAnchorVertical() {
    @Override
    public void stretch(final Zoom zoom) {
      Size size = Context.getRender().size;
      double layerHeight = size.height - 2 * zoom.y;
      double scale = size.height / layerHeight;
      zoom.scale *= scale;
      double dWidth = scale * size.width - size.width;
      zoom.x = -dWidth;
      zoom.y = 0;
    }
  };
}

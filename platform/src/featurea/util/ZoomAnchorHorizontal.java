package featurea.util;

import featurea.app.Context;

public interface ZoomAnchorHorizontal extends ZoomAnchor {

  ZoomAnchorHorizontal top = new ZoomAnchorHorizontal() {
    @Override
    public void stretch(Zoom zoom) {
      Size size = Context.getRender().size;
      double layerWidth = size.width - 2 * zoom.x;
      double scale = size.width / layerWidth;
      zoom.scale *= scale;
      zoom.x = 0;
      zoom.y = 0;
    }
  };

  ZoomAnchorHorizontal center = new ZoomAnchorHorizontal() {
    @Override
    public void stretch(Zoom zoom) {
      Size size = Context.getRender().size;
      double layerWidth = size.width - 2 * zoom.x;
      double scale = size.width / layerWidth;
      zoom.scale *= scale;
      double layerHeight = size.height * scale;
      double dHeight = layerHeight - size.height;
      zoom.x = 0;
      zoom.y = -dHeight / 2;
    }
  };

  ZoomAnchorHorizontal bottom = new ZoomAnchorHorizontal() {
    @Override
    public void stretch(Zoom zoom) {
      Size size = Context.getRender().size;
      double layerWidth = size.width - 2 * zoom.x;
      double scale = size.width / layerWidth;
      zoom.scale *= scale;
      double layerHeight = size.height * scale;
      double dHeight = layerHeight - size.height;
      zoom.x = 0;
      zoom.y = -dHeight;
    }
  };

}

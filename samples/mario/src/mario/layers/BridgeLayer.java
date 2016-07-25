package mario.layers;

import mario.objects.landscape.Bridge;

public class BridgeLayer extends BodyLayer {

  private int[] xy;

  public void setXy(int[] xy) {
    this.xy = xy;
  }

  @Override
  public BridgeLayer build() {
    super.build();
    for (int i = 0; i < xy.length; i += 4) {
      int x1 = xy[i];
      int x2 = xy[i + 1];
      int y1 = xy[i + 2];
      int y2 = xy[i + 3];
      Bridge bridge = (Bridge) new Bridge().setRectangle(0, 0, x2 - x1, y2 - (y1 + (y2 - y1) / 2 + 1)).setPosition(x1, y1);
      add(bridge);
    }
    return this;
  }

}

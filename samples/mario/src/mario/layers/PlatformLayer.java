package mario.layers;

import mario.objects.landscape.Platform;

public class PlatformLayer extends BodyLayer {

  private int[] xy;

  // support preview
  public void setXy(int[] xy) {
    removeAllChildren();
    this.xy = xy;
    for (int i = 0; i < xy.length; i += 4) {
      int x1 = xy[i];
      int x2 = xy[i + 1];
      int y1 = xy[i + 2];
      int y2 = xy[i + 3];
      add(new Platform().setRectangle(x1, y1, x2, y2).build());
    }
  }

  @Override
  public PlatformLayer build() {
    super.build();
    return this;
  }

}

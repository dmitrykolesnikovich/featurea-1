package mario.layers;

import mario.objects.landscape.GiantMushroom;

import java.util.ArrayList;
import java.util.List;

public class GiantMushroomLayer extends BodyLayer {

  private int[] value;
  private GiantMushroom.Mode mode;

  public void setValue(int[] value) {
    this.value = value;
  }

  public void setMode(GiantMushroom.Mode mode) {
    this.mode = mode;
  }

  @Override
  public GiantMushroomLayer build() {
    super.build();
    for (int i = 0; i < value.length; i += 4) {
      int x = value[i];
      int y = value[i + 1];
      int count = value[i + 2];
      int z = value[i + 3];
      GiantMushroom giantMushroom = new GiantMushroom();
      giantMushroom.setPosition(x, y, z);
      giantMushroom.setCount(count);
      giantMushroom.setMode(mode);
      giantMushroom.build();
      add(giantMushroom);
    }
    return this;
  }

}

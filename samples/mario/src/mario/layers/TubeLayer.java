package mario.layers;

import mario.objects.landscape.Tube;

public class TubeLayer extends BodyLayer {

  private int[] piranhaPlant;

  public void setXy(int[] xy) {
    removeAllChildren();
    if (xy != null) {
      for (int i = 0; i < xy.length; i += 2) {
        int x = xy[i];
        int y = xy[i + 1];
        addTube(x, y);
      }
    }
  }

  public void setPiranhaPlant(int[] piranhaPlant) {
    this.piranhaPlant = piranhaPlant;
  }

  private void addTube(double x, double y) {
    Tube tube = new Tube();
    tube.setPosition(x, y);
    tube.buildSize();
    if (getVector(tube, piranhaPlant) != null) {
      tube.setPiranhaPlant(true);
    }
    add(tube.build());
  }

}

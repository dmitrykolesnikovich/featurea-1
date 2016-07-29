package mario.objects.landscape;

import com.sun.istack.internal.Nullable;
import featurea.platformer.Animation;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.util.Vector;
import mario.Sprites;

import java.util.List;

public class Stairs extends Animation {

  private double left, top, right, bottom;
  private int[] steps;

  public Stairs() {
    setSteps(new int[]{1});
  }

  // this style of programming is called "preview support"
  public void setSteps(int[] steps) {
    this.steps = steps;
  }

  @Override
  public Stairs build() {
    if (steps != null) {
      left = Double.MAX_VALUE;
      top = Double.MAX_VALUE;
      right = Double.MIN_VALUE;
      bottom = Double.MIN_VALUE;
      removeAllChildren();
      for (int i = 0; i < steps.length; i++) {
        int step = steps[i];
        for (int j = 0; j < step; j++) {
          double x1 = position.x + i * Sprites.Items.stoneWidth;
          double y1 = position.y - (j + 1) * Sprites.Items.stoneHeight;
          Block stone = new Block();
          stone.setPosition(x1, y1);
          stone.setState(Block.State.stone);
          add(stone);

          left = Math.min(left, stone.left());
          top = Math.min(top, stone.top());
          right = Math.max(right, stone.right());
          bottom = Math.max(bottom, stone.bottom());
        }
      }
    }
    return this;
  }

  @Override
  public boolean shouldBeSwipedFromBuffer() {
    return false;
  }

  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    super.getSelection(result, position);
    if (!listAreas().isEmpty()) {
      result.setRectangle(left, top, right, bottom);
    } else {
      result.setRectangle(ox(), oy(), ox() + 4, oy() - 4);
    }
    if (position != null) {
      result.isSelected = result.contains(position.x, position.y);
    }
    result.color = Colors.green;
  }

  @Override
  public List<? extends Animation> listAreas() {
    return super.listAreas();
  }

}

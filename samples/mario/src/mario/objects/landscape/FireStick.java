package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.motion.Rotation;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.util.Angle;
import mario.Sprites;
import mario.objects.hero.Hero;

public class FireStick extends Body {

  private static final double width = Sprites.Items.Fireball.flyWidth;
  private static final double height = Sprites.Items.Fireball.flyHeight;

  private int length;
  private boolean isEmpty;
  private Angle spriteAngle = new Angle();

  public FireStick() {
    timeline.add(new Rotation().setGraph(360).setVelocity(0.1).setLoop(true));
    getRectangle().setTile(false);
  }

  public void setLength(int length) {
    this.length = length;
  }

  @Override
  public void rotate(Angle angle) {
    super.rotate(angle);
  }

  public void setEmpty(boolean isEmpty) {
    this.isEmpty = isEmpty;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    spriteAngle.setValue(angle.getValue() * 6);
    getRectangle().drawTile(graphics, Sprites.Items.Fireball.fly(), position, spriteAngle);
    /*getRectangle().draw(graphics, position, Colors.red);*/
  }

  @Override
  public Animation build() {
    removeAllChildren();
    getRectangle().setValue(-width / 2, -height / 2, width / 2, height / 2 + (length - 1) * height);
    if (!isEmpty) {
      Block block = new Block();
      block.setState(Block.State.done);
      block.setPosition(position);
      block.position.x -= block.getSize().width / 2;
      block.position.y -= block.getSize().height / 2;
      position.z += 1;
      this.add(block);
    }
    return this;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Hero) {
      Hero hero = (Hero) animation;
      hero.damage();
    }
  }

  @Override
  public String toString() {
    return "FireStick";
  }

  @Override
  public void updateIndex() {
    super.updateIndex();
  }

}

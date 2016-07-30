package featurea.opengl;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.graphics.Graphics;
import featurea.util.Vector;

// lifecycle: clear() -> drawSpecific() -> build() -> drawBuffers()
public class Batch {

  public final Vector lastLayerPosition = new Vector();
  public static final int DEFAULT_CAPACITY = 1_000;
  protected int size;
  private int capacity;
  private boolean isDirty = true;

  public boolean isDirty() {
    return isDirty;
  }

  public final void drawBuffers(Graphics graphics) {
    Layer layer = graphics.getLayer();
    Vector currentPosition = layer.getCamera().getPosition();
    double currentScreenX = layer.toScreenX(currentPosition.x);
    double currentScreenY = layer.toScreenY(currentPosition.y);
    double lastScreenX = layer.toScreenX(lastLayerPosition.x);
    double lastScreenY = layer.toScreenY(lastLayerPosition.y);
    double dx = currentScreenX - lastScreenX;
    double dy = currentScreenY - lastScreenY;
    Context.getRender().shiftBatch(-dx, -dy);
    onDraw(graphics);
    Context.getRender().shiftBatch(dx, dy);
  }

  public void clear() {
    isDirty = true;
    this.size = 0;
  }

  public Batch build() {
    isDirty = false;
    return this;
  }

  protected void onDraw(Graphics graphics) {
    // no op
  }

  public Batch setCapacity(int capacity) {
    this.capacity = capacity;
    return this;
  }

  public int getCapacity() {
    return capacity;
  }

  public boolean isFull() {
    return size == capacity;
  }

  public Batch finish() {
    size++;
    return this;
  }

}
package featurea.opengl;

import featurea.graphics.Graphics;

// lifecycle: clear() -> build() -> draw()
public class Batch {


  public static final int DEFAULT_CAPACITY = 1_000;
  protected int size;
  private int capacity;
  private boolean isDirty = true;

  public boolean isDirty() {
    return isDirty;
  }

  public final void drawBuffers(Graphics graphics) {
    // todo hotfix, translate to correct position before draw
    onDraw(graphics);
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
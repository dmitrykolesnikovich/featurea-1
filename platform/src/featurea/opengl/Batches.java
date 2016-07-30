package featurea.opengl;

import featurea.opengl.batches.DrawLineAndDrawRectangleBatch;
import featurea.opengl.batches.DrawTextureBatch;
import featurea.opengl.batches.FillRectangleBatch;
import featurea.opengl.batches.FillShapeBatch;

public final class Batches {

  private Batches() {
    // no op
  }

  public static DrawTextureBatch newDrawTextureBatch() {
    return newDrawTextureBatch(Batch.DEFAULT_CAPACITY);
  }

  public static DrawTextureBatch newDrawTextureBatch(int capacity) {
    return new DrawTextureBatch().setCapacity(capacity);
  }

  public static DrawLineAndDrawRectangleBatch newDrawLineAndDrawRectangleBatch() {
    return newDrawLineAndDrawRectangleBatch(Batch.DEFAULT_CAPACITY);
  }

  public static DrawLineAndDrawRectangleBatch newDrawLineAndDrawRectangleBatch(int capacity) {
    return new DrawLineAndDrawRectangleBatch().setCapacity(capacity);
  }

  public static FillRectangleBatch newFillRectangleBatch() {
    return newFillRectangleBatch(Batch.DEFAULT_CAPACITY);
  }

  public static FillRectangleBatch newFillRectangleBatch(int capacity) {
    return new FillRectangleBatch().setCapacity(capacity);
  }

  public static FillShapeBatch newFillShapeBatch() {
    return newFillShapeBatch(Batch.DEFAULT_CAPACITY);
  }

  public static FillShapeBatch newFillShapeBatch(int capacity) {
    return new FillShapeBatch().setCapacity(capacity);
  }

}

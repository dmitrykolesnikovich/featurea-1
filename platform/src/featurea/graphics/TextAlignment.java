package featurea.graphics;

import featurea.util.ArrayUtil;

public class TextAlignment {

  public AlignmentX x;
  public AlignmentY y;

  public TextAlignment() {
    // no op
  }

  public TextAlignment(AlignmentX x, AlignmentY y) {
    this.x = x;
    this.y = y;
  }

  public TextAlignment setValue(String string) {
    String[] tokens = ArrayUtil.split(string);
    this.x = AlignmentX.valueOf(tokens[0]);
    this.y = AlignmentY.valueOf(tokens[1]);
    return this;
  }

  public enum AlignmentX {
    left, right, center
  }

  public enum AlignmentY {
    top, bottom, center
  }

  public static TextAlignment valueOf(String primitive) {
    return new TextAlignment().setValue(primitive);
  }

}

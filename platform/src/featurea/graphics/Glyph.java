package featurea.graphics;

import java.util.HashMap;
import java.util.Map;

// class that encapsulates individual character in FNT file
public class Glyph {

  public final Font font;
  public double xoffset;
  public double yoffset;
  public double u, v, u2, v2;
  public double width;
  public double height;
  public int ch;
  public double xadvance;
  private final Map<Integer, Integer> kerning = new HashMap<>();

  public Glyph(Font font) {
    this.font = font;
  }

  public int getKerning(int ch) {
    Integer result = kerning.get(ch);
    if (result == null) {
      result = 0;
    }
    return result;
  }

  public void setKerning(int ch, int amount) {
    kerning.put(ch, amount);
  }

}

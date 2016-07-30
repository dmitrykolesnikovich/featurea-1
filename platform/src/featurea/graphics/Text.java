package featurea.graphics;

import featurea.util.Angle;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Size;

import java.util.ArrayList;
import java.util.List;

public class Text {

  public String string = "Text";
  public Font font = Font.valueOf("featurea/fonts/default.fnt");
  public Color color = Colors.white;
  public TextAlignment alignment;
  public double x1;
  public double y1;
  public double x2;
  public double y2;
  private final List<Letter> letters = new ArrayList<>();

  public List<Letter> onLayoutLetters(double ox, double oy, double scaleX, double scaleY) {
    letters.clear();
    layout(letters, ox, oy, scaleX, scaleY);
    return letters;
  }

  public void drawOnLayer(Graphics graphics, double ox, double oy, Angle angle, double scaleX, double scaleY, boolean isFlipX, boolean isFlipY) {
    graphics.drawText(this, ox, oy, angle, scaleX, scaleY, isFlipX, isFlipY);
  }

  public Size layout(List<Letter> letters, double ox, double oy, double scaleX, double scaleY) {
    double x1 = ox + this.x1;
    double y1 = oy + this.y1;
    final double x = x1;
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      Glyph glyph = font.getGlyph(ch);
      x1 += glyph.xoffset * scaleX;
      if (i != 0) {
        x1 += font.getGlyph(string.charAt(i - 1)).getKerning(ch) * scaleX;
      }
      double lineHeight = (glyph.yoffset + glyph.height) * scaleY;
      Letter letter = new Letter(glyph);
      letter.setRectangle(x1, y1 - lineHeight, x1 + glyph.width * scaleX, y1 - lineHeight + glyph.height * scaleY);
      letter.color = this.color;
      letters.add(letter);
      x1 += glyph.xadvance * scaleX;
    }
    double dx = 0;
    double width = x1 - x;
    if (alignment != null) {
      if (alignment.x == TextAlignment.AlignmentX.left) {
        dx = 0;
      }
      if (alignment.x == TextAlignment.AlignmentX.right) {
        dx = width() * scaleX - width;
      }
      if (alignment.x == TextAlignment.AlignmentX.center) {
        dx = (width() * scaleX - width) / 2;
      }
    }
    double dy = 0;
    double height = font.lineHeight * scaleY;
    if (alignment != null) {
      if (alignment.y == TextAlignment.AlignmentY.top) {
        dy = 0;
      }
      if (alignment.y == TextAlignment.AlignmentY.bottom) {
        dy = height() * scaleY - height;
      }
      if (alignment.y == TextAlignment.AlignmentY.center) {
        dy = (height() * scaleY - height) / 2;
      }
    }
    for (Letter letter : letters) {
      letter.x1 += dx;
      letter.y1 += dy;
      letter.x2 += dx;
      letter.y2 += dy;
    }
    return new Size(width, height);
  }

  public Text setAlignment(TextAlignment alignment) {
    this.alignment = alignment;
    return this;
  }

  public Text setFont(Font font) {
    this.font = font;
    return this;
  }

  public Text setColor(Color color) {
    this.color = color;
    return this;
  }

  public static class Letter {

    public final Glyph glyph;
    public double x1;
    public double y1;
    public double x2;
    public double y2;
    public Color color;

    public Letter(Glyph glyph) {
      this.glyph = glyph;
    }

    public void setRectangle(double x1, double y1, double x2, double y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }

    public void drawOnLayer(Graphics graphics, Angle angle, double ox, double oy, Color color, boolean isFlipX, boolean isFlipY) {
      // no op
    }

  }

  public double width() {
    return x2 - x1;
  }

  public double height() {
    return y2 - y1;
  }

}

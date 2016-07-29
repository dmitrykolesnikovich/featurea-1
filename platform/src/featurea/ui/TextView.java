package featurea.ui;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Layer;
import featurea.geometry.Shape;
import featurea.graphics.*;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.util.Vector;
import featurea.xml.XmlNode;

import java.util.List;

public class TextView extends Shape implements Area, XmlNode {

  public final Text text = new Text();
  private boolean isVisible = true;
  private UILayer layer;
  public final GraphicsBuffer graphics = new GraphicsBuffer(this) {
    @Override
    public Layer getLayer() {
      return layer;
    }
  };

  public void setColorHex(String hexString) {
    setColor(new Color().setValue(hexString));
  }

  public TextView setText(String text) {
    this.text.string = text;
    return this;
  }

  public TextView setFont(Font font) {
    text.setFont(font);
    return this;
  }

  public TextView setAlignment(TextAlignment alignment) {
    text.alignment = alignment;
    return this;
  }

  public TextView setColor(Color color) {
    text.color = color;
    return this;
  }

  public TextView setTextBounds(float x1, float y1, float x2, float y2) {
    text.x1 = x1;
    text.y1 = y1;
    text.x2 = x2;
    text.y2 = y2;
    return this;
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (isVisible()) {
      text.drawOnLayer(graphics, (float) ox(), (float) oy(), angle,
          (float) getScaleX(), (float) getScaleY(), isFlipX(), isFlipY());
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    // no op
  }

  @Override
  public List listAreas() {
    return null;
  }


  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    result.setRectangle(left(), top(), right(), bottom());
    result.color = Colors.black;
    if (position != null) {
      result.isSelected = contains(position.x, position.y);
    }
    result.setPosition(this.getPosition());
  }

  public void setLayer(UILayer layer) {
    this.layer = layer;
  }

  public void onAdd() {
    // no op
  }

  public void onRemove() {
    // no op
  }

  public Layer getLayer() {
    return layer;
  }

}

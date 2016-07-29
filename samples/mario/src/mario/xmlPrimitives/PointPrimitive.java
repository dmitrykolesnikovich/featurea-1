package mario.xmlPrimitives;

import featurea.app.MediaPlayer;
import featurea.graphics.Graphics;
import featurea.input.Key;
import featurea.platformer.physics.Body;
import featurea.util.ArrayUtil;
import featurea.util.CastUtil;
import featurea.util.Colors;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;
import featurea.xml.XmlUtil;
import mario.layers.BodyLayer;
import mario.objects.hero.World;
import mario.objects.landscape.Block;
import mario.util.BonusBlocksProjection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PointPrimitive extends XmlPrimitive {

  private static final List<String> sharedKeys = new ArrayList<>();

  static {
    Collections.addAll(sharedKeys, "flower", "star", "levelUp", "coin");
  }

  private final BonusBlocksProjection bonusBlocks = new BonusBlocksProjection();

  private Body selectedBody;

  public PointPrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    super(mediaPlayer, xmlTag, key);
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    if (id == 0) {
      BodyLayer bodyLayer = xmlTag.getResource();
      this.selectedBody = bodyLayer.findBlock((int) x, (int) y);
    }
    return false;
  }

  @Override
  public void onDraw(Graphics graphics) {
    super.onDraw(graphics);
    if (graphics.containsDrawLine()) {
      if (selectedBody != null) {
        double x1 = selectedBody.left();
        double y1 = selectedBody.top();
        double x2 = selectedBody.right();
        double y2 = selectedBody.bottom();
        graphics.drawRectangle(x1, y1, x2, y2, Colors.red);
      }
    }
    if (!graphics.containsDrawTexture()) {
      for (Block block : bonusBlocks.subList(getWorld().projection)) {
        block.getBonus().debugDraw(graphics);
      }
    }
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    if (id == 0) {
      if (selectedBody != null) {
        editValue(0);
        save();
      }
    }
    return false;
  }

  @Override
  public void onKeyUp(Key key) {
    if (selectedBody != null) {
      if (getSize(this.key) == 3) {
        if (key == Key.DPAD_DOWN) {
          editValue(-1);
          save();
        } else if (key == Key.DPAD_UP) {
          editValue(1);
          save();
        }
      }
      if (key == Key.DEL) {
        delete(this.key);
        save();
        selectedBody = null;
      }
    }
  }

  /*private API*/

  private void editValue(int zIncrement) {
    boolean exists = exists(zIncrement);
    String value = xmlTag.getAttribute(key);
    if (value == null) {
      value = "";
    }
    if (!exists) {
      value += "," + ((int) selectedBody.ox()) + ", " + ((int) selectedBody.oy());
      if (getSize(key) == 3) {
        value += ", 1";
      }
    }
    value = value.trim();
    if (value.startsWith(",")) {
      value = value.substring(1, value.length());
    }
    saveValue(value);
  }

  private void saveValue(String value) {
    xmlTag.setAttribute(key, value);
    removeFromSharedAttributes();
  }

  private int getSize(String key) {
    int size = mediaPlayer.project.xmlFormatter.getSize(xmlTag.name + "." + key);
    if (size == -1) {
      throw new IllegalArgumentException("size == -1");
    }
    return size;
  }

  private boolean exists(int zIncrement) {
    boolean result = false;
    String value = xmlTag.getAttribute(key);
    if (value == null) {
      value = "";
    }
    int[] array = CastUtil.castValue(value, int[].class);
    int size = getSize(key);
    int[][] chunks = ArrayUtil.twoDimensions(array, size);
    for (int[] chunk : chunks) {
      int x = chunk[0];
      int y = chunk[1];
      if (selectedBody.insideRectangle(x, y)) {
        if (size == 3) {
          chunk[2] += zIncrement;
        }
        result = true;
        String text = ArrayUtil.toString(chunks);
        xmlTag.setAttribute(key, text);
        break;
      }
    }

    return result;
  }

  private void removeFromSharedAttributes() {
    if (sharedKeys.contains(key)) {
      for (String sharedAttribute : sharedKeys) {
        if (!sharedAttribute.equals(key)) {
          delete(sharedAttribute);
        }
      }
    }
  }

  private void delete(String key) {
    String value = xmlTag.getAttribute(key);
    if (value == null) {
      return;
    }
    int[] array = CastUtil.castValue(value, int[].class);
    int[][] chunks = ArrayUtil.twoDimensions(array, getSize(key));
    List<int[]> selectedChunks = new ArrayList<>();
    for (int[] chunk : chunks) {
      int x = chunk[0];
      int y = chunk[1];
      if (selectedBody.insideRectangle(x, y)) {
        selectedChunks.add(chunk);
      }
    }
    chunks = ArrayUtil.remove(chunks, selectedChunks);
    String text = "";
    if (chunks.length != 0) {
      text = ArrayUtil.toString(chunks);
    }
    xmlTag.setAttribute(key, text);
  }

  private World getWorld() {
    return (World) XmlUtil.getLayer(xmlTag, mediaPlayer.app.screen);
  }

}

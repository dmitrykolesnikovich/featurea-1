package featurea.opengl;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.util.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TexturePack {

  public final Map<String, TexturePart> parts = new HashMap<String, TexturePart>();
  public final String file;

  public TexturePack(String file) {
    this.file = file;
    parsePackFile(file);
  }

  private void parsePackFile(String file) {
    String text = Context.getFiles().getText(file);
    text = text.trim();
    String[] tokens = text.split("\n\n");
    for (String token : tokens) {
      token = token.trim();
      if (token.isEmpty()) {
        continue;
      }
      String[] lines = token.split("\n");
      String firstLine = lines[0];
      TexturePart part = new TexturePart(firstLine);
      parts.put(part.partFile, part);
      try {
        String secondLine = lines[1];
        part.size = new Size().setValue(secondLine.trim().replace("size: ", ""));
      } catch (Exception e) {
        e.printStackTrace();
      }
      int x = 0, y = 0;
      String name = null;
      for (int i = 2; i < lines.length; i++) {
        String line = lines[i].trim();
        if (line.endsWith(".png")) {
          name = line;
        } else if (line.startsWith("rotate: ")) {
          String value = line.replaceAll("rotate: ", "");
          double angle = value.equals("true") ? 90 : 0;
          if (angle != 0) {
            throw new IllegalArgumentException("");
          }
        } else if (line.startsWith("xy: ")) {
          String value = line.replaceAll("xy: ", "");
          String[] xy = value.split(",");
          x = Integer.valueOf(xy[0].trim());
          y = Integer.valueOf(xy[1].trim());
        } else if (line.startsWith("size: ")) {
          String value = line.replaceAll("size: ", "");
          String[] wh = value.split(",");
          int w = Integer.valueOf(wh[0].trim());
          int h = Integer.valueOf(wh[1].trim());
          Texture texture = new Texture(name, part, x, y, x + w, y + h);
          part.textures.put(texture.file, texture);
        }
      }
    }
  }

  public Texture getTexture(String file) {
    for (TexturePart part : parts.values()) {
      Texture result = part.textures.get(file);
      if (result != null) {
        return result;
      }
    }
    System.err.println("Texture not found: " + file);
    return null;
  }

  private final List<Texture> texturesResult = new ArrayList<>();

  public List<Texture> getTextures() {
    texturesResult.clear();
    for (TexturePart part : parts.values()) {
      texturesResult.addAll(part.textures.values());
    }
    return texturesResult;
  }

  public boolean isLoad() {
    for (TexturePart part : parts.values()) {
      if (!part.isLoad()) {
        return false;
      }
    }
    return true;
  }

}

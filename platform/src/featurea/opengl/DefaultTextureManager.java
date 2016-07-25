package featurea.opengl;

import java.util.HashMap;
import java.util.Map;

public class DefaultTextureManager extends TextureManager {

  public final Map<String, Texture> textures = new HashMap<>();

  @Override
  public Texture getTexture(String file) {
    return textures.get(file);
  }

  @Override
  public Iterable<Texture> getTextures() {
    return textures.values();
  }

  @Override
  public void load(String file) throws TextureNotFoundException {
    Texture texture = textures.get(file);
    if (texture == null) {
      TexturePart texturePart = new TexturePart(file);
      texturePart.load();
      texture = new Texture(file, texturePart, 0, 0, texturePart.size.width, texturePart.size.height);
      texture.load();
      textures.put(file, texture);
    }
  }

  @Override
  public void release(String file) {
    Texture texture = textures.get(file);
    if (texture != null) {
      texture.release();
      textures.remove(texture.file);
    }
  }

}

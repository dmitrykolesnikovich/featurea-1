package featurea.opengl;

import featurea.app.Context;
import featurea.util.UnpackedFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TexturePacker extends TextureManager {

  public final Map<String, TexturePack> packs = new HashMap<>();

  @Override
  public Texture getTexture(String file) {
    for (TexturePack texturePack : packs.values()) {
      Texture texture = texturePack.getTexture(file);
      if (texture != null && (texture.isLoad() || texture.part.isLoad())) {
        return texture;
      }
    }
    return null;
  }

  private final List<Texture> texturesResult = new ArrayList<>();

  @Override
  public Iterable<Texture> getTextures() {
    texturesResult.clear();
    for (TexturePack texturePack : packs.values()) {
      for (Texture texture : texturePack.getTextures()) {
        texturesResult.add(texture);
      }
    }
    return texturesResult;
  }

  @Override
  public void load(String file) throws TextureNotFoundException {
    UnpackedFiles files = (UnpackedFiles) Context.getFiles();
    String packFile = files.getPackFile(file);
    if (packFile == null || !files.exists(packFile)) {
      throw new TextureNotFoundException(file);
    }
    TexturePack texturePack = packs.get(packFile);
    if (texturePack == null) {
      texturePack = new TexturePack(packFile);
      packs.put(packFile, texturePack);
    }
    Texture texture = texturePack.getTexture(file);
    if (texture != null) {
      texture.load();
    } else {
      throw new TextureNotFoundException(file);
    }
  }

  @Override
  public void release(String file) {
    Texture texture = getTexture(file);
    if (texture != null) {
      texture.release();
    }
  }

}

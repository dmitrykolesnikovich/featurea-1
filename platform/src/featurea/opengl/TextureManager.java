package featurea.opengl;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.util.Size;

public abstract class TextureManager {

  public abstract Texture getTexture(String file);

  public abstract Iterable<Texture> getTextures();

  public abstract void load(String file) throws TextureNotFoundException;

  public abstract void release(String file);

}

package featurea.app;

import featurea.opengl.TextureNotFoundException;
import featurea.opengl.TexturePack;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Loader {

  private final MediaPlayer mediaPlayer;

  public Loader(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public interface Listener {
    void onLoad(double progress);
  }

  private enum Type {
    load, release
  }

  public static class Asset {
    public final String file;
    public final Type type;

    public Asset(String file, Type type) {
      this.file = file;
      this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Asset) {
        Asset asset = (Asset) obj;
        return asset.file.equals(file) && asset.type == type;
      }
      return false;
    }
  }

  public Listener listener;

  private final Queue<Asset> notYetProcessed = new LinkedList<Asset>() {
    @Override
    public boolean add(Asset asset) {
      if (!contains(asset)) {
        return super.add(asset);
      } else {
        return false;
      }
    }
  };
  private final Queue<Asset> alreadyProcessed = new LinkedList<>();

  public Loader load(List<String> files) {
    return load(files.toArray(new String[files.size()]));
  }

  public Loader load(String... files) {
    for (String file : files) {
      if (isDir(file)) {
        for (String child : mediaPlayer.getFiles().getChildren(file)) {
          notYetProcessed.add(new Asset(child, Type.load));
        }
      } else {
        notYetProcessed.add(new Asset(file, Type.load));
      }
    }
    return this;
  }

  public void release(List<String> files) {
    release(files.toArray(new String[files.size()]));
  }

  public void release(String... files) {
    for (String file : files) {
      notYetProcessed.add(new Asset(file, Type.release));
    }
  }

  public Asset nextAsset() {
    Asset asset = poll();
    while (asset != null && areAllPacksLoaded() && isPng(asset.file)) {
      asset = poll();
    }
    return asset;
  }

  public void load(Asset asset) {
    switch (asset.type) {
      case load: {
        try {
          performLoad(asset.file);
        } catch (TextureNotFoundException e) {
          e.printStackTrace();
        }
        break;
      }
      case release: {
        performRelease(asset.file);
        break;
      }
    }
    alreadyProcessed.add(asset);
    if (!isEmpty()) {
      double progress = getProgress();
      if (listener != null) {
        listener.onLoad(progress);
      }
    }
  }

  public double getProgress() {
    return (double) alreadyProcessed.size() / (double) (notYetProcessed.size() + alreadyProcessed.size());
  }

  public boolean isEmpty() {
    return notYetProcessed.size() + alreadyProcessed.size() == 0;
  }

  public void clear() {
    notYetProcessed.clear();
    alreadyProcessed.clear();
  }

  /*private static API*/

  private Asset poll() {
    return notYetProcessed.poll();
  }

  private void performLoad(String filePath) throws TextureNotFoundException {
    if (isPng(filePath)) {
      mediaPlayer.render.load(filePath);
    } else if (isMp3(filePath)) {
      mediaPlayer.render.load(filePath);
      mediaPlayer.audio.load(filePath);
    }
  }

  private void performRelease(String file) {
    if (isPng(file)) {
      mediaPlayer.render.release(file);
    } else if (isMp3(file)) {
      mediaPlayer.audio.release(file);
    }
  }

  private static boolean isPng(String filePath) {
    return filePath.endsWith(".png");
  }

  private static boolean isMp3(String filePath) {
    return filePath.endsWith(".mp3");
  }

  private static boolean isDir(String filePath) {
    return !filePath.contains(".");
  }

  private boolean areAllPacksLoaded() {
    Map<String, TexturePack> packs = mediaPlayer.render.texturePacker.packs;
    if (mediaPlayer.isProduction() && mediaPlayer.project.packProperties.properties.size() == packs.size()) {
      for (TexturePack pack : packs.values()) {
        if (!pack.isLoad()) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

}

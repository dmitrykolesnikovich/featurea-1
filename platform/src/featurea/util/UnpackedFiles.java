package featurea.util;

import featurea.app.Context;
import featurea.app.Project;
import featurea.opengl.Texture;
import featurea.opengl.TexturePack;
import featurea.opengl.TexturePacker;

import java.util.Iterator;
import java.util.List;

public class UnpackedFiles extends Files {

  private final Project project;

  public UnpackedFiles(Project project) {
    this.project = project;
    setProduction(false);
  }

  public void setProduction(boolean isProduction) {
    this.classPath = new ClassPath(isProduction ? ClassPath.class.getClassLoader() : null);
  }

  @Override
  public List<String> getChildren(String dir, String... extensions) {
    List<String> result = super.getChildren(dir, extensions);
    unpack(result, dir, extensions);
    return result;
  }

  private void unpack(List<String> result, String dir, String... extensions) {
    if (project.isProduction()) {
      TexturePacker texturePacker = (TexturePacker) Context.getRender().getTextureManager();
      Iterator<String> iterator = result.iterator();
      while (iterator.hasNext()) {
        String file = iterator.next();
        if (file.contains(".part")) {
          iterator.remove();
        }
      }
      String packFile = getPackFile(dir);
      if (packFile != null) {
        TexturePack texturePack = newPackOrOldOne(texturePacker, packFile);
        for (Texture texture : texturePack.getTextures()) {
          String file = texture.file;
          if (file.startsWith(dir) && FileUtil.filterByExtensions(file, extensions)) {
            result.add(file);
          }
        }
      } else {
        result.add(dir);
      }
    }
  }

  public String getPackFile(String dir) {
    Properties packConfig = project.packProperties;
    try {
      while (true) {
        for (String value : packConfig.values()) {
          String[] tokens;
          if (value.isEmpty()) {
            tokens = new String[]{""};
          } else {
            tokens = StringUtil.split(value, ",");
          }
          for (String token : tokens) {
            if (token.equals(dir)) {
              return packConfig.getKey(value);
            }
          }
        }
        if (dir.isEmpty()) {
          break;
        } else {
          dir = FileUtil.getDir(dir);
        }
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /*private API*/

  private TexturePack newPackOrOldOne(TexturePacker texturePacker, String file) {
    TexturePack result = texturePacker.packs.get(file);
    if (result == null) {
      result = new TexturePack(file);
      texturePacker.packs.put(file, result);
    }
    return result;
  }

}

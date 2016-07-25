package featurea.graphics;

import featurea.app.Context;
import featurea.opengl.Shader;
import featurea.util.Angle;
import featurea.util.Color;
import featurea.util.Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sprite {

  public static final double DEFAULT_FPS = 12;
  public final List<String> sheet = new ArrayList<>();
  public double fps = DEFAULT_FPS;
  private boolean isLoop = true;
  public final Color color = new Color();
  public final Size size = new Size();
  private Object file;
  public int sheetIndex;
  private boolean isStop;
  private double currentTime;
  private final List<Shader> shaders = new ArrayList<>();
  public Runnable stopListener;

  public void setProgress(double progress) {
    sheetIndex = (int) Math.round((sheet.size() - 1) * progress);
  }

  public double getProgress() {
    return ((double) sheetIndex + 1) / sheet.size();
  }

  public String getCurrentFile() {
    if (sheet.isEmpty()) {
      return null;
    }
    if (sheetIndex >= sheet.size()) {
      sheetIndex = 0;
    }
    return sheet.get(sheetIndex);
  }

  public void onTick(double elapsedTime) {
    if (sheet.isEmpty()) {
      return;
    }
    if (isStop) {
      return;
    }
    currentTime += elapsedTime;
    double oneFrameDuration = 1000f / fps;
    double allFramesDuration = oneFrameDuration * sheet.size();
    if (currentTime >= allFramesDuration && !isLoop()) {
      sheetIndex = sheet.size() - 1;
      isStop = true;
      if (stopListener != null) {
        stopListener.run();
      }
    } else {
      currentTime %= allFramesDuration;
      sheetIndex = (int) (currentTime / oneFrameDuration);
    }
  }

  public Sprite setSize(double width, double height) {
    size.setValue(width, height);
    return this;
  }

  public Sprite setSize(Size size) {
    return setSize(size.width, size.height);
  }

  public Sprite setFile(String file) {
    if (!file.equals(this.file)) {
      this.file = file;
      sheet.clear();
      if (file.endsWith(".png")) {
        sheet.add(file);
      } else {
        List<String> files = Context.getFiles().getChildren(file, ".png");
        sheet.addAll(files);
      }
      reset();
    }
    return this;
  }

  public void setFile(String... file) {
    if (!file.equals(this.file)) {
      this.file = file;
      sheet.clear();
      sheet.addAll(Arrays.asList(file));
      reset();
    }
  }

  public Object getFile() {
    return file;
  }

  public Sprite setFps(double fps) {
    this.fps = fps;
    return this;
  }

  public final void draw(Graphics graphics, double ox, double oy, Angle angle, boolean isFlipX, boolean isFlipY, double scaleX, double scaleY) {
    double halfWidth = getWidth() / 2;
    double halfHeight = getHeight() / 2;
    double x1 = ox - halfWidth * scaleX;
    double y1 = oy - halfHeight * scaleY;
    double x2 = ox + halfWidth * scaleX;
    double y2 = oy + halfHeight * scaleY;
    draw(graphics, x1, y1, x2, y2, ox, oy, angle, isFlipX, isFlipY);
  }

  public double getWidth() {
    return size.width;
  }

  public double getHeight() {
    return size.height;
  }

  public void draw(Graphics graphics, double x1, double y1, double x2, double y2, double ox, double oy, Angle angle, boolean isFlipX, boolean isFlipY) {
    String currentFile = getCurrentFile();
    if (currentFile != null) {
      graphics.drawTexture(currentFile, x1, y1, x2, y2, angle, ox, oy, color, isFlipX, isFlipY, shaders);
    }
  }


  public void reset() {
    sheetIndex = 0;
    isStop = false;
    currentTime = 0;
  }

  public void add(Shader shader) {
    shaders.add(shader);
  }

  public double getCurrentTime() {
    return currentTime;
  }

  public boolean isStop() {
    return isStop;
  }

  public boolean isLoop() {
    return isLoop;
  }

  public void setLoop(boolean isLoop) {
    this.isLoop = isLoop;
  }

  public void setStop(boolean isStop) {
    this.isStop = isStop;
  }

}

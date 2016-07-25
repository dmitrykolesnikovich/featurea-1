package com.badlogic.gdx.texturePacker;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.MathUtils;
import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.util.Files;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TexturePackerManager {

  private final Settings settings;
  private final MaxRectsPacker maxRectsPacker;
  private final ImageProcessor imageProcessor;

  public TexturePackerManager(Settings settings) {
    this.settings = settings;
    if (settings.POT) {
      if (settings.MAX_WIDTH != MathUtils.nextPowerOfTwo(settings.MAX_WIDTH))
        throw new RuntimeException("If pot is true, maxWidth must be a power of two: " + settings.MAX_WIDTH);
      if (settings.MAX_HEIGHT != MathUtils.nextPowerOfTwo(settings.MAX_HEIGHT))
        throw new RuntimeException("If pot is true, maxHeight must be a power of two: " + settings.MAX_HEIGHT);
    }
    maxRectsPacker = new MaxRectsPacker(settings);
    imageProcessor = new ImageProcessor(settings);
  }

  public void addImage(String root, File file, Files files) {
    imageProcessor.addImage(root, file, files);
  }

  public void pack(File outputDir, String packFileName) {
    outputDir.mkdirs();
    System.out.println("pack to " + outputDir);
    Array<Page> pages = maxRectsPacker.pack(imageProcessor.getImages());
    writeImages(outputDir, pages, packFileName);
    try {
      writePackFile(outputDir, pages, packFileName);
    } catch (IOException ex) {
      throw new RuntimeException("Error writing pack file.", ex);
    }
  }

  private void writeImages(File outputDir, Array<Page> pages, String packFileName) {
    String imageName = packFileName;
    int dotIndex = imageName.lastIndexOf('.');
    if (dotIndex != -1) imageName = imageName.substring(0, dotIndex);
    for (Page page : pages) {
      int width = page.width, height = page.height;
      int paddingX = settings.paddingX;
      int paddingY = settings.paddingY;
      if (settings.duplicatePadding) {
        paddingX /= 2;
        paddingY /= 2;
      }
      width -= settings.paddingX;
      height -= settings.paddingY;
      if (settings.edgePadding) {
        page.x = paddingX;
        page.y = paddingY;
        width += paddingX * 2;
        height += paddingY * 2;
      }
      if (settings.POT) {
        width = MathUtils.nextPowerOfTwo(width);
        height = MathUtils.nextPowerOfTwo(height);
      }
      width = Math.max(settings.MIN_WIDTH, width);
      height = Math.max(settings.MIN_HEIGHT, height);
      if (settings.forceSquareOutput) {
        if (width > height) {
          height = width;
        } else {
          width = height;
        }
      }
      int fileIndex = 1;
      File outputFile;
      while (true) {
        outputFile = new File(outputDir, imageName + ".part" + fileIndex + "." + settings.OUTPUT_EXTENSION);
        fileIndex++;
        if (!outputFile.exists()) break;
      }
      page.imageName = outputFile.getName();
      BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = (Graphics2D) canvas.getGraphics();
      System.out.println("Writing " + canvas.getWidth() + "x" + canvas.getHeight() + ": " + outputFile);
      for (Rect rect : page.outputRects) {
        BufferedImage image = rect.getImage(imageProcessor);
        int iw = image.getWidth();
        int ih = image.getHeight();
        int rectX = page.x + rect.x, rectY = page.y + page.height - rect.y - rect.height;

        // >> IMPORTANT edgeTiling logic
        if (settings.isEdgeTilingPath(rect.name)) {
          rectX -= 1;
          rectY -= 1;
          iw += 2;
          ih += 2;
        }
        // <<

        if (settings.duplicatePadding) {
          int amountX = settings.paddingX / 2;
          int amountY = settings.paddingY / 2;
          if (rect.rotated) {
            for (int i = 1; i <= amountX; i++) {
              for (int j = 1; j <= amountY; j++) {
                plot(canvas, rectX - j, rectY + iw - 1 + i, getRGB(image, rect.name, 0, 0));
                plot(canvas, rectX + ih - 1 + j, rectY + iw - 1 + i, getRGB(image, rect.name, 0, ih - 1));
                plot(canvas, rectX - j, rectY - i, getRGB(image, rect.name, iw - 1, 0));
                plot(canvas, rectX + ih - 1 + j, rectY - i, getRGB(image, rect.name, iw - 1, ih - 1));
              }
            }
            for (int i = 1; i <= amountY; i++) {
              for (int j = 0; j < iw; j++) {
                plot(canvas, rectX - i, rectY + iw - 1 - j, getRGB(image, rect.name, j, 0));
                plot(canvas, rectX + ih - 1 + i, rectY + iw - 1 - j, getRGB(image, rect.name, j, ih - 1));
              }
            }
            for (int i = 1; i <= amountX; i++) {
              for (int j = 0; j < ih; j++) {
                plot(canvas, rectX + j, rectY - i, getRGB(image, rect.name, iw - 1, j));
                plot(canvas, rectX + j, rectY + iw - 1 + i, getRGB(image, rect.name, 0, j));
              }
            }
          } else {
            for (int i = 1; i <= amountX; i++) {
              for (int j = 1; j <= amountY; j++) {
                canvas.setRGB(rectX - i, rectY - j, getRGB(image, rect.name, 0, 0));
                canvas.setRGB(rectX - i, rectY + ih - 1 + j, getRGB(image, rect.name, 0, ih - 1));
                canvas.setRGB(rectX + iw - 1 + i, rectY - j, getRGB(image, rect.name, iw - 1, 0));
                canvas.setRGB(rectX + iw - 1 + i, rectY + ih - 1 + j, getRGB(image, rect.name, iw - 1, ih - 1));
              }
            }
            for (int i = 1; i <= amountY; i++) {
              copy(image, rect.name, 0, 0, iw, 1, canvas, rectX, rectY - i, rect.rotated);
              copy(image, rect.name, 0, ih - 1, iw, 1, canvas, rectX, rectY + ih - 1 + i, rect.rotated);
            }
            for (int i = 1; i <= amountX; i++) {
              copy(image, rect.name, 0, 0, 1, ih, canvas, rectX - i, rectY, rect.rotated);
              copy(image, rect.name, iw - 1, 0, 1, ih, canvas, rectX + iw - 1 + i, rectY, rect.rotated);
            }
          }
        }
        copy(image, rect.name, 0, 0, iw, ih, canvas, rectX, rectY, rect.rotated);
        if (settings.debug) {
          g.setColor(Color.magenta);
          g.drawRect(rectX, rectY, rect.width - settings.paddingX - 1, rect.height - settings.paddingY - 1);
        }
      }
      if (settings.bleed && !settings.premultiplyAlpha && !settings.OUTPUT_EXTENSION.equalsIgnoreCase("jpg")) {
        canvas = new ColorBleedEffect().processImage(canvas, 2);
        g = (Graphics2D) canvas.getGraphics();
      }
      if (settings.debug) {
        g.setColor(Color.magenta);
        g.drawRect(0, 0, width - 1, height - 1);
      }
      ImageOutputStream ios = null;
      try {
        if (settings.OUTPUT_EXTENSION.equalsIgnoreCase("jpg")) {
          BufferedImage newImage = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
          newImage.getGraphics().drawImage(canvas, 0, 0, null);
          canvas = newImage;
          Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
          ImageWriter writer = writers.next();
          ImageWriteParam param = writer.getDefaultWriteParam();
          param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
          param.setCompressionQuality(settings.jpegQuality);
          ios = ImageIO.createImageOutputStream(outputFile);
          writer.setOutput(ios);
          writer.write(null, new IIOImage(canvas, null, null), param);
        } else {
          if (settings.premultiplyAlpha) canvas.getColorModel().coerceData(canvas.getRaster(), true);
          ImageIO.write(canvas, "png", outputFile);
        }
        page.canvasWidth = canvas.getWidth();
        page.canvasHeight = canvas.getHeight();
      } catch (IOException ex) {
        throw new RuntimeException("Error writing file: " + outputFile, ex);
      } finally {
        if (ios != null) {
          try {
            ios.close();
          } catch (Exception ignored) {
          }
        }
      }
    }
  }

  private void plot(BufferedImage dst, int x, int y, int argb) {
    if (0 <= x && x < dst.getWidth() && 0 <= y && y < dst.getHeight()) dst.setRGB(x, y, argb);
  }

  private void copy(BufferedImage src, String file, int x, int y, int w, int h, BufferedImage dst, int dx, int dy, boolean rotated) {
    System.out.println("start: " + file);
    if (rotated) {
      for (int i = 0; i < w; i++)
        for (int j = 0; j < h; j++)
          dst.setRGB(dx + j, dy + w - i - 1, getRGB(src, file, x + i, y + j));
    } else {
      for (int i = 0; i < w; i++)
        for (int j = 0; j < h; j++)
          dst.setRGB(dx + i, dy + j, getRGB(src, file, x + i, y + j));
    }
    System.out.println("finish");
  }

  private void writePackFile(File outputDir, Array<Page> pages, String packFileName) throws IOException {
    File packFile = new File(outputDir, packFileName);
    FileWriter writer = new FileWriter(packFile, true);
    for (Page page : pages) {
      writer.write("\n" + page.imageName + "\n");
      writer.write("size: " + page.canvasWidth + "," + page.canvasHeight + "\n");
      writer.write("format: RGBA8888\n");
      writer.write("filter: Nearest,Nearest\n");
      writer.write("repeat: none\n");
      for (Rect rect : page.outputRects) {
        writeRect(writer, page, rect);
        for (Alias alias : rect.aliases) {
          Rect aliasRect = new Rect();
          aliasRect.set(rect);
          alias.apply(aliasRect);
          writeRect(writer, page, aliasRect);
        }
      }
    }
    writer.close();
  }

  private void writeRect(FileWriter writer, Page page, Rect rect) throws IOException {
    writer.write(rect.name + "\n");
    writer.write("  rotate: " + rect.rotated + "\n");
    writer.write("  xy: " + (page.x + rect.x) + ", " + (page.y + page.height - rect.height - rect.y) + "\n");
    writer.write("  size: " + rect.regionWidth + ", " + rect.regionHeight + "\n");
    if (rect.splits != null) {
      writer.write("  split: "
          + rect.splits[0] + ", " + rect.splits[1] + ", " + rect.splits[2] + ", " + rect.splits[3] + "\n");
    }
    if (rect.pads != null) {
      if (rect.splits == null) writer.write("  split: 0, 0, 0, 0\n");
      writer.write("  pad: " + rect.pads[0] + ", " + rect.pads[1] + ", " + rect.pads[2] + ", " + rect.pads[3] + "\n");
    }
    writer.write("  orig: " + rect.originalWidth + ", " + rect.originalHeight + "\n");
    writer.write("  offset: " + rect.offsetX + ", " + (rect.originalHeight - rect.regionHeight - rect.offsetY) + "\n");
    writer.write("  index: " + rect.index + "\n");
  }

  // >> IMPORTANT edgeTiling logic
  private int getRGB(BufferedImage image, String file, int x, int y) {
    if (settings.isEdgeTilingPath(file)) {
      if (x >= 1 && x <= image.getWidth() &&
          y >= 1 && y <= image.getHeight()) {
        return image.getRGB(x - 1, y - 1);
      } else {
        if (x == 0) {
          x = 0;
        } else if (x == image.getWidth() - 1 + 2) {
          x -= 2;
        } else {
          x -= 1;
        }
        if (y == 0) {
          y = 0;
        } else if (y == image.getHeight() - 1 + 2) {
          y -= 2;
        } else {
          y -= 1;
        }
        return image.getRGB(x, y);
      }
    } else {
      return image.getRGB(x, y);
    }
  }
  // <<

  public static class Page {
    public String imageName;
    public Array<Rect> outputRects, remainingRects;
    public float occupancy;
    public int x, y, width, height;
    public int canvasWidth, canvasHeight;
  }

  public static class Alias {
    public String name;
    public int index;
    public int[] splits;
    public int[] pads;
    public int offsetX, offsetY, originalWidth, originalHeight;

    public Alias(Rect rect) {
      name = rect.name;
      index = rect.index;
      splits = rect.splits;
      pads = rect.pads;
      offsetX = rect.offsetX;
      offsetY = rect.offsetY;
      originalWidth = rect.originalWidth;
      originalHeight = rect.originalHeight;
    }

    public void apply(Rect rect) {
      rect.name = name;
      rect.index = index;
      rect.splits = splits;
      rect.pads = pads;
      rect.offsetX = offsetX;
      rect.offsetY = offsetY;
      rect.originalWidth = originalWidth;
      rect.originalHeight = originalHeight;
    }
  }

  public static class Rect {
    public String name;
    public int offsetX, offsetY, regionWidth, regionHeight, originalWidth, originalHeight;
    public int x, y, width, height;
    public int index;
    public boolean rotated;
    public Set<Alias> aliases = new HashSet<Alias>();
    public int[] splits;
    public int[] pads;
    public boolean canRotate = true;
    private boolean isPatch;
    private BufferedImage image;
    private File file;
    int score1, score2;

    public Rect(BufferedImage source, int left, int top, int newWidth, int newHeight, boolean isPatch) {
      image = new BufferedImage(source.getColorModel(), source.getRaster().createWritableChild(left, top, newWidth, newHeight,
          0, 0, null), source.getColorModel().isAlphaPremultiplied(), null);
      offsetX = left;
      offsetY = top;
      regionWidth = newWidth;
      regionHeight = newHeight;
      originalWidth = source.getWidth();
      originalHeight = source.getHeight();
      width = newWidth;
      height = newHeight;
      this.isPatch = isPatch;
    }

    public void unloadImage(File file) {
      this.file = file;
      image = null;
    }

    public BufferedImage getImage(ImageProcessor imageProcessor) {
      if (image != null) return image;
      BufferedImage image;
      try {
        image = ImageIO.read(file);
      } catch (IOException ex) {
        throw new RuntimeException("Error reading image: " + file, ex);
      }
      if (image == null) throw new RuntimeException("Unable to read image: " + file);
      String name = this.name;
      if (isPatch) name += ".9";
      return imageProcessor.processImage(image, name).getImage(null);
    }

    Rect() {
    }

    Rect(Rect rect) {
      x = rect.x;
      y = rect.y;
      width = rect.width;
      height = rect.height;
    }

    void set(Rect rect) {
      name = rect.name;
      image = rect.image;
      offsetX = rect.offsetX;
      offsetY = rect.offsetY;
      regionWidth = rect.regionWidth;
      regionHeight = rect.regionHeight;
      originalWidth = rect.originalWidth;
      originalHeight = rect.originalHeight;
      x = rect.x;
      y = rect.y;
      width = rect.width;
      height = rect.height;
      index = rect.index;
      rotated = rect.rotated;
      aliases = rect.aliases;
      splits = rect.splits;
      pads = rect.pads;
      canRotate = rect.canRotate;
      score1 = rect.score1;
      score2 = rect.score2;
      file = rect.file;
      isPatch = rect.isPatch;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Rect other = (Rect) obj;
      if (name == null) {
        if (other.name != null) return false;
      } else if (!name.equals(other.name)) return false;
      return true;
    }

    @Override
    public String toString() {
      return name + "[" + x + "," + y + " " + width + "x" + height + "]";
    }
  }

  public static class Settings {

    public static final boolean POT = true;
    public int paddingX = 4, paddingY = 4;
    public boolean edgePadding = true;
    public boolean duplicatePadding = false;
    public boolean rotation;
    public static final int MIN_WIDTH = 16, MIN_HEIGHT = 16;
    public static final int MAX_WIDTH = 2048, MAX_HEIGHT = 2048;
    public boolean forceSquareOutput = false;
    public boolean stripWhitespaceX, stripWhitespaceY;
    public int alphaThreshold;
    public boolean alias = true;
    public final static String OUTPUT_EXTENSION = "png";
    public float jpegQuality = 0.9f;
    public boolean ignoreBlankImages = true;
    public boolean fast;
    public boolean debug;
    public boolean premultiplyAlpha;
    public boolean useIndexes = true;
    public boolean bleed = true;
    public boolean limitMemory = true;

    public String[] outlinePaths;

    public boolean isEdgeTilingPath(String file) {
      return true;
    }
  }

}

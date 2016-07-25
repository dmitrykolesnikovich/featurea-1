package featurea.graphics;

import featurea.app.Context;
import featurea.util.*;

import java.io.*;
import java.util.*;
import java.util.ArrayList;

public class Font {

  public String fntFile;
  public String pngFile;
  private List<Glyph> glyphs = new ArrayList<>();
  private Map<Integer, Glyph> glyphsMap = new HashMap<>();
  private double descent;
  public double lineHeight;
  private double spaceWidth;
  private double xHeight;
  private double capHeight;
  private double ascent;
  private double down;
  private double scaleW;
  private double scaleH;

  public Glyph getGlyph(int ch) {
    Glyph result = glyphsMap.get(ch);
    if (result == null) {
      result = glyphsMap.get((int) ' ');
    }
    return result;
  }

  public Font setFntFile(String fntFile) {
    this.fntFile = fntFile;
    String text = Context.getFiles().getText(fntFile);
    if (text != null) {
      onLoad(text);
    }
    return this;
  }

  private void onLoad(String text) {
    char[] xChars = {'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z'};
    char[] capChars = {'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    BufferedReader reader = new BufferedReader(new StringReader(text), 512);
    try {
      reader.readLine();
      String line = reader.readLine();
      String[] common = line.split(" ", 7);
      lineHeight = Integer.parseInt(common[1].substring(11));
      double baseLine = Integer.parseInt(common[2].substring(5));
      scaleW = Float.parseFloat(common[3].substring(7));
      scaleH = Float.parseFloat(common[4].substring(7));
      line = reader.readLine();
      String[] pageLine = line.split(" ", 4);
      String dir = FileUtil.getDir(fntFile);
      if (pageLine[2].endsWith("\"")) {
        pngFile = dir + "/" + pageLine[2].substring(6, pageLine[2].length() - 1);
      } else {
        pngFile = dir + "/" + pageLine[2].substring(5, pageLine[2].length());
      }
      descent = 0;
      while (true) {
        line = reader.readLine();
        if (line == null) break;
        if (line.startsWith("kernings ")) break;
        if (!line.startsWith("char ")) continue;
        Glyph glyph = new Glyph(this);
        glyphs.add(glyph);
        StringTokenizer tokens = new StringTokenizer(line, " =");
        tokens.nextToken();
        tokens.nextToken();
        int ch = Integer.parseInt(tokens.nextToken());
        if (ch > Character.MAX_VALUE)
          continue;
        glyph.ch = ch;
        glyphsMap.put(ch, glyph);
        tokens.nextToken();
        double srcX = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        double srcY = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        glyph.width = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        glyph.height = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        glyph.u = srcX / scaleW;
        glyph.u2 = glyph.u + glyph.width / scaleW;
        glyph.v = srcY / scaleH;
        glyph.v2 = glyph.v + glyph.height / scaleH;
        glyph.xoffset = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
        tokens.nextToken();
        glyph.xadvance = Integer.parseInt(tokens.nextToken());
        if (tokens.hasMoreTokens()) tokens.nextToken();
        if (tokens.hasMoreTokens()) {
          try {
            Integer.parseInt(tokens.nextToken());
          } catch (NumberFormatException ignored) {
          }
        }
        if (glyph.width > 0 && glyph.height > 0) {
          descent = Math.min(baseLine + glyph.yoffset, descent);
        }
      }
      while (true) {
        line = reader.readLine();
        if (line == null) break;
        if (!line.startsWith("kerning ")) break;
        StringTokenizer tokens = new StringTokenizer(line, " =");
        tokens.nextToken();
        tokens.nextToken();
        int first = Integer.parseInt(tokens.nextToken());
        tokens.nextToken();
        int second = Integer.parseInt(tokens.nextToken());
        if (first < 0 || first > Character.MAX_VALUE || second < 0 || second > Character.MAX_VALUE) continue;
        Glyph glyph = getGlyph((char) first);
        tokens.nextToken();
        int amount = Integer.parseInt(tokens.nextToken());
        if (glyph != null) {
          glyph.setKerning(second, amount);
        }
      }
      Glyph spaceGlyph = getGlyph(' ');
      if (spaceGlyph == null) {
        spaceGlyph = new Glyph(this);
        spaceGlyph.ch = (int) ' ';
        Glyph xadvanceGlyph = getGlyph('l');
        if (xadvanceGlyph == null) xadvanceGlyph = glyphs.get(0);
        spaceGlyph.xadvance = xadvanceGlyph.xadvance;
        spaceGlyph.ch = ' ';
      }
      spaceWidth = spaceGlyph != null ? spaceGlyph.xadvance + spaceGlyph.width : 1;
      Glyph xGlyph = null;
      for (int i = 0; i < xChars.length; i++) {
        xGlyph = getGlyph(xChars[i]);
        if (xGlyph != null) break;
      }
      if (xGlyph == null) xGlyph = glyphs.get(0);
      xHeight = xGlyph.height;
      Glyph capGlyph = null;
      for (int i = 0; i < capChars.length; i++) {
        capGlyph = getGlyph(capChars[i]);
        if (capGlyph != null) break;
      }
      if (capGlyph == null) {
        for (Glyph glyph : glyphs) {
          if (glyph == null || glyph.height == 0 || glyph.width == 0) continue;
          capHeight = Math.max(capHeight, glyph.height);
        }
      } else
        capHeight = capGlyph.height;
      ascent = baseLine - capHeight;
      down = -lineHeight;
    } catch (Exception ex) {
      throw new RuntimeException("Error loading font file: " + fntFile, ex);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static boolean isPngFontFile(File file) {
    if (file == null) {
      return false;
    }
    String fntFileName = file.getName().replaceAll(".png", ".fnt");
    File parentFile = file.getParentFile();
    if (parentFile == null) {
      return false;
    }
    File[] children = parentFile.listFiles();
    if (children == null) {
      return false;
    }
    for (File child : children) {
      if (fntFileName.equals(child.getName())) {
        return true;
      }
    }
    return false;
  }

  public Font setValue(String file) {
    setFntFile(file);
    return this;
  }

  public static Font valueOf(String primitive) {
    return new Font().setValue(primitive);
  }

}

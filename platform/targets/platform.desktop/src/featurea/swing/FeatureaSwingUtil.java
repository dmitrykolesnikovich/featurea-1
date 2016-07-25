package featurea.swing;

import de.matthiasmann.twl.utils.PNGDecoder;
import featurea.app.Context;
import featurea.util.FileUtil;
import featurea.util.Size;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Enumeration;

public final class FeatureaSwingUtil {

  private FeatureaSwingUtil() {
    // no op
  }

  public static BufferedImage getBufferedImage(String file) {
    if (FileUtil.isAbsolutePath(file)) {
      try {
        return ImageIO.read(new FileInputStream(file));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      return ImageIO.read(Context.getFiles().getStream(file));
    } catch (Throwable skip) {
      // no op
    }
    try {
      return ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(file));
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Size loadTexture(String file) {
    InputStream stream = Context.getFiles().getStream(file);
    if (stream == null) {
      return new Size();
    }
    try {
      PNGDecoder dec = new PNGDecoder(stream);
      double width = dec.getWidth();
      double height = dec.getHeight();
      ByteBuffer buffer = BufferUtils.createByteBuffer((int) (4 * width * height));
      dec.decode(buffer, (int) (width * 4), PNGDecoder.Format.RGBA);
      buffer.flip();
      GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
      GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
      GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int) width, (int) height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
      return new Size(width, height);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (stream != null) {
        try {
          stream.close();
          stream = null;
          java.lang.System.gc();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static IntBuffer getPixels(int textureId, int width, int height) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    IntBuffer result = BufferUtils.createIntBuffer(width * height);
    GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_SHORT_4_4_4_4, result);
    return result;
  }

  public static Size getSize(String file) {
    BufferedImage image = getBufferedImage(file);
    double width = image.getWidth();
    double height = image.getHeight();
    return new Size(width, height);
  }

  public static JFrame findFrame(Component component) {
    component.setPreferredSize(null);
    while (!(component instanceof JFrame)) {
      component = component.getParent();
    }
    return (JFrame) component;
  }

  public static void resetUI(Component component) {
    Component frame = FeatureaSwingUtil.findFrame(component);
    frame.validate();
    frame.repaint();
    component.validate();
    component.repaint();
    component.requestFocusInWindow();
  }

  public static void expandTree(JTree tree) {
    TreeNode root = (TreeNode) tree.getModel().getRoot();
    if (root != null) {
      expandTreeRecursively(tree, new TreePath(root));
    }
  }

  private static void expandTreeRecursively(JTree tree, TreePath parent) {
    tree.expandPath(parent);
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration e = node.children(); e.hasMoreElements(); ) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        expandTreeRecursively(tree, path);
      }
    }
    // tree.collapsePath(parent);
  }

  public static void setupTheme() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    UIManager.put("ScrollBarUI", "featurea.swing.BegScrollBarUI");
    UIManager.put("ScrollBar.width", 8);
  }

  public static int getDisplayWidth() {
    return Toolkit.getDefaultToolkit().getScreenSize().width;
  }

  public static int getDisplayHeight() {
    return Toolkit.getDefaultToolkit().getScreenSize().height;
  }

}

package featurea.desktop;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.swing.FeatureaSwingUtil;
import featurea.util.ArrayList;
import featurea.util.FileUtil;
import org.lwjgl.InputListener;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.jar.JarFile;

public class LwjglWindow extends AWTGLCanvas implements InputWindow {

  private long now;
  private boolean isCreate; // todo fix this shit about visibility
  private boolean isClose;
  private boolean isExit;
  public final Simulator simulator;
  public final MediaPlayer mediaPlayer;
  private final List<InputListener> keyboardInputListeners = new ArrayList<>();

  public LwjglWindow(Simulator simulator, File file, boolean isProduction) throws LWJGLException {
    Simulator.instanceCount++;
    this.simulator = simulator;
    this.mediaPlayer = new MediaPlayer();
    this.mediaPlayer.project.setProduction(isProduction);
    JarFile jarFile = FileUtil.retrieveJarFile(Simulator.class);
    if (jarFile != null) {
      this.mediaPlayer.getFiles().add(jarFile);
      this.mediaPlayer.getClassLoader().add(jarFile);
      System.out.println("jar added: " + jarFile.getName());
    } else {
      System.err.println("jar == null");
    }
    this.mediaPlayer.project.setFile(file);

    addInputListener(new SimulatorInputListener(this.mediaPlayer));
    setIgnoreRepaint(false);
    addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        requestFocusInWindow();
      }
    });
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e) {
        for (InputListener keyboardInputListener : keyboardInputListeners) {
          switch (e.getID()) {
            case KeyEvent.KEY_PRESSED: {
              keyboardInputListener.keyPressed(e);
              break;
            }
            case KeyEvent.KEY_RELEASED: {
              keyboardInputListener.keyReleased(e);
              break;
            }
          }
        }
        return false;
      }
    });
    addMouseListener(new HotFixSimulatorInputListener(simulator));
  }

  @Override
  protected void initGL() {
    Context.mediaPlayers.put(Thread.currentThread(), this.mediaPlayer);
    super.initGL();
    this.mediaPlayer.render.window = this;
  }

  @Override
  protected synchronized void paintGL() {
    try {
      long nanoTime = System.nanoTime();
      if (now == 0) {
        now = nanoTime;
      }
      double elapsedTime = (nanoTime - now) / 1_000_000.0d;

      Context.mediaPlayers.put(Thread.currentThread(), this.mediaPlayer);
      super.paintGL();

      if (!isCreate) {
        isCreate = true;
        simulator.onCreate(this.mediaPlayer);
      }

      if (isClose) { // exit
        simulator.onExit();
      } else if (isCreate) { // created
        this.mediaPlayer.app.onResize(getWidth(), getHeight());
        simulator.onInput();
        simulator.onTick(elapsedTime);
        simulator.onDrawBackground();
        simulator.onDraw(Context.gl);
        try {
          swapBuffers();
        } catch (LWJGLException ex) {
          ex.printStackTrace();
        }
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        repaint();
        now = nanoTime;
      }
      if (isExit) {
        this.mediaPlayer.app.onDestroy();
      }
    } catch (Throwable e) {
      System.out.println("breakpoint");
      e.printStackTrace(this.mediaPlayer.getFiles().err);
      e.printStackTrace();
      JOptionPane.showMessageDialog(FeatureaSwingUtil.findFrame(this), "Application has stopped unexpectedly");
      this.mediaPlayer.timer.clearModifications();
      this.mediaPlayer.app.screen = null;
    }
  }

  @Override
  public void close() {
    isClose = true;
  }

  @Override
  public void addInputListener(final InputListener inputListener) {
    addMouseListener(inputListener);
    addMouseMotionListener(inputListener);
    addMouseWheelListener(inputListener);
    keyboardInputListeners.add(inputListener);
  }

  @Override
  public void removeInputListener(InputListener inputListener) {
    removeMouseListener(inputListener);
    removeMouseMotionListener(inputListener);
    removeMouseWheelListener(inputListener);
    keyboardInputListeners.remove(inputListener);
  }

  @Override
  public void exit() {
    isExit = true;
  }

  // >> IMPORTANT do not delete this
  @Override
  public Dimension getMinimumSize() {
    return new Dimension();
  }

  public void onDoubleClick() {
    simulator.onDoubleClick();
  }
  // <<

}

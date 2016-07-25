package featurea.desktop;

import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.input.Key;
import org.lwjgl.InputListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class SimulatorInputListener extends InputListener {

  private final MediaPlayer mediaPlayer;

  public SimulatorInputListener(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  @Override
  public synchronized void down(MouseEvent e, double x, double y) {
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    mediaPlayer.input.display.down(x, y, getPointerId(e));
  }

  @Override
  public synchronized void move(MouseEvent e, double x, double y) {
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    mediaPlayer.input.display.drag(x, y, getPointerId(e));
  }

  @Override
  public synchronized void up(MouseEvent e, double x, double y) {
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    mediaPlayer.input.display.up(x, y, getPointerId(e));
  }

  @Override
  public synchronized void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    int x = e.getX();
    int y = e.getY();
    mediaPlayer.input.display.move(x, y, getPointerId(e));
  }

  @Override
  public synchronized void keyPressed(KeyEvent e) {
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    Key key = getKey(e);
    mediaPlayer.input.keyboard.keyDown(key);
  }

  @Override
  public synchronized void keyReleased(KeyEvent e) {
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    Key key = getKey(e);
    mediaPlayer.input.keyboard.keyUp(key);
  }

  @Override
  public synchronized void mouseWheelMoved(MouseWheelEvent e) {
    super.mouseWheelMoved(e);
    Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
    int x = e.getX();
    int y = e.getY();
    mediaPlayer.input.display.wheel(e.getWheelRotation(), x, y);
  }

  private static Key getKey(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ADD:
        return Key.PLUS;
      case KeyEvent.VK_SUBTRACT:
        return Key.MINUS;
      case KeyEvent.VK_0:
        return Key.NUM_0;
      case KeyEvent.VK_1:
        return Key.NUM_1;
      case KeyEvent.VK_2:
        return Key.NUM_2;
      case KeyEvent.VK_3:
        return Key.NUM_3;
      case KeyEvent.VK_4:
        return Key.NUM_4;
      case KeyEvent.VK_5:
        return Key.NUM_5;
      case KeyEvent.VK_6:
        return Key.NUM_6;
      case KeyEvent.VK_7:
        return Key.NUM_7;
      case KeyEvent.VK_8:
        return Key.NUM_8;
      case KeyEvent.VK_9:
        return Key.NUM_9;
      case KeyEvent.VK_A:
        return Key.A;
      case KeyEvent.VK_B:
        return Key.B;
      case KeyEvent.VK_C:
        return Key.C;
      case KeyEvent.VK_D:
        return Key.D;
      case KeyEvent.VK_E:
        return Key.E;
      case KeyEvent.VK_F:
        return Key.F;
      case KeyEvent.VK_G:
        return Key.G;
      case KeyEvent.VK_H:
        return Key.H;
      case KeyEvent.VK_I:
        return Key.I;
      case KeyEvent.VK_J:
        return Key.J;
      case KeyEvent.VK_K:
        return Key.K;
      case KeyEvent.VK_L:
        return Key.L;
      case KeyEvent.VK_M:
        return Key.M;
      case KeyEvent.VK_N:
        return Key.N;
      case KeyEvent.VK_O:
        return Key.O;
      case KeyEvent.VK_P:
        return Key.P;
      case KeyEvent.VK_Q:
        return Key.Q;
      case KeyEvent.VK_R:
        return Key.R;
      case KeyEvent.VK_S:
        return Key.S;
      case KeyEvent.VK_T:
        return Key.T;
      case KeyEvent.VK_U:
        return Key.U;
      case KeyEvent.VK_V:
        return Key.V;
      case KeyEvent.VK_W:
        return Key.W;
      case KeyEvent.VK_X:
        return Key.X;
      case KeyEvent.VK_Y:
        return Key.Y;
      case KeyEvent.VK_Z:
        return Key.Z;
      case KeyEvent.VK_ALT:
        return Key.ALT_LEFT;
      case KeyEvent.VK_ALT_GRAPH:
        return Key.ALT_RIGHT;
      case KeyEvent.VK_BACK_SLASH:
        return Key.BACKSLASH;
      case KeyEvent.VK_COMMA:
        return Key.COMMA;
      case KeyEvent.VK_DELETE:
        return Key.DEL;
      case KeyEvent.VK_LEFT:
        return Key.DPAD_LEFT;
      case KeyEvent.VK_RIGHT:
        return Key.DPAD_RIGHT;
      case KeyEvent.VK_UP:
        return Key.DPAD_UP;
      case KeyEvent.VK_DOWN:
        return Key.DPAD_DOWN;
      case KeyEvent.VK_ENTER:
        return Key.ENTER;
      case KeyEvent.VK_HOME:
        return Key.HOME;
      case KeyEvent.VK_MINUS:
        return Key.MINUS;
      case KeyEvent.VK_PERIOD:
        return Key.PERIOD;
      case KeyEvent.VK_PLUS:
        return Key.PLUS;
      case KeyEvent.VK_SEMICOLON:
        return Key.SEMICOLON;
      case KeyEvent.VK_SHIFT:
        return Key.SHIFT_LEFT;
      case KeyEvent.VK_SLASH:
        return Key.SLASH;
      case KeyEvent.VK_SPACE:
        return Key.SPACE;
      case KeyEvent.VK_TAB:
        return Key.TAB;
      case KeyEvent.VK_BACK_SPACE:
        return Key.DEL;
      case KeyEvent.VK_CONTROL:
        return Key.CONTROL_LEFT;
      case KeyEvent.VK_ESCAPE:
        return Key.ESCAPE;
      case KeyEvent.VK_END:
        return Key.END;
      case KeyEvent.VK_INSERT:
        return Key.INSERT;
      case KeyEvent.VK_PAGE_UP:
        return Key.PAGE_UP;
      case KeyEvent.VK_PAGE_DOWN:
        return Key.PAGE_DOWN;
      case KeyEvent.VK_F1:
        return Key.F1;
      case KeyEvent.VK_F2:
        return Key.F2;
      case KeyEvent.VK_F3:
        return Key.F3;
      case KeyEvent.VK_F4:
        return Key.F4;
      case KeyEvent.VK_F5:
        return Key.F5;
      case KeyEvent.VK_F6:
        return Key.F6;
      case KeyEvent.VK_F7:
        return Key.F7;
      case KeyEvent.VK_F8:
        return Key.F8;
      case KeyEvent.VK_F9:
        return Key.F9;
      case KeyEvent.VK_F10:
        return Key.F10;
      case KeyEvent.VK_F11:
        return Key.F11;
      case KeyEvent.VK_F12:
        return Key.F12;
      case KeyEvent.VK_COLON:
        return Key.COLON;
      case KeyEvent.VK_NUMPAD0:
        return Key.NUM_0;
      case KeyEvent.VK_NUMPAD1:
        return Key.NUM_1;
      case KeyEvent.VK_NUMPAD2:
        return Key.NUM_2;
      case KeyEvent.VK_NUMPAD3:
        return Key.NUM_3;
      case KeyEvent.VK_NUMPAD4:
        return Key.NUM_4;
      case KeyEvent.VK_NUMPAD5:
        return Key.NUM_5;
      case KeyEvent.VK_NUMPAD6:
        return Key.NUM_6;
      case KeyEvent.VK_NUMPAD7:
        return Key.NUM_7;
      case KeyEvent.VK_NUMPAD8:
        return Key.NUM_8;
      case KeyEvent.VK_NUMPAD9:
        return Key.NUM_9;
      default:
        return Key.UNKNOWN;
    }
  }

  private int getPointerId(MouseEvent e) {
    if (e instanceof MouseWheelEvent) {
      return 1;
    }
    if (SwingUtilities.isLeftMouseButton(e)) {
      return 0;
    }
    if (SwingUtilities.isMiddleMouseButton(e)) {
      return 1;
    }
    if (SwingUtilities.isRightMouseButton(e)) {
      return 2;
    }
    return 0;
  }

}

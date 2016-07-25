package featurea.input;

import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.app.Screen;

import java.util.ArrayList;
import java.util.List;

public final class Input {

  private final MediaPlayer mediaPlayer;
  public final List<InputListener> inputListeners = new ArrayList<>();
  public final Keyboard keyboard = new Keyboard();
  public final Display display = new Display();

  public Input(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public boolean update() {
    boolean isPin = false;
    for (InputListener inputListener : inputListeners) {
      if (isPin) {
        break;
      }
      isPin = update(inputListener);
    }
    return isPin;
  }

  public void update(Screen screen) {
    boolean isPin = update();
    for (InputListener inputListener : screen.inputListeners) {
      if (isPin) {
        break;
      }
      isPin = update(inputListener);
    }
    for (int i = screen.getLayers().size() - 1; i >= 0; i--) {
      if (isPin) {
        break;
      }
      Layer layer = screen.getLayers().get(i);
      for (InputListener inputListener : layer.inputListeners) {
        if (isPin) {
          break;
        }
        isPin = update(inputListener, layer);
      }
    }
  }

  public boolean update(InputListener inputListener, Layer layer) {
    keyboard.update(inputListener);
    return display.update(inputListener, layer);
  }

  public boolean update(InputListener inputListener) {
    return update(inputListener, null);
  }

  public void flush() {
    keyboard.flush();
    display.flush();
  }

  public void clear() {
    keyboard.clear();
    display.clear();
  }

  public void clear(Screen screen) {
    clear();
    if (screen != null) {
      for (Layer layer : screen.getLayers()) {
        for (InputListener inputListener : layer.inputListeners) {
          inputListener.clear();
        }
      }
    }
  }

  public void flushAndUpdate(Screen screen) {
    flush();
    if (screen != null) {
      update(screen);
    } else {
      update();
    }
  }

}

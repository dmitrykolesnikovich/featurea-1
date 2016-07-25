package featurea.input;

import featurea.util.BufferedMap;

public final class Keyboard {

  private final BufferedMap<Key, KeyboardEvent> events = new BufferedMap<>();

  /*package*/ Keyboard() {
    // no op
  }

  public void keyDown(Key key) {
    KeyboardEvent event = retrieveEvent(key);
    bufferEvent(event, key, KeyboardEventType.KEY_DOWN);
  }

  public void keyUp(Key key) {
    KeyboardEvent event = retrieveEvent(key);
    bufferEvent(event, key, KeyboardEventType.KEY_UP);
  }

  public void update(InputListener inputListener) {
    for (KeyboardEvent event : events.values()) {
      if (event.type != null) {
        switch (event.type) {
          case KEY_DOWN: {
            inputListener.onKeyDown(event.key);
            break;
          }
          case KEY_UP: {
            inputListener.onKeyUp(event.key);
            break;
          }
        }
      }
    }
  }

  public boolean isDown(Key key) {
    KeyboardEvent event = events.get(key);
    return event != null && event.type == KeyboardEventType.KEY_DOWN;
  }

  public boolean isUp(Key key) {
    KeyboardEvent event = events.get(key);
    return event != null && event.type == KeyboardEventType.KEY_UP;
  }

  public boolean isControlDown() {
    return isDown(Key.CONTROL_LEFT) || isDown(Key.CONTROL_RIGHT);
  }

  public boolean isShiftDown() {
    return isDown(Key.SHIFT_LEFT) || isDown(Key.SHIFT_RIGHT);
  }

  public boolean isAltDown() {
    return isDown(Key.ALT_LEFT) || isDown(Key.ALT_RIGHT);
  }

  public void flush() {
    events.flush();
  }

  public void clear() {
    for (KeyboardEvent event : events.values()) {
      // >> just for now
      // 1)
      if (event.type == KeyboardEventType.KEY_UP) {
        event.type = null;
      }

      // 2)
      /*event.type = null;*/
      // <<
    }
  }

  /*private API*/

  private KeyboardEvent retrieveEvent(Key key) {
    KeyboardEvent event = events.get(key);
    if (event == null) {
      event = new KeyboardEvent();
      events.put(key, event);
    }
    return event;
  }

  private void bufferEvent(KeyboardEvent event, Key key, KeyboardEventType type) {
    event.key = key;
    event.type = type;
    events.putBuffer(key, event);
  }

}

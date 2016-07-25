package featurea.input;

import featurea.app.Layer;
import featurea.util.BufferedMap;

import static featurea.input.DisplayEventType.*;

public final class Display {

  private final BufferedMap<Integer, DisplayEvent> events = new BufferedMap<>();

  /*package*/ Display() {
    // no op
  }

  public void down(double x, double y, int id) {
    DisplayEvent event = retrieveEvent(id);
    bufferEvent(x, y, id, DisplayEventType.POINTER_DOWN, event);
  }

  public void drag(double x, double y, int id) {
    DisplayEvent event = retrieveEvent(id);
    if (event.type != POINTER_DOWN) { // trash-hold
      bufferEvent(x, y, id, DisplayEventType.POINTER_DRAG, event);
    }
  }

  public void up(double x, double y, int id) {
    DisplayEvent event = retrieveEvent(id);
    bufferEvent(x, y, id, DisplayEventType.POINTER_UP, event);
  }

  public void move(int x, int y, int id) {
    DisplayEvent event = retrieveEvent(id);
    if (event.type != POINTER_UP) { // trash-hold
      bufferEvent(x, y, id, DisplayEventType.POINTER_MOVE, event);
    }
  }

  public void wheel(int count, double x, double y) {
    int id = 1;
    DisplayEvent event = retrieveEvent(id);
    event.count = count;
    bufferEvent(x, y, id, DisplayEventType.WHEEL, event);
  }

  public boolean update(InputListener inputListener, Layer layer) {
    for (DisplayEvent event : events.values()) {
      if (event.type != null) {
        boolean isPin = performUpdate(inputListener, event, layer);
        if (isPin) {
          return true;
        }
      }
    }
    return false;
  }

  public void flush() {
    events.flush();
  }

  public void clear() {
    for (DisplayEvent displayEvent : events.values()) {
      if (displayEvent.type == POINTER_DOWN) {
        displayEvent.type = POINTER_DRAG;
      } else if (displayEvent.type == POINTER_UP) {
        displayEvent.type = null;
      } else if (displayEvent.type == WHEEL) {
        displayEvent.type = null;
        displayEvent.count = 1;
      }
    }
  }

  /*private API*/

  private DisplayEvent retrieveEvent(int id) {
    DisplayEvent event = events.get(id);
    if (event == null) {
      event = new DisplayEvent();
      events.put(id, event);
    }
    return event;
  }

  private void bufferEvent(double x, double y, int id, DisplayEventType type, DisplayEvent event) {
    event.x = x;
    event.y = y;
    event.pointerId = id;
    event.type = type;
    events.putBuffer(id, event);
  }

  private boolean performUpdate(InputListener inputListener, DisplayEvent event, Layer layer) {
    double x = event.x;
    double y = event.y;
    int pointerId = event.pointerId;
    if (layer != null) {
      x = layer.toLayerX(x);
      y = layer.toLayerY(y);
    }
    switch (event.type) {
      case POINTER_DOWN: {
        return inputListener.onTouchDown(x, y, pointerId);
      }
      case POINTER_DRAG: {
        return inputListener.onTouchDrag(x, y, pointerId);
      }
      case POINTER_MOVE: {
        return inputListener.onTouchMove(x, y, pointerId);
      }
      case POINTER_UP: {
        return inputListener.onTouchUp(x, y, pointerId);
      }
      case WHEEL: {
        return inputListener.onMouseWheel(event.count, x, y);
      }
    }
    return false;
  }

}

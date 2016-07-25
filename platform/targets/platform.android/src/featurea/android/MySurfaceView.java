package featurea.android;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.FROYO)
public class MySurfaceView extends GLSurfaceView {

  public final List<OnTouchListener> touchListeners = new ArrayList<>();
  private final FeatureaActivity activity;

  public MySurfaceView(FeatureaActivity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public synchronized boolean onTouchEvent(MotionEvent event) {
    for (OnTouchListener touchListener : touchListeners) {
      touchListener.onTouch(this, event);
    }
    super.onTouchEvent(event);
    {
      int pointerIndex = event.getActionIndex();
      int pointerId = event.getPointerId(pointerIndex);
      double x = event.getX(pointerIndex);
      double y = event.getY(pointerIndex);
      switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN: {
          activity.getMediaPlayer().input.display.down(x, y, pointerId);
          break;
        }
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP: {
          activity.getMediaPlayer().input.display.up(x, y, pointerId);
          break;
        }
      }
    }
    {
      switch (event.getActionMasked()) {
        case MotionEvent.ACTION_MOVE: {
          for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++) {
            double x = event.getX(pointerIndex);
            double y = event.getY(pointerIndex);
            int pointerId = event.getPointerId(pointerIndex);
            activity.getMediaPlayer().input.display.drag(x, y, pointerId);
          }
          break;
        }
      }
    }
    return true;
  }
}

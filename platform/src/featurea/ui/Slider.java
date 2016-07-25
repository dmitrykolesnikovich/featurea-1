package featurea.ui;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.geometry.Shape;

import java.util.List;

public class Slider extends Layer {

  public List<Object> slides;
  private Slide slideOpen;
  private Slide slideClose;
  private int index;

  public void first() {
    slideOpen = /*(Slide) slides.get(index).newValue()*/null;
    show();
  }

  public void prev() {
    slideClose = slideOpen;
    slideClose.prev();
    slideClose.isClose = true;
    pause(slideClose);
    index--;
    if (index < 0) {
      index = slides.size() - 1;
    }
    slideOpen = /*(Slide) slides.get(index).newValue()*/null;
    slideOpen.next();
    shift(slideOpen);
    show();
  }

  public void next() {
    slideClose = slideOpen;
    slideClose.next();
    pause(slideClose);
    slideClose.isClose = true;
    index++;
    if (index >= slides.size()) {
      index = 0;
    }
    slideOpen = /*(Slide) slides.get(index).newValue()*/null;
    slideOpen.prev();
    shift(slideOpen);
    show();
  }

  private void shift(Slide slide) {
    for (Shape child : slide.getShapes()) {
    }
  }

  private void pause(Slide slide) {
    for (Shape child : slide.getShapes()) {
    }
  }

  public void show() {
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        getScreen().add(slideOpen);
        if (slideClose != null) {
          slideClose.start();
        }
        if (slideOpen != null) {
          slideOpen.start();
        }
      }
    });
  }
}

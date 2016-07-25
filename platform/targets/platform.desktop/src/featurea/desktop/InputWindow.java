package featurea.desktop;

import featurea.graphics.Window;
import org.lwjgl.InputListener;

public interface InputWindow extends Window {

  void addInputListener(InputListener inputListener);

  void removeInputListener(InputListener inputListener);

  void exit();

}

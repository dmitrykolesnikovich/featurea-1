package featurea.xmlEditor.views;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ErrTextView extends JTextArea {

  private final StringBuilder builder = new StringBuilder();

  public final PrintStream err = new PrintStream(new ByteArrayOutputStream()) {
    @Override
    public void print(String x) {
      super.print(x);
      builder.append(x + "\n");
    }
  };

  public boolean isError() {
    return builder.length() != 0;
  }

  public void flush() {
    setText(builder.toString());
  }

}

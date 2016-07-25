package featurea.util;

import java.util.List;

public class BufferedList<T> extends java.util.ArrayList<T> {

  private final List<T> buffer = new ArrayList<>();

  @Override
  public boolean add(T t) {
    return buffer.add(t);
  }

  public void flush() {
    addAll(buffer);
    buffer.clear();
  }

}

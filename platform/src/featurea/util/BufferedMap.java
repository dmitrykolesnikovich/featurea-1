package featurea.util;

import java.util.HashMap;
import java.util.Map;

public class BufferedMap<K, V> extends HashMap<K, V> {

  private final Map<K, V> buffer = new HashMap<>();

  public void flush() {
    putAll(buffer);
    buffer.clear();
  }

  public void putBuffer(K key, V value) {
    buffer.put(key, value);
  }

}

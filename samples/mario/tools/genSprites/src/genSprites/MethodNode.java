package genSprites;

import java.io.File;

public class MethodNode {

  public final String name;
  public final File file;

  public MethodNode(String name, File file) {
    this.name = Util.getPropertyName(name);
    this.file = file;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof MethodNode) {
      MethodNode methodNode = (MethodNode) object;
      return methodNode.name.equals(name) /*&& methodNode.file.getAbsolutePath().equals(file.getAbsolutePath())*/;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  public String debugString() {
    return name;
  }

  @Override
  public String toString() {
    return debugString();
  }

}

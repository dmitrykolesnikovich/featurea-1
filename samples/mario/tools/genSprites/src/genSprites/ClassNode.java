package genSprites;

import java.util.*;

public class ClassNode {

  public final String name;
  public final List<ClassNode> children = new ArrayList<>();
  public ClassNode parent;
  public final Map<MethodNode, Set<String>> methods = new HashMap<>();

  public ClassNode(String name) {
    this.name = name;
  }

  public ClassNode get(String name) {
    for (ClassNode child : children) {
      if (name.equals(child.name)) {
        return child;
      }
    }
    return null;
  }

  public String debugString() {
    return name;
  }

  @Override
  public String toString() {
    return debugString();
  }

  public String getFullName() {
    if (parent == null || parent.parent == null /*TODO avoid this shit*/) {
      return name;
    } else {
      return parent.getFullName() + "/" + name;
    }
  }

}

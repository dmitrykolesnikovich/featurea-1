package featurea.xml;

import com.sun.istack.internal.Nullable;
import featurea.util.Selection;
import featurea.util.Vector;

public interface XmlNode {

  void getSelection(Selection result, @Nullable Vector position);

}

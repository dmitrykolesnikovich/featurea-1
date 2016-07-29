package featurea.xml;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.app.Project;
import featurea.graphics.Graphics;
import featurea.util.Selection;

import java.io.File;

public interface XmlEditor {

  void selectXmlTag(XmlTag xmlTag);

  @NotNull
  XmlTag getSelectedXmlTag();

  @Nullable
  Selection getCurrentSelection();

  @Nullable
  Selection getSelection(XmlTag xmlTag);

  void setXmlTagToAppend(@Nullable String name);

  void replaceXmlTag(XmlTag parentXmlTag, int index, XmlTag childXmlTag);

  void appendXmlTag(@Nullable XmlTag parentXmlTag, XmlTag childXmlTag);

  void removeXmlTag(XmlTag xmlTag);

  void editAttribute(String key, String value);

  XmlPrimitive getSelectedPrimitive();

  MediaPlayer getMediaPlayer();

  Project getProject();

  File getXmlFile();

  XmlContext getXmlContext();

  @Nullable
  Layer getSelectedLayer();

  void save();

  void updatePointerCoordinates(double x, double y);

  void reload();

}

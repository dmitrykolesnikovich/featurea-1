package featurea.xml;

import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.graphics.DefaultGraphics;
import featurea.graphics.Graphics;
import featurea.input.InputAdapter;
import featurea.util.Selection;

public class XmlPrimitive extends InputAdapter {

  public final MediaPlayer mediaPlayer;
  public final XmlTag xmlTag;
  public final String key;

  public XmlPrimitive(MediaPlayer mediaPlayer, XmlTag xmlTag, String key) {
    this.mediaPlayer = mediaPlayer;
    this.xmlTag = xmlTag;
    this.key = key;
  }

  public void onDraw(Graphics graphics) {
    // no op
  }

  public void save() {
    XmlEditor writer = mediaPlayer.getResources().editor;
    if (writer != null) {
      writer.save();
    }
  }

  public int roundValue(double value) {
    return mediaPlayer.project.settings.roundValue(value);
  }

  public void onAttach() {
    // no op
  }

  public void onDetach() {
    // no op
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof XmlPrimitive) {
      XmlPrimitive xmlPrimitive = (XmlPrimitive) obj;
      return xmlPrimitive.key.equals(key) && xmlPrimitive.xmlTag == xmlTag;
    }
    return false;
  }

  public Layer getLayer() {
    return XmlUtil.getLayer(xmlTag, mediaPlayer.app.screen);
  }

  public Selection getSelection() {
    Selection selection = new Selection();
    XmlNode xmlNode = xmlTag.getResource();
    xmlNode.getSelection(selection, null);
    return selection;
  }

}

package featurea.xmlEditor.inputListeners;

import featurea.app.MediaPlayer;
import featurea.xml.XmlEditor;
import featurea.xml.XmlTag;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CopyPasteXmlTagListener extends KeyAdapter {

  private XmlTag xmlTag;

  private final MediaPlayer mediaPlayer;

  public CopyPasteXmlTagListener(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    XmlEditor editor = mediaPlayer.getResources().editor;
    if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
      XmlTag selectedXmlTag = editor.getSelectedXmlTag();
      if (selectedXmlTag != null) {
        xmlTag = selectedXmlTag;
      }
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      xmlTag = null;
    }
    if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
      if (xmlTag != null) {
        XmlTag selectedXmlTag = editor.getSelectedXmlTag();
        if (selectedXmlTag != null) {
          XmlTag parentXmlTag = selectedXmlTag.getParent();
          if (parentXmlTag == null || xmlTag != selectedXmlTag) {
            parentXmlTag = selectedXmlTag;
          }
          XmlTag xmlTagCopy = xmlTag.clone();
          editor.appendXmlTag(parentXmlTag, xmlTagCopy);
          editor.selectXmlTag(xmlTagCopy);
          xmlTag = null;
        }
      }
    }
  }

}

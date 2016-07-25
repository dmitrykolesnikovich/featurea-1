package featurea.ui;

import featurea.xml.XmlTag;

public class ClickListener {

  protected void click(Button button) {
    // no op
  }

  protected void down(Button button) {
    // no op
  }

  protected void up(Button button) {
    // no op
  }

  public final void move(Button button) {
    down(button);
  }

  public static String valueOf(XmlTag xmlTag, String var) {
    String clickString = "";
    String click = xmlTag.getAttribute("click");
    if (click != null) {
      clickString = click + "(button);";
    }
    String downString = "";
    String down = xmlTag.getAttribute("down");
    if (down != null) {
      downString = down + "(button);";
    }
    String upString = "";
    String up = xmlTag.getAttribute("up");
    if (up != null) {
      upString = up + "(button);";
    }
    if (!clickString.isEmpty() || !downString.isEmpty() || !upString.isEmpty()) {
      return var + ".clickListener = new featurea.ui.ClickListener() {" +
          "protected void click(featurea.ui.Button button) {" + clickString + "}" +
          "protected void down(featurea.ui.Button button) {" + downString + "}" +
          "protected void up(featurea.ui.Button button) {" + upString + "}" +
          "};";
    } else {
      return "";
    }
  }

}

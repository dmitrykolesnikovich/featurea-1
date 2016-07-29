package mario.objects.background;

import featurea.graphics.Graphics;
import featurea.platformer.util.Background;
import mario.objects.hero.World;

public class ThemeBackground extends Background {

  @Override
  protected String getFile(Graphics graphics, String sprite) {
    World world = (World) graphics.getLayer();
    // fixme featurea.util.TextureNotFoundException: background/night/bush.png
    return "background/" + world.getTheme().name() + "/" + sprite;
  }

}

package mario.util;

import featurea.app.Area;
import featurea.app.Projection;
import mario.objects.landscape.Block;

public class BonusBlocksProjection extends Projection<Block> {

  @Override
  public boolean onFilter(Area area) {
    if (area instanceof Block) {
      Block block = (Block) area;
      if (block.getBonus() != null) {
        return true;
      }
    }
    return false;
  }

}

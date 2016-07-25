package mario.objects.bonuses;

import featurea.app.Context;
import featurea.app.Layer;
import mario.objects.landscape.Liana;

public class LianaBonus extends Bonus {

  @Override
  public boolean work(Layer layer) {
    final Liana liana = new Liana();
    liana.setLength(1);
    liana.setPosition(position);
    liana.setTop(getBlock().top());
    liana.build();
    layer.add(liana);
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        liana.growUp();
      }
    }, 2000);
    return true;
  }

}

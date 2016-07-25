package featurea.android;

import android.app.Application;
import featurea.app.Context;

public class FeatureaApplication extends Application {

  public static FeatureaApplication instance;

  public FeatureaApplication() {
    instance = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Context.gl = new OpenGLImpl();
    Context.al = new OpenALImpl();
  }

}

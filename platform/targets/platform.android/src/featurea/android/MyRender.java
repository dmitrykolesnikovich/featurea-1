package featurea.android;

import android.annotation.TargetApi;
import android.opengl.GLSurfaceView;
import android.os.Build;
import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.app.Project;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.util.jar.JarFile;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class MyRender implements GLSurfaceView.Renderer {

  private long now;
  private final MediaPlayer mediaPlayer;
  private boolean isCreated;
  public final FeatureaActivity activity;

  public MyRender(FeatureaActivity activity) {
    this.activity = activity;
    mediaPlayer = new MediaPlayer();
    mediaPlayer.project.setProduction(true);
    JarFile apkFile = ApkFileUtil.getInstance().getFile();
    if (apkFile != null) {
      mediaPlayer.getFiles().add(apkFile);
      mediaPlayer.getClassLoader().add(apkFile);
    } else {
      throw new IllegalStateException("apkFile == null");
    }
    mediaPlayer.project.setFile(new File(Project.PROJECT_FILE_NAME));
  }

  @Override
  public void onSurfaceCreated(GL10 skip, EGLConfig config) {
    if (!isCreated) {
      isCreated = true;
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer); // IMPORTANT
      mediaPlayer.app.onCreate();
      activity.onCreateMediaPlayer(mediaPlayer);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 skip, int width, int height) {
    mediaPlayer.app.onResize(width, height);
  }

  @Override
  public void onDrawFrame(GL10 skip) {
    long nanoTime = System.nanoTime();
    if (now == 0) {
      now = nanoTime;
    }
    double elapsedTime = (nanoTime - now) / 1_000_000.0d;
    activity.onTick(elapsedTime);
    mediaPlayer.app.onDrawBackground();
    mediaPlayer.app.onDraw();
    now = nanoTime;
  }

}

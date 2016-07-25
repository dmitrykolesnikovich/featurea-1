package featurea.android;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.jar.JarFile;

public final class ApkFileUtil {

  private final static ApkFileUtil instance = new ApkFileUtil();

  private ApkFileUtil() {
    // no op
  }

  public static ApkFileUtil getInstance() {
    return instance;
  }

  public JarFile getFile() {
    Application app = FeatureaApplication.instance;
    String packageName = app.getPackageName();
    PackageManager packageManager = app.getPackageManager();
    try {
      PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
      String sourceDir = packageInfo.applicationInfo.sourceDir;
      JarFile apkFile = new JarFile(sourceDir);
      return apkFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getRoot() {
    Application app = FeatureaApplication.instance;
    String packageName = app.getPackageName();
    File dir;
    if (isExternalStorageAvailable()) {
      File sdDir = android.os.Environment.getExternalStorageDirectory();
      dir = new File(sdDir, "/Android/data/" + packageName);
    } else {
      dir = new File("/data/data/" + packageName);
    }
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir.getAbsolutePath() + "/";
  }

  public String getCacheDir() {
    return getRoot() + "/cache";
  }

  private boolean isExternalStorageAvailable() {
    boolean mExternalStorageAvailable;
    boolean mExternalStorageWriteable;
    String state = android.os.Environment.getExternalStorageState();
    if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
      mExternalStorageAvailable = mExternalStorageWriteable = true;
    } else if (android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      mExternalStorageAvailable = true;
      mExternalStorageWriteable = false;
    } else {
      mExternalStorageAvailable = mExternalStorageWriteable = false;
    }
    return mExternalStorageAvailable && mExternalStorageWriteable;
  }

}


package com.androidinstalledapps;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import javax.annotation.Nullable;

import com.helper.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RNAndroidInstalledAppsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNAndroidInstalledAppsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNAndroidInstalledApps";
  }

  @ReactMethod
  public void getApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      JSONArray apps = new JSONArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        addApp(packageInfo, apps, pm);
      }
      promise.resolve(apps.toString());
    } catch (Exception ex) {
      promise.reject(ex);
    }
  }

  @ReactMethod
  public void getNonSystemApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      JSONArray apps = new JSONArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        WritableMap appInfo = Arguments.createMap();

        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
          addApp(packageInfo, apps, pm);
        }
      }
      promise.resolve(apps.toString());
    } catch (Exception ex) {
      promise.reject(ex);
    }

  }

  @ReactMethod
  public void getSystemApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      JSONArray apps = new JSONArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        WritableMap appInfo = Arguments.createMap();

        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
          addApp(packageInfo, apps, pm);
        }
      }
      promise.resolve(apps.toString());
    } catch (Exception ex) {
      promise.reject(ex);
    }

  }

  private void addApp(PackageInfo packageInfo, JSONArray apps, PackageManager pm) throws JSONException {
    JSONObject app = new JSONObject();

    app.put("packageName", packageInfo.packageName);
    app.put("versionName", packageInfo.versionName);
    app.put("versionCode", packageInfo.versionCode);
    app.put("firstInstallTime", packageInfo.firstInstallTime);
    app.put("lastUpdateTime", packageInfo.lastUpdateTime);
    app.put("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)));

    String apkDir = packageInfo.applicationInfo.publicSourceDir;
    app.put("apkDir", packageInfo.applicationInfo.publicSourceDir);

    apps.put(app);
  }
}
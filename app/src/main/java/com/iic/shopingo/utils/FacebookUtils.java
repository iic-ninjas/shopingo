package com.iic.shopingo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import java.security.MessageDigest;

/**
 * Created by ifeins on 3/17/15.
 */
public class FacebookUtils {

  public static String getKeyHash(Context context) {
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo("com.iic.shopingo", PackageManager.GET_SIGNATURES);
      Signature signature = info.signatures[0];
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(signature.toByteArray());
      return Base64.encodeToString(md.digest(), Base64.DEFAULT);
    } catch (Exception e) {
      return null;
    }
  }
}

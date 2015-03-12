package com.iic.shopingo.ui.utils;

import android.net.Uri;

/**
 * Created by ifeins on 3/9/15.
 */
public class AvatarUriGenerator {

  public static Uri generateAvatarUri(String keyword) {
    return Uri.parse("http://robohash.org").buildUpon().appendPath(keyword).appendQueryParameter("bgset", "bg1").build();
  }
}

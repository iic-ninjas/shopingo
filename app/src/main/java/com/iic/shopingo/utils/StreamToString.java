package com.iic.shopingo.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by asafg on 12/03/15.
 */
public class StreamToString {
  public static String toString(InputStream stream) {
    BufferedReader r = new BufferedReader(new InputStreamReader(stream));
    StringBuilder total = new StringBuilder();
    String line;
    try {
      while ((line = r.readLine()) != null) {
        total.append(line);
      }
    } catch (Exception ex) {
      return null;
    }
    return total.toString();
  }
}

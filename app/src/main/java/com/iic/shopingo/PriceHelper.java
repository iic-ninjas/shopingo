package com.iic.shopingo;

import android.content.Context;
import android.text.style.TtsSpan;
import java.util.Currency;

/**
 * Created by asafg on 03/03/15.
 */
public class PriceHelper {
  public static String getPriceString(int cents, String symbol) {
    return symbol + Float.toString((float)cents/100.0f);
  }

  public static String getPriceString(int cents, Currency currency) {
    return getPriceString(cents, currency.getSymbol());
  }

  public static String getUSDPriceString(int cents) {
    return getPriceString(cents, Currency.getInstance("USD"));
  }

}

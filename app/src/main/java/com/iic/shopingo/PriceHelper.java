package com.iic.shopingo;

import java.util.Currency;

/**
 * Created by asafg on 03/03/15.
 */
public class PriceHelper {
  public static String getPriceString(int dollars, String symbol) {
    return symbol + Integer.toString(dollars);
  }

  public static String getPriceString(int dollars, Currency currency) {
    return getPriceString(dollars, currency.getSymbol());
  }

  public static String getUSDPriceString(int cents) {
    return getPriceString(cents, Currency.getInstance("USD"));
  }

}

package com.iic.shopingo.ui.request_flow;

import android.app.ListActivity;
import android.os.Bundle;
import com.iic.shopingo.R;

public class SelectShopperActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_shopper);
  }
}
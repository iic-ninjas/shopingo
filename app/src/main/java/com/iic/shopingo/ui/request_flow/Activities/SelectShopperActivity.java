package com.iic.shopingo.ui.request_flow.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.request_flow.SelectShopperAdapter;
import java.util.ArrayList;

public class SelectShopperActivity extends ActionBarActivity {
  @InjectView(R.id.select_shopper_list)
  ListView mShopperList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_shopper);
    ButterKnife.inject(this);

    SelectShopperAdapter adapter = new SelectShopperAdapter(this, new ArrayList<SelectShopperAdapter.User>());
    mShopperList.setAdapter(adapter);
  }
}
package com.iic.shopingo.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.data.Request;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestDetails extends Activity {
  public static final String EXTRA_REQUEST = "request";

  public static final int RESULT_NONE = 0;
  public static final int RESULT_ACCEPT = 1;
  public static final int RESULT_REJECT = 2;

  private Request request;

  @InjectView(R.id.requester_name) TextView name;
  @InjectView(R.id.num_items) TextView numItems;
  @InjectView(R.id.items_list) ListView itemsList;
  @InjectView(R.id.offer) TextView offer;
  @InjectView(R.id.address) TextView address;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (!intent.hasExtra(EXTRA_REQUEST)) {
      throw new IllegalArgumentException("Must pass EXTRA_REQUEST to RequestDetails");
    } else {
      request = intent.getParcelableExtra(EXTRA_REQUEST);

      setContentView(R.layout.request_details);
      ButterKnife.inject(this);

      name.setText(request.name);
      numItems.setText(request.items.size() + " Items");
      offer.setText(PriceHelper.getUSDPriceString(request.offerInCents));
      address.setText(request.location.toAddressString());

      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
      adapter.addAll(request.items);
      itemsList.setAdapter(adapter);
    }
  }

  @OnClick(R.id.call_button)
  public void onCall(View view) {
    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(Uri.parse("tel:" + request.location.phone));
    startActivity(intent);
  }

  @OnClick(R.id.accept_button)
  public void onAccept(View view) {
    setResult(RESULT_ACCEPT);
    finish();
  }

  @OnClick(R.id.reject_button)
  public void onReject(View view) {
    setResult(RESULT_REJECT);
    finish();
  }
}

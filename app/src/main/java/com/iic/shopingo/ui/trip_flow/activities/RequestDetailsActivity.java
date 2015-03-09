package com.iic.shopingo.ui.trip_flow.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.PriceHelper;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.trip_flow.data.Request;
import com.squareup.picasso.Picasso;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestDetailsActivity extends ActionBarActivity {
  public static final String EXTRA_REQUEST = "request";

  public static final int RESULT_NONE = 0;
  public static final int RESULT_ACCEPT = 1;
  public static final int RESULT_DECLINE = 2;

  private Request request;

  @InjectView(R.id.request_details_requester_name) TextView name;
  @InjectView(R.id.request_details_items_list) ListView itemsList;
  @InjectView(R.id.request_details_offer) TextView offer;
  @InjectView(R.id.request_details_address) TextView address;
  @InjectView(R.id.request_details_avatar_icon) ImageView avatarImageView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (!intent.hasExtra(EXTRA_REQUEST)) {
      throw new IllegalArgumentException("Must pass EXTRA_REQUEST to RequestDetails");
    } else {
      request = intent.getParcelableExtra(EXTRA_REQUEST);

      setContentView(R.layout.activity_request_details);
      ButterKnife.inject(this);

      Uri avatarUri = Uri.parse("http://robohash.org").buildUpon().appendPath(request.name).build();
      Picasso.with(this).load(avatarUri).into(avatarImageView);
      name.setText(request.name);
      offer.setText(getString(R.string.format_offered_price, PriceHelper.getUSDPriceString(request.offerInCents)));
      address.setText(getString(R.string.format_delivery_address, request.location.toAddressString()));

      ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
      adapter.addAll(request.items);
      itemsList.setAdapter(adapter);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_request_details, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_call_requester:
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + request.location.phone));
        startActivity(intent);
        break;
      case R.id.action_accept_request:
        onAccept(null);
        break;
      case R.id.action_decline_request:
        onReject(null);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.request_details_accept_button)
  public void onAccept(View view) {
    setResult(RESULT_ACCEPT);
    finish();
  }

  @OnClick(R.id.request_details_decline_button)
  public void onReject(View view) {
    setResult(RESULT_DECLINE);
    finish();
  }
}

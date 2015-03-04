package com.iic.shopingo.ui.request_flow.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.request_flow.views.CreateRequestItemListView;
import com.iic.shopingo.ui.request_flow.views.CreateRequestListItemView;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CreateRequestActivity extends ActionBarActivity implements
    CreateRequestListItemView.OnListViewChanged {
  public static final String SHOPPER_EXTRA_KEY = "shopper";

  @InjectView(R.id.create_request_items_list)
  CreateRequestItemListView itemListView;

  @InjectView(R.id.create_request_currency_symbol)
  TextView currencyView;

  @InjectView(R.id.create_request_price_input)
  EditText priceView;

  private SelectShopperActivity.SelectShopperAdapter.Shopper shopper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shopper = getIntent().getParcelableExtra(SHOPPER_EXTRA_KEY);

    setContentView(R.layout.activity_create_request);
    ButterKnife.inject(this);

    itemListView.addItem("");
    currencyView.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
  }

  @Override
  public void onRemoveButtonClicked(View view) {
    itemListView.removeItem(itemListView.getPositionForView(view));
  }

  @Override
  public void onItemEdited(View view, String value) {
    int position = itemListView.getPositionForView(view);
    if (value.equals("")) {
      if (position != itemListView.size() - 1) {
        itemListView.removeItem(position);
      }
    } else {
      itemListView.setItem(position, value);
      if (position == itemListView.size() - 1) {
        itemListView.addItem("");
        ((CreateRequestListItemView)itemListView.getChildAt(position + 1)).focus();
      }
    }
  }

  @OnClick(R.id.create_request_create_button)
  public void onCreateRequest(View view) {
    List<String> items = itemListView.getItems();
    int price = Integer.parseInt(priceView.getText().toString());
    Request request = new Request(shopper, items, price);
    // TODO: Create request on server and move to state activity
  }

  public static class Request implements Parcelable {
    public SelectShopperActivity.SelectShopperAdapter.Shopper shopper;
    public List<String> items;
    public int price;

    public Request(SelectShopperActivity.SelectShopperAdapter.Shopper shopper, List<String> items, int price) {
      this.shopper = shopper;
      this.items = items;
      this.price = price;
    }

    public Request(Parcel source) {
      shopper = source.readParcelable(SelectShopperActivity.SelectShopperAdapter.Shopper.class.getClassLoader());
      items = source.createStringArrayList();
      price = source.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(shopper, 0);
      dest.writeStringList(items);
      dest.writeInt(price);
    }

    public final static Creator<Request> CREATOR = new Creator<Request>() {
      @Override
      public Request createFromParcel(Parcel source) {
        return new Request(source);
      }

      @Override
      public Request[] newArray(int size) {
        return new Request[size];
      }
    };
  }
}

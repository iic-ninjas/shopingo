package com.iic.shopingo.ui.trip_flow.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.ui.trip_flow.data.Request;
import com.iic.shopingo.ui.trip_flow.data.ShoppingList;
import com.iic.shopingo.ui.trip_flow.fragments.RequestListFragment;
import com.iic.shopingo.ui.trip_flow.fragments.UnifiedShoppingListFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class ManageTripActivity extends ActionBarActivity implements RequestListFragment.RequestListListener {

  @InjectView(R.id.manage_trip_pager)
  ViewPager pager;

  RequestListFragment requestListFragment;
  UnifiedShoppingListFragment unifiedShoppingListFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_trip);
    ButterKnife.inject(this);

    List<Request> requests = new ArrayList<>(10);

    for (int i = 0; i < 10; ++i) {
      Request req = new Request();
      req.photoUrl = "";
      req.name = "Moshe " + i;
      req.offerInCents = 100*i;
      req.location.coords.setLatitude(32.063146);
      req.location.coords.setLongitude(34.770706);
      req.location.city = "Tel Aviv";
      req.location.country = "Israel";
      req.location.address = "13 Rothschild Ave.";
      req.location.zipcode = "12345";
      req.location.phone = "12345";
      req.items.add("1 Milk");
      req.items.add("1 Bread");
      req.items.add("1 Cheese");
      req.items.add("12 Eggs");
      requests.add(req);
    }

    requestListFragment = new RequestListFragment();
    requestListFragment.setRequestListListener(this);
    requestListFragment.setRequests(requests);

    unifiedShoppingListFragment = new UnifiedShoppingListFragment();

    pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        switch (position) {
          case 0:
            return requestListFragment;
          case 1:
            return unifiedShoppingListFragment;
          default:
            throw new IllegalStateException("Can't have more than 2 fragments in trip manager");
        }
      }

      @Override
      public CharSequence getPageTitle(int position) {
        switch (position) {
          case 0:
            return "Available Requests";
          case 1:
            return "Shopping List";
          default:
            throw new IllegalStateException("Can't have more than 2 fragments in trip manager");
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    });
  }

  @Override
  public void onRequestAccepted(Request request) {
    ShoppingList sl = new ShoppingList();
    sl.requesterName = request.name;
    sl.phoneNumber = request.location.phone;
    for (String itemTitle : request.items) {
      sl.items.add(new ShoppingList.Item(itemTitle));
    }

    unifiedShoppingListFragment.addShoppingList(sl);
  }

  @Override
  public void onRequestDeclined(Request request) {
    // TODO: nothing?
  }

  @Override
  public void onRequestSelected(Request request) {
    pager.setCurrentItem(1, true);
    // TODO: scroll to correct shopping list
  }
}

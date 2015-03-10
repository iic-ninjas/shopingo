package com.iic.shopingo.ui.trip_flow.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.Contact;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.ui.trip_flow.data.ShoppingList;
import com.iic.shopingo.ui.trip_flow.fragments.RequestListFragment;
import com.iic.shopingo.ui.trip_flow.fragments.UnifiedShoppingListFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class ManageTripActivity extends FragmentActivity implements RequestListFragment.RequestListListener {

  @InjectView(R.id.manage_trip_pager)
  ViewPager pager;

  RequestListFragment requestListFragment;
  UnifiedShoppingListFragment unifiedShoppingListFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.manage_trip);
    ButterKnife.inject(this);

    List<IncomingRequest> requests = new ArrayList<>(10);

    for (int i = 0; i < 10; ++i) {
      List<String> items = new ArrayList<>(4);
      items.add("1 Milk");
      items.add("1 Bread");
      items.add("1 Cheese");
      items.add("12 Eggs");
      IncomingRequest req = new IncomingRequest(
          "123",
          new Contact(
              "12345",
              "Moshe",
              Integer.toString(i),
              "12345",
              "13 Rothschild Ave.",
              "Tel Aviv",
              32.063146,
              34.770706
          ),
          new com.iic.shopingo.dal.models.ShoppingList(items, 500),
          BaseRequest.RequestStatus.PENDING
      );
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
  public void onRequestAccepted(IncomingRequest request) {
    ShoppingList sl = new ShoppingList();
    sl.requesterName = request.getRequester().getFirstName();
    sl.phoneNumber = request.getRequester().getPhoneNumber();
    for (String itemTitle : request.getShoppingList().getItems()) {
      sl.items.add(new ShoppingList.Item(itemTitle));
    }

    unifiedShoppingListFragment.addShoppingList(sl);
  }

  @Override
  public void onRequestDeclined(IncomingRequest request) {
    // TODO: nothing?
  }

  @Override
  public void onRequestSelected(IncomingRequest request) {
    pager.setCurrentItem(1, true);
    // TODO: scroll to correct shopping list
  }
}

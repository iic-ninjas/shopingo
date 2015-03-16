package com.iic.shopingo.ui.trip_flow.activities;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.trip.EndTripCommand;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.async.ApiTask;
import com.iic.shopingo.ui.trip_flow.data.ShoppingList;
import com.iic.shopingo.ui.trip_flow.fragments.DiscardTripDialogFragment;
import com.iic.shopingo.ui.trip_flow.fragments.RequestListFragment;
import com.iic.shopingo.ui.trip_flow.fragments.UnifiedShoppingListFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class ManageTripActivity extends ActionBarActivity
    implements RequestListFragment.RequestListListener, DiscardTripDialogFragment.OnResultListener {

  private static final String DISCARD_TRIP_DIALOG_TAG = "DISCARD_TRIP_DIALOG";

  private boolean backPressed;

  private boolean upPressed;

  public static final String EXTRA_REQUESTS = "requests";

  @InjectView(R.id.manage_trip_pager)
  ViewPager pager;

  RequestListFragment requestListFragment;

  UnifiedShoppingListFragment unifiedShoppingListFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_trip);
    ButterKnife.inject(this);

    List<IncomingRequest> requests = new ArrayList<>();
    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_REQUESTS)) {
      requests = intent.getParcelableArrayListExtra(EXTRA_REQUESTS);
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
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      upPressed = true;
      promptDiscardTrip();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    backPressed = true;
    promptDiscardTrip();
  }

  private void promptDiscardTrip() {
    DiscardTripDialogFragment dialog = new DiscardTripDialogFragment();
    dialog.setOnResultListener(this);
    dialog.show(getSupportFragmentManager(), DISCARD_TRIP_DIALOG_TAG);
  }

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

  @Override
  public void onDiscardTripDialogOK() {
    ApiTask<ApiResult> task = new ApiTask<>(getSupportFragmentManager(), "Ending trip...", new EndTripCommand(CurrentUser.getToken()));
    task.execute().continueWith(new Continuation<ApiResult, Object>() {
      @Override
      public Object then(Task<ApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          if (backPressed) {
            ManageTripActivity.super.onBackPressed();
          } else {
            NavUtils.navigateUpFromSameTask(ManageTripActivity.this);
          }

          backPressed = false;
          upPressed = false;
        } else {
          Toast.makeText(ManageTripActivity.this, "Could not end trip: " + task.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }

  @Override
  public void onDiscardTripDialogCancel() {
    backPressed = false;
    upPressed = false;
  }
}

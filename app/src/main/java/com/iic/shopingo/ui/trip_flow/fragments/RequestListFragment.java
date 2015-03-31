package com.iic.shopingo.ui.trip_flow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.iic.shopingo.R;
import com.iic.shopingo.api.trip.GetPendingRequestsCommand;
import com.iic.shopingo.api.trip.PendingRequestsApiResult;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.events.AppEventBus;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.services.notifications.IncomingRequestNotification;
import com.iic.shopingo.ui.trip_flow.views.RequestListAdapter;
import com.iic.shopingo.ui.trip_flow.views.RequestListItem;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class RequestListFragment extends Fragment implements RequestListItem.RequestListener {

  public interface RequestListListener {
    public void onRequestAccepted(IncomingRequest request);
    public void onRequestDeclined(IncomingRequest request);
  }

  private static final String TAG = RequestListFragment.class.getSimpleName();

  private RequestListAdapter adapter;

  private RequestListListener listener;

  private List<IncomingRequest> requests;

  @InjectView(R.id.request_list_list_view)
  ListView listView;

  @InjectView(R.id.request_list_empty_stub)
  ViewStub emptyStateStub;

  @Override
  public void onRequestAccepted(IncomingRequest request) {
    if (listener != null) {
      listener.onRequestAccepted(request);
    }
  }

  @Override
  public void onRequestDeclined(IncomingRequest request) {
    if (listener != null) {
      listener.onRequestDeclined(request);
    }
  }

  public void setRequestListListener(RequestListListener listener) {
    this.listener = listener;
  }

  public void setRequests(List<IncomingRequest> requests) {
    this.requests = filterOnlyPending(requests);
    if (adapter != null) {
      adapter.setRequests(this.requests);
    }
  }

  private List<IncomingRequest> filterOnlyPending(List<IncomingRequest> requests) {
    List<IncomingRequest> pendingRequests = new ArrayList<>();
    for (IncomingRequest req : requests) {
      if (req.getStatus() == BaseRequest.RequestStatus.PENDING) {
        pendingRequests.add(req);
      }
    }
    return pendingRequests;
  }

  public void removeRequest(IncomingRequest request) {
    this.requests.remove(request);
    adapter.removeRequest(request);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.request_list, container, false);
    ButterKnife.inject(this, view);

    listView.setEmptyView(emptyStateStub);

    adapter = new RequestListAdapter(getActivity(), this);
    listView.setAdapter(adapter);
    if (requests != null) {
      adapter.setRequests(requests);
    }
    return view;
  }

  @OnItemClick(R.id.request_list_list_view)
  public void onItemClick(int position) {
    ((RequestListItem) listView.getChildAt(position)).toggleExpanded();
  }

  @Override
  public void onStart() {
    super.onStart();
    AppEventBus.getInstance().register(this);
  }

  @Override
  public void onStop() {
    AppEventBus.getInstance().unregister(this);
    super.onStop();
  }

  @Override
  public void onResume() {
    super.onResume();
    updateRequests();
  }

  @Subscribe
  public void onIncomingRequest(IncomingRequestNotification notification) {
    updateRequests();
  }

  private void updateRequests() {
    GetPendingRequestsCommand req = new GetPendingRequestsCommand(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<PendingRequestsApiResult, Object>() {
      @Override
      public Object then(Task<PendingRequestsApiResult> task) throws Exception {
        if (!task.isFaulted() && !task.isCancelled()) {
          RequestListFragment.this.requests = filterOnlyPending(task.getResult().requests);
          adapter.setRequests(RequestListFragment.this.requests);
        } else {
          Log.e(TAG, "Error receiving pending requests", task.getError());
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }
}

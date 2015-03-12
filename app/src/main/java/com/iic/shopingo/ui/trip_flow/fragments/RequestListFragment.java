package com.iic.shopingo.ui.trip_flow.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;
import com.iic.shopingo.api.request.GetNearbyShoppers;
import com.iic.shopingo.api.request.ShoppersApiResult;
import com.iic.shopingo.api.trip.GetPendingRequests;
import com.iic.shopingo.api.trip.PendingRequestsApiResult;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.trip_flow.activities.RequestDetails;
import com.iic.shopingo.ui.trip_flow.views.RequestListAdapter;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class RequestListFragment extends Fragment implements AdapterView.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  private RequestListAdapter adapter;
  private RequestListListener listener;
  private List<IncomingRequest> requests;

  @InjectView(R.id.request_list_list_view)
  ListView listView;

  @InjectView(R.id.request_list_swipe_container)
  SwipeRefreshLayout swipeLayout;

  public interface RequestListListener {
    public void onRequestAccepted(IncomingRequest request);
    public void onRequestDeclined(IncomingRequest request);
    public void onRequestSelected(IncomingRequest request);
  }

  public void setRequestListListener(RequestListListener listener) {
    this.listener = listener;
  }

  public void setRequests(List<IncomingRequest> requests) {
    this.requests = requests;
    if (adapter != null) {
      adapter.setRequests(this.requests);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.request_list, container, false);
    ButterKnife.inject(this, view);
    //listView = (ListView)view.findViewById(R.id.request_list_list_view);
    //swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.request_list_swipe_container);
    listView.setOnItemClickListener(this);
    adapter = new RequestListAdapter(getActivity());
    listView.setAdapter(adapter);
    if (requests != null) {
      adapter.setRequests(requests);
    }
    swipeLayout.setOnRefreshListener(this);
    return view;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    IncomingRequest req = adapter.getItem(position);

    if (req.getStatus() == BaseRequest.RequestStatus.PENDING) {
      Intent intent = new Intent(getActivity(), RequestDetails.class);
      intent.putExtra(RequestDetails.EXTRA_REQUEST, req);
      startActivityForResult(intent, position);
    } else if (req.getStatus() == BaseRequest.RequestStatus.ACCEPTED) {
      if (listener != null) {
        listener.onRequestSelected(req);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode >= 0) {
      IncomingRequest req = adapter.getItem(requestCode);
      if (resultCode == RequestDetails.RESULT_ACCEPT) {
        req.setStatus(BaseRequest.RequestStatus.ACCEPTED);
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.onRequestAccepted(req);
        }
      }
      if (resultCode == RequestDetails.RESULT_DECLINE) {
        req.setStatus(BaseRequest.RequestStatus.DECLINED);
        adapter.notifyDataSetChanged();
        if (listener != null) {
          listener.onRequestDeclined(req);
        }
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onRefresh() {
    updateRequests();
  }

  private void updateRequests() {
    swipeLayout.setRefreshing(true);
    GetPendingRequests req = new GetPendingRequests(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<PendingRequestsApiResult, Object>() {
      @Override
      public Object then(Task<PendingRequestsApiResult> task) throws Exception {
        swipeLayout.setRefreshing(false);
        if (!task.isFaulted() && !task.isCancelled()) {
          adapter.setRequests(task.getResult().requests);
        } else {
          // TODO: handle failure
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }
}

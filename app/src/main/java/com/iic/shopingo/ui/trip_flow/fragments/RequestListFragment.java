package com.iic.shopingo.ui.trip_flow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.iic.shopingo.api.trip.GetPendingRequestsCommand;
import com.iic.shopingo.api.trip.PendingRequestsApiResult;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.services.CurrentUser;
import com.iic.shopingo.ui.trip_flow.activities.RequestDetailsActivity;
import com.iic.shopingo.ui.trip_flow.views.RequestListAdapter;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class RequestListFragment extends Fragment implements AdapterView.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  private static final String TAG = RequestListFragment.class.getSimpleName();

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
      Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
      intent.putExtra(RequestDetailsActivity.EXTRA_REQUEST, req);
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
      if (resultCode == RequestDetailsActivity.RESULT_ACCEPT) {
        req.setStatus(BaseRequest.RequestStatus.ACCEPTED);
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.onRequestAccepted(req);
        }
      } else if (resultCode == RequestDetailsActivity.RESULT_DECLINE) {
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
    GetPendingRequestsCommand req = new GetPendingRequestsCommand(CurrentUser.getToken());
    req.executeAsync().continueWith(new Continuation<PendingRequestsApiResult, Object>() {
      @Override
      public Object then(Task<PendingRequestsApiResult> task) throws Exception {
        swipeLayout.setRefreshing(false);
        if (!task.isFaulted() && !task.isCancelled()) {
          adapter.setRequests(task.getResult().requests);
        } else {
          Log.e(TAG, "Error receiving pending requests", task.getError());
        }
        return null;
      }
    }, Task.UI_THREAD_EXECUTOR);
  }
}

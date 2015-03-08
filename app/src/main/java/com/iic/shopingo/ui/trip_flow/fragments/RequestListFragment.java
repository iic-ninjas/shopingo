package com.iic.shopingo.ui.trip_flow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.iic.shopingo.dal.models.BaseRequest;
import com.iic.shopingo.dal.models.IncomingRequest;
import com.iic.shopingo.ui.trip_flow.activities.RequestDetails;
import com.iic.shopingo.ui.trip_flow.views.RequestListAdapter;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class RequestListFragment extends Fragment implements AdapterView.OnItemClickListener {

  private ListView listView;
  private RequestListAdapter adapter;
  private RequestListListener listener;
  private List<IncomingRequest> requests;

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
    if (listView != null) {
      adapter = new RequestListAdapter(getActivity(), this.requests);
      listView.setAdapter(adapter);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    listView = new ListView(getActivity());
    listView.setOnItemClickListener(this);
    if (requests != null) {
      adapter = new RequestListAdapter(getActivity(), this.requests);
      listView.setAdapter(adapter);
    }
    return listView;
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
}

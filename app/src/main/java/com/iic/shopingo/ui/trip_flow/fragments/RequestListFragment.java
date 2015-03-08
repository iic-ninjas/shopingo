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
import com.iic.shopingo.ui.trip_flow.activities.RequestDetailsActivity;
import com.iic.shopingo.ui.trip_flow.data.Request;
import com.iic.shopingo.ui.trip_flow.views.RequestListAdapter;
import java.util.List;

/**
 * Created by asafg on 05/03/15.
 */
public class RequestListFragment extends Fragment implements AdapterView.OnItemClickListener {

  private ListView listView;
  private RequestListAdapter adapter;
  private RequestListListener listener;
  private List<Request> requests;

  public interface RequestListListener {
    public void onRequestAccepted(Request request);
    public void onRequestDeclined(Request request);
    public void onRequestSelected(Request request);
  }

  public void setRequestListListener(RequestListListener listener) {
    this.listener = listener;
  }

  public void setRequests(List<Request> requests) {
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
    Request req = adapter.getItem(position);

    if (req.status == Request.STATUS_PENDING) {
      Intent intent = new Intent(getActivity(), RequestDetailsActivity.class);
      intent.putExtra(RequestDetailsActivity.EXTRA_REQUEST, req);
      startActivityForResult(intent, position);
    } else if (req.status == Request.STATUS_ACCEPTED) {
      if (listener != null) {
        listener.onRequestSelected(req);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode >= 0) {
      Request req = adapter.getItem(requestCode);
      if (resultCode == RequestDetailsActivity.RESULT_ACCEPT) {
        req.status = Request.STATUS_ACCEPTED;
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.onRequestAccepted(req);
        }
      }
      if (resultCode == RequestDetailsActivity.RESULT_DECLINE) {
        req.status = Request.STATUS_DECLINED;
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

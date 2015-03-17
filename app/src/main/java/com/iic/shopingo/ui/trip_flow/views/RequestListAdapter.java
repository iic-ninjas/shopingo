package com.iic.shopingo.ui.trip_flow.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.iic.shopingo.dal.models.IncomingRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 04/03/15.
 */
public class RequestListAdapter extends BaseAdapter {
  private List<IncomingRequest> requests = new ArrayList<>(0);
  private Context context;
  private RequestListItem.RequestListener listener;

  public RequestListAdapter(Context context, RequestListItem.RequestListener listener) {
    this.context = context;
    this.listener = listener;
  }

  @Override
  public int getCount() {
    return requests.size();
  }

  @Override
  public IncomingRequest getItem(int position) {
    return requests.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    RequestListItem item = null;
    if (convertView != null) {
      item = (RequestListItem)convertView;
    } else {
      item = new RequestListItem(context);
    }
    IncomingRequest req = getItem(position);
    item.setListener(listener);
    item.setRequest(req);
    return item;
  }

  public void setRequests(List<IncomingRequest> requests) {
    this.requests = requests;
    notifyDataSetChanged();
  }

  public void removeRequest(IncomingRequest request) {
    this.requests.remove(request);
    notifyDataSetChanged();
  }
}

package com.iic.shopingo.ui.trip_flow.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.iic.shopingo.ui.trip_flow.data.Request;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 04/03/15.
 */
public class RequestListAdapter extends BaseAdapter {
  private List<Request> requests = new ArrayList<>();
  private Context context;

  public RequestListAdapter(Context context, List<Request> requests) {
    this.context = context;
    this.requests = requests;
  }

  @Override
  public int getCount() {
    return requests.size();
  }

  @Override
  public Request getItem(int position) {
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
    Request req = getItem(position);
    item.setRequest(null, req.name, req.items.size(), req.offerInCents, req.status);
    return item;
  }
}

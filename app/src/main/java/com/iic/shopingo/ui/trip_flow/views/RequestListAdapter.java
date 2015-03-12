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
  private List<IncomingRequest> requests = new ArrayList<>();
  private Context context;

  public RequestListAdapter(Context context) {
    this.context = context;
    this.requests = new ArrayList<>(0);
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
    item.setRequest(
        req.getRequester().getAvatarUrl(),
        req.getRequester().getFirstName(),
        req.getShoppingList().getItems().size(),
        req.getShoppingList().getOffer(),
        req.getStatus()
    );
    return item;
  }

  public void setRequests(List<IncomingRequest> requests) {
    this.requests = requests;
    notifyDataSetChanged();
  }
}

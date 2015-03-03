package com.iic.shopingo.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.iic.shopingo.data.Request;
import com.iic.shopingo.ui.RequestListItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 03/03/15.
 */
public class RequestList extends Activity implements AdapterView.OnItemClickListener {

  private ListView list;
  private RequestListAdapter adapter;
  private OnRequestHandledListener listener;

  public interface OnRequestHandledListener {
    public void onRequestAccepted(Request request);
    public void onRequestRejected(Request request);
  }

  public void setOnRequestHandledListener(OnRequestHandledListener listener) {
    this.listener = listener;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    list = new ListView(this);
    setContentView(list);

    List<Request> requests = new ArrayList<>(10);

    for (int i = 0; i < 10; ++i) {
      Request req = new Request();
      req.photoUrl = "";
      req.name = "Moshe " + i;
      req.offerInCents = 100*i;
      req.location.coords.setLatitude(32.063146);
      req.location.coords.setLongitude(34.770706);
      req.location.city = "Tel Aviv";
      req.location.country = "Israel";
      req.location.address = "13 Rothschild Ave.";
      req.location.zipcode = "12345";
      req.location.phone = "12345";
      req.items.add("1 Milk");
      req.items.add("1 Bread");
      req.items.add("1 Cheese");
      req.items.add("12 Eggs");
      requests.add(req);
    }


    list.setOnItemClickListener(this);
    adapter = new RequestListAdapter(this, requests);
    list.setAdapter(adapter);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Request req = adapter.getItem(position);

    Intent intent = new Intent(this, RequestDetails.class);
    intent.putExtra(RequestDetails.EXTRA_REQUEST, req);
    startActivityForResult(intent, position);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode >= 0) {
      if (resultCode == RequestDetails.RESULT_ACCEPT || resultCode == RequestDetails.RESULT_REJECT) {
        Request req = adapter.getItem(requestCode);
        adapter.removeIndex(requestCode);
        if (listener != null) {
          if (resultCode == RequestDetails.RESULT_ACCEPT) {
            listener.onRequestAccepted(req);
          } else if (resultCode == RequestDetails.RESULT_REJECT) {
            listener.onRequestRejected(req);
          }
        }
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  public static class RequestListAdapter extends BaseAdapter {
    private List<Request> requests = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public RequestListAdapter(Context context, List<Request> requests) {
      this.context = context;
      this.requests = requests;
    }

    public void removeIndex(int index) {
      requests.remove(index);
      notifyDataSetChanged();
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
      item.setRequest(null, req.name, req.items.size(), req.offerInCents);
      return item;
    }
  }
}

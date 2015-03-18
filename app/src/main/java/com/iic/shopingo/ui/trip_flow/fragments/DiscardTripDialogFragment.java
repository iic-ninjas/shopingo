package com.iic.shopingo.ui.trip_flow.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A dialog that prompts the user if he's interested in discarding the trip when he presses up or back
 * Created by ifeins on 3/9/15.
 */
public class DiscardTripDialogFragment extends DialogFragment {

  private OnResultListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Discard trip?");
    builder.setMessage("This will cancel your trip and decline any existing requests.");
    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
          listener.onDiscardTripDialogOK();
        }
      }
    });
    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (listener != null) {
          listener.onDiscardTripDialogCancel();
        }
      }
    });

    return builder.create();
  }

  public void setOnResultListener(OnResultListener listener) {
    this.listener = listener;
  }

  public interface OnResultListener {
    public void onDiscardTripDialogOK();

    public void onDiscardTripDialogCancel();
  }
}

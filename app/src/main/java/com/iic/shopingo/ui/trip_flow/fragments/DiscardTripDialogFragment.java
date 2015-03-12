package com.iic.shopingo.ui.trip_flow.fragments;

import android.app.Activity;
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

  private DiscardTripDialogListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.
        setTitle("Discard trip?").
        setMessage("This will cancel your trip, preventing any requests coming in.").
        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            listener.onDiscardTripDialogOK();
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onDiscardTripDialogCancel();
      }
    });

    return builder.create();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    listener = (DiscardTripDialogListener) activity;
  }

  public interface DiscardTripDialogListener {
    public void onDiscardTripDialogOK();

    public void onDiscardTripDialogCancel();
  }
}

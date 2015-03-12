package com.iic.shopingo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by asafg on 10/03/15.
 */
public class ErrorDialogFragment extends DialogFragment {

  private String message;
  private AlertDialog dialog;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dismiss();
      }
    });
    builder.setTitle("Error");
    builder.setMessage(message);

    dialog = builder.create();
    return dialog;
  }

  public void setMessage(String message) {
    this.message = message;
    if (dialog != null) {
      dialog.setMessage(message);
    }
  }
}

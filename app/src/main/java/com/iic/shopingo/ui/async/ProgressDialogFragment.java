package com.iic.shopingo.ui.async;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by asafg on 10/03/15.
 */
public class ProgressDialogFragment extends DialogFragment {

  private String message;
  private ProgressDialog dialog;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    dialog = new ProgressDialog(getActivity());
    dialog.setIndeterminate(true);
    dialog.setMessage(message);
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    return dialog;
  }

  public void setMessage(String message) {
    this.message = message;
    if (dialog != null) {
      dialog.setMessage(message);
    }
  }
}

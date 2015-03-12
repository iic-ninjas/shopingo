package com.iic.shopingo.ui;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import bolts.Continuation;
import bolts.Task;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.BaseApiRequest;

/**
 * Created by asafg on 10/03/15.
 */
public class ApiTask<T extends ApiResult> {

  private ProgressDialogFragment progressDialogFragment;
  private ErrorDialogFragment errorDialogFragment;
  private FragmentManager fragmentManager;
  private String title;
  private BaseApiRequest<T> request;

  public ApiTask(final FragmentManager fragmentManager, String title, BaseApiRequest<T> request) {
    this.fragmentManager = fragmentManager;
    this.title = title;
    this.request = request;
  }

  public Task<T> execute() {
    progressDialogFragment = new ProgressDialogFragment();
    progressDialogFragment.setMessage(title);
    progressDialogFragment.show(fragmentManager, "progress");

    return request.executeAsync().continueWithTask(new Continuation<T, Task<T>>() {
      @Override
      public Task<T> then(Task<T> task) throws Exception {
        progressDialogFragment.dismiss();
        if (task.isFaulted()) {
          errorDialogFragment = new ErrorDialogFragment();
          errorDialogFragment.setMessage(task.getResult().errorMessage);
          errorDialogFragment.show(fragmentManager, "error");
        }
        return task;
      }
    });
  }
}

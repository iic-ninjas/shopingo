package com.iic.shopingo.api;

import android.util.Log;
import bolts.Task;
import java.util.concurrent.Callable;

/**
 * Created by asafg on 11/03/15.
 */
public abstract class BaseApiRequest<T extends ApiResult> {

  protected String authToken;
  protected Server server;

  public BaseApiRequest(String authToken) {
    this.authToken = authToken;
    server = new Server(authToken);
  }

  public abstract T executeSync();

  public Task<T> executeAsync() {
    final Task<T>.TaskCompletionSource source = Task.create();
    Task.callInBackground(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        Log.d("BaseApiRequest", "Starting request");
        T result = executeSync();
        Log.d("BaseApiRequest", "Got result");
        if (result.success) {
          Log.d("BaseApiRequest", "Success");
          source.setResult(result);
        } else {
          Log.d("BaseApiRequest", "Fail", new ApiRequestError(result));
          source.setError(new ApiRequestError(result));
        }
        return null;
      }
    });
    return source.getTask();
  }

  public static class ApiRequestError extends Exception {
    public ApiResult result;

    public ApiRequestError(ApiResult result) {
      super(result.errorMessage);
      this.result = result;
    }
  }
}

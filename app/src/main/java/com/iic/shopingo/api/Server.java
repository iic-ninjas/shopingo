package com.iic.shopingo.api;

import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iic.shopingo.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by assafgelber on 3/10/15.
 */
public class Server {
  private static final String HEADER_AUTH_KEY = "X-WYAI-FBID";

  private static final Uri BASE_URI = Uri.parse(BuildConfig.SERVER_HOST);

  public static <T> T get(String authToken, String path, Class<T> responseClass) throws IOException {
    return get(authToken, path, responseClass, new HashMap<String, Object>());
  }

  public static <T> T get(String authToken, String path, Class<T> responseClass, Map<String, Object> params) throws IOException {
    Uri.Builder requestUriBuilder = BASE_URI.buildUpon().encodedPath(path);
    for (String key : params.keySet()) {
      requestUriBuilder.appendQueryParameter(key, params.get(key).toString());
    }
    HttpGet request = new HttpGet(requestUriBuilder.build().toString());
    addAuthData(request, authToken);
    return executeRequest(request, responseClass);
  }

  public static <T> T post(String authToken, String path, Class<T> responseClass) throws IOException {
    return post(authToken, path, responseClass, new HashMap<String, Object>());
  }

  public static <T> T post(String authToken, String path, Class<T> responseClass, Map<String, Object> params) throws IOException {
    Uri.Builder requestUriBuilder = BASE_URI.buildUpon().encodedPath(path);
    HttpPost request = new HttpPost(requestUriBuilder.build().toString());
    request.setHeader("Content-Type", "application/json");
    request.setHeader("Accept", "application/json");
    addAuthData(request, authToken);
    JSONObject obj = new JSONObject(params);
    request.setEntity(new StringEntity(obj.toString()));
    return executeRequest(request, responseClass);
  }

  private static void addAuthData(HttpUriRequest request, String authToken) {
    if (authToken != null) {
      request.addHeader(HEADER_AUTH_KEY, authToken);
    }
  }

  private static <T> T executeRequest(HttpUriRequest request, Class<T> responseClass) throws IOException {
    HttpClient client = new DefaultHttpClient();
    InputStream content = null;
    try {
      HttpResponse response = client.execute(request);
      content = response.getEntity().getContent();
      Reader reader = new InputStreamReader(content);
      Gson gson = new GsonBuilder().create();
      T result = gson.fromJson(reader, responseClass);
      content.close();
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }
}

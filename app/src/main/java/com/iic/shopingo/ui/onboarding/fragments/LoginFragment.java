package com.iic.shopingo.ui.onboarding.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.widget.LoginButton;
import com.iic.shopingo.R;

public class LoginFragment extends Fragment {

  @InjectView(R.id.onboarding_login_btn)
  LoginButton loginBtn;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.onboarding_login, container, false);
    ButterKnife.inject(this, view);

    loginBtn.setReadPermissions("public_profile", "user_location");

    return view;
  }
}

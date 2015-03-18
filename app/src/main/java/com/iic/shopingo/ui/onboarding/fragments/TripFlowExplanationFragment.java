package com.iic.shopingo.ui.onboarding.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.iic.shopingo.R;

public class TripFlowExplanationFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.onboarding_trip_flow_explanation, container, false);
  }
}

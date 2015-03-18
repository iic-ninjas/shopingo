package com.iic.shopingo.ui.onboarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.iic.shopingo.ui.onboarding.fragments.IntroductionFragment;
import com.iic.shopingo.ui.onboarding.fragments.LoginFragment;
import com.iic.shopingo.ui.onboarding.fragments.RequestFlowExplanationFragment;
import com.iic.shopingo.ui.onboarding.fragments.TripFlowExplanationFragment;

/**
 * Created by assafgelber on 3/18/15.
 */
public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {
  public OnboardingPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return new IntroductionFragment();
      case 1:
        return new RequestFlowExplanationFragment();
      case 2:
        return new TripFlowExplanationFragment();
      case 3:
        return new LoginFragment();
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public int getCount() {
    return 4;
  }
}

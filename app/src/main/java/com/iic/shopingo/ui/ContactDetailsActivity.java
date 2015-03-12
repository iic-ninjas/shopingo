package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;
import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.trip.StartTrip;
import com.iic.shopingo.api.user.UpdateDetails;
import com.iic.shopingo.api.user.UserApiResult;
import com.iic.shopingo.dal.models.UserInfo;
import com.iic.shopingo.services.CurrentUser;
import com.squareup.picasso.Picasso;

public class ContactDetailsActivity extends ActionBarActivity {

  @InjectView(R.id.contact_first_name_editview)
  EditText firstNameEditView;

  @InjectView(R.id.contact_last_name_editview)
  EditText lastNameEditView;

  @InjectView(R.id.contact_street_editview)
  EditText streetEditView;

  @InjectView(R.id.contact_city_editview)
  EditText cityEditView;

  @InjectView(R.id.contact_phone_editview)
  EditText phoneEditView;

  @InjectView(R.id.contact_avatar_imageview)
  ImageView avatarImageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_details);
    ButterKnife.inject(this);

    prefillFields();
  }

  @OnClick(R.id.contact_save_btn)
  void onSaveClicked() {
    if (validateInput()) {
      final UserInfo info = gatherUserInfo();

      ApiTask<ApiResult> apiTask = new ApiTask<>(getSupportFragmentManager(), "Saving data...", new UpdateDetails(
          CurrentUser.getToken(),
          info.getUid(),
          info.getFirstName(),
          info.getLastName(),
          info.getStreet(),
          info.getCity(),
          info.getPhoneNumber()
      ));

      apiTask.execute().continueWith(new Continuation<ApiResult, Object>() {
        @Override
        public Object then(Task<ApiResult> task) throws Exception {
          if (!task.isFaulted() && !task.isCancelled()) {
            CurrentUser.getInstance().userInfo = info;
            CurrentUser.getInstance().save();
            Intent intent = new Intent(ContactDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
          } else {
            Toast.makeText(ContactDetailsActivity.this, task.getError().getMessage(), Toast.LENGTH_LONG).show();
          }
          return null;
        }
      });
    }
  }

  private boolean validateInput() {
    // TODO: actually validate stuff here
    return true;
  }

  private UserInfo gatherUserInfo() {
    return new UserInfo(
        CurrentUser.getInstance().userInfo.getUid(),
        firstNameEditView.getText().toString(),
        lastNameEditView.getText().toString(),
        streetEditView.getText().toString(),
        cityEditView.getText().toString(),
        phoneEditView.getText().toString()
    );
  }

  private void prefillFields() {
    UserInfo user = CurrentUser.getInstance().userInfo;
    firstNameEditView.setText(user.getFirstName());
    lastNameEditView.setText(user.getLastName());
    streetEditView.setText(user.getStreet());
    cityEditView.setText(user.getCity());
    phoneEditView.setText(user.getPhoneNumber());
    Picasso.with(this).load(user.getAvatarUrl()).into(avatarImageView);
  }
}

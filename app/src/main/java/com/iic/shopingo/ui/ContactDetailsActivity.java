package com.iic.shopingo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.iic.shopingo.R;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_details);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.contact_save_btn)
  void onSaveClicked() {
    Intent intent = new Intent(this, HomeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }
}

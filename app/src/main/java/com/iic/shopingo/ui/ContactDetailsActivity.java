package com.iic.shopingo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_contact_details, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.contact_save_btn)
  void onSaveClicked() {
    Toast.makeText(this, "Yeah I've been saved", Toast.LENGTH_LONG).show();
  }
}

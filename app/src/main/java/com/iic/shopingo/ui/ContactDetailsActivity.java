package com.iic.shopingo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.iic.shopingo.R;

public class ContactDetailsActivity extends ActionBarActivity {

  @InjectView(R.id.contact_first_name_editview)
  private EditText firstNameEditView;

  @InjectView(R.id.contact_last_name_editview)
  private EditText lastNameEditView;

  @InjectView(R.id.contact_street_editview)
  private EditText streetEditView;

  @InjectView(R.id.contact_city_editview)
  private EditText cityEditView;

  @InjectView(R.id.contact_phone_editview)
  private EditText phoneEditView;

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
}

package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.api.models.ApiContact;
import com.iic.shopingo.dal.models.Contact;

/**
 * Created by assafgelber on 3/10/15.
 */
public class ContactConverter {
  public static Contact convert(ApiContact apiContact) {
    return  new Contact(
        apiContact.facebookId,
        apiContact.firstName,
        apiContact.lastName,
        apiContact.phoneNumber,
        apiContact.streetAddress,
        apiContact.city,
        apiContact.latitude,
        apiContact.longitude
    );
  }
}

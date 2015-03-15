package com.iic.shopingo.api.request;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.models.ApiContact;
import com.iic.shopingo.api.models.converters.ContactConverter;
import com.iic.shopingo.dal.models.Contact;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asafg on 11/03/15.
 */
public class ShoppersApiResult extends ApiResult {
  public List<Contact> shoppers;

  public ShoppersApiResult(List<ApiContact> shoppers) {
    List<Contact> tempShoppers = new ArrayList<>();
    for (ApiContact apiShopper : shoppers) {
      tempShoppers.add(ContactConverter.convert(apiShopper));
    }
    this.shoppers = tempShoppers;
  }

  public ShoppersApiResult(String errorMessage) {
    super(errorMessage);
  }
}

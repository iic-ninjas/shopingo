package com.iic.shopingo.api.models.converters;

import com.iic.shopingo.api.models.ApiUserInfo;
import com.iic.shopingo.dal.models.UserInfo;

/**
 * Created by assafgelber on 3/10/15.
 */
public class UserInfoConverter {
  public static UserInfo convert(ApiUserInfo apiUserInfo) {
    return new UserInfo(
        apiUserInfo.facebookId,
        apiUserInfo.firstName,
        apiUserInfo.lastName,
        apiUserInfo.streetAddress,
        apiUserInfo.city,
        apiUserInfo.phoneNumber
    );
  }
}

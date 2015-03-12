package com.iic.shopingo.api.user;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.models.ApiUserInfo;
import com.iic.shopingo.api.models.converters.StatusConverter;
import com.iic.shopingo.api.models.converters.UserInfoConverter;
import com.iic.shopingo.dal.models.UserInfo;
import com.iic.shopingo.services.CurrentUser;

/**
 * Created by asafg on 11/03/15.
 */
public class UserApiResult extends ApiResult {
  public UserInfo userContactInfo;
  public CurrentUser.State userState;

  public UserApiResult(ApiUserInfo apiUserInfo) {
    super();
    this.userContactInfo = UserInfoConverter.convert(apiUserInfo);
    this.userState = StatusConverter.UserState.convert(apiUserInfo.state);
  }

  public UserApiResult(String errorMessage) {
    super(errorMessage);
  }
}

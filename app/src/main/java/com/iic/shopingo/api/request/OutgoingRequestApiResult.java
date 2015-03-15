package com.iic.shopingo.api.request;

import com.iic.shopingo.api.ApiResult;
import com.iic.shopingo.api.models.ApiOutgoingRequest;
import com.iic.shopingo.api.models.converters.OutgoingRequestConverter;
import com.iic.shopingo.dal.models.OutgoingRequest;

/**
 * Created by asafg on 11/03/15.
 */
public class OutgoingRequestApiResult extends ApiResult {

  public OutgoingRequest request;

  public OutgoingRequestApiResult(ApiOutgoingRequest request) {
    this.request = OutgoingRequestConverter.convert(request);
  }

  public OutgoingRequestApiResult(String errorMessage) {
    super(errorMessage);
  }
}

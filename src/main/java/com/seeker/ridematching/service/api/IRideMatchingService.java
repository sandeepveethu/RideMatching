package com.seeker.ridematching.service.api;

import com.seeker.ridematching.exception.ValidationException;

public interface IRideMatchingService {

  /**
   * Match rider to a driver
   *
   * @param riderName
   * @return matched driver's name
   * @throws ValidationException
   */
  String matchRide(String riderName) throws ValidationException;
}

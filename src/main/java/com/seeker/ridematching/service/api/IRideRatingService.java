package com.seeker.ridematching.service.api;

import com.seeker.ridematching.exception.ValidationException;

/**
 * Contract for Service which should handle ride rating operations.
 *
 * @author sandeep
 */
public interface IRideRatingService {

  /**
   * Record ride ratings as given by both rider and driver. Update rider and driver history as well.
   *
   * @param riderName
   * @param riderRating
   * @param driverName
   * @param driverRating
   * @return
   * @throws ValidationException
   */
  String recordRideRating(String riderName, int riderRating, String driverName, int driverRating)
      throws ValidationException;

  boolean eitherRatedOtherOneStar(String driverName, String riderName);

  String dumpAllData();
}

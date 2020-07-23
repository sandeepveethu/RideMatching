package com.seeker.ridematching.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.seeker.ridematching.domain.Rider;
import com.seeker.ridematching.exception.ValidationException;
import com.seeker.ridematching.service.api.IDriverService;
import com.seeker.ridematching.service.api.IRideMatchingService;
import com.seeker.ridematching.service.api.IRideRatingService;
import com.seeker.ridematching.service.api.IRiderService;

public class DefaultRideMatchingService implements IRideMatchingService {
  private IRiderService riderService = DefaultRiderService.getInstance();
  private IDriverService driverService = DefaultDriverService.getInstance();
  private IRideRatingService rideRatingService = DefaultRideRatingService.getInstance();

  private DefaultRideMatchingService() {};

  private static class SingletonHolder {
    public static DefaultRideMatchingService instance = new DefaultRideMatchingService();
  }

  public static DefaultRideMatchingService getInstance() {
    return SingletonHolder.instance;
  }

  @Override
  public String matchRide(String riderName) throws ValidationException {
    Objects.requireNonNull(riderName);

    Rider rider = riderService.getRider(riderName);
    String matchedDriverName = null;

    if (rider == null) {
      throw new ValidationException(
          String.format("Failed to match rider. Rider[name=%s] doesn't exist.", riderName));
    } else {

      double riderRating = rider.getAverageRating();

      List<String> drivers = driverService.findDriversInRatingRange(riderRating, 5);

      drivers =
          drivers
              .stream()
              .filter(
                  driverName -> !rideRatingService.eitherRatedOtherOneStar(driverName, riderName))
              .collect(Collectors.toList());

      if (drivers.isEmpty()) {
        drivers = driverService.findDriversInRatingRange(1, 5);
      }

      // discard driver, if driver or the user have given the other 1 rating in past
      for (String driverName : drivers) {
        if (!rideRatingService.eitherRatedOtherOneStar(driverName, riderName)) {
          matchedDriverName = driverName;
          break;
        }
      }
    }
    if (matchedDriverName == null) {
      matchedDriverName = "NO MATCH FOUND";
    }
    return matchedDriverName;
  }
}

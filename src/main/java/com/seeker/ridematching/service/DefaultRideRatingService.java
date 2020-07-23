package com.seeker.ridematching.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.seeker.ridematching.domain.Driver;
import com.seeker.ridematching.domain.RideDetails;
import com.seeker.ridematching.domain.Rider;
import com.seeker.ridematching.exception.EntityExistsException;
import com.seeker.ridematching.exception.UnknownEntityException;
import com.seeker.ridematching.exception.ValidationException;
import com.seeker.ridematching.service.api.IDriverService;
import com.seeker.ridematching.service.api.IRideRatingService;
import com.seeker.ridematching.service.api.IRiderService;

public class DefaultRideRatingService implements IRideRatingService {

  private List<RideDetails> rideDetailsStore = new ArrayList<>();
  private Map<String, Set<String>> driversOneRatedRiders = new HashMap<>();
  private Map<String, Set<String>> ridersOneRatedDrivers = new HashMap<>();
  private IRiderService riderService = DefaultRiderService.getInstance();
  private IDriverService driverService = DefaultDriverService.getInstance();

  private DefaultRideRatingService() {};

  private static class SingletonHolder {
    public static DefaultRideRatingService instance = new DefaultRideRatingService();
  }

  public static DefaultRideRatingService getInstance() {
    return SingletonHolder.instance;
  }

  @Override
  public String recordRideRating(
      String riderName, int riderRating, String driverName, int driverRating)
      throws ValidationException {
    validateRating(riderRating);
    validateRating(driverRating);

    RideDetails rideDetails = new RideDetails();
    rideDetails.setRiderName(riderName);
    rideDetails.setRiderRating(riderRating);
    rideDetails.setDriverName(driverName);
    rideDetails.setDriverRating(driverRating);

    rideDetailsStore.add(rideDetails);

    // update rider bio
    if (riderService.getRider(riderName) == null) {
      Rider rider = new Rider();
      rider.setName(riderName);
      rider.setAverageRating(riderRating);
      rider.setRideCount(1);
      try {
        riderService.addRider(rider);
      } catch (EntityExistsException e) {
        // ignore, we checked first
      }
    } else {
      Rider rider = riderService.getRider(riderName);
      double newAvgRating =
          (rider.getAverageRating() * rider.getRideCount() + riderRating)
              / (rider.getRideCount() + 1);
      rider.setRideCount(rider.getRideCount() + 1);
      rider.setAverageRating(newAvgRating);
      try {
        riderService.modifyRider(rider);
      } catch (UnknownEntityException e) {
        // ignore, we checked first
      }
    }

    // update driver bio
    if (driverService.getDriver(driverName) == null) {
      Driver driver = new Driver();
      driver.setName(driverName);
      driver.setAverageRating(driverRating);
      driver.setRideCount(1);
      try {
        driverService.addDriver(driver);
      } catch (EntityExistsException e) {
        // ignore, we checked first
      }
    } else {
      Driver driver = driverService.getDriver(driverName);
      double newAvgRating =
          (driver.getAverageRating() * driver.getRideCount() + driverRating)
              / (driver.getRideCount() + 1);
      driver.setRideCount(driver.getRideCount() + 1);
      driver.setAverageRating(newAvgRating);
      try {
        driverService.modifyDriver(driver);
      } catch (UnknownEntityException e) {
        // ignore, we checked first
      }
    }

    // if driver rated the rider with 1 star, add to that driver's one rated riders set
    if (riderRating == 1) {
      if (!driversOneRatedRiders.containsKey(driverName)) {
        driversOneRatedRiders.put(driverName, new HashSet<>());
      }
      Set<String> riders = driversOneRatedRiders.get(driverName);
      if (!riders.contains(riderName)) {
        riders.add(riderName);
      }
    }

    // if rider rated the driver with 1 star, add to that rider's one rated drivers set
    if (driverRating == 1) {
      if (!ridersOneRatedDrivers.containsKey(riderName)) {
        ridersOneRatedDrivers.put(riderName, new HashSet<>());
      }
      Set<String> drivers = ridersOneRatedDrivers.get(riderName);
      if (!drivers.contains(driverName)) {
        drivers.add(driverName);
      }
    }

    return "Ride ratings successfully recorded.";
  }

  private void validateRating(int riderRating) throws ValidationException {
    if (riderRating < 1 || riderRating > 5) {
      throw new ValidationException("Rating should be between [1,5].");
    }
  }

  @Override
  public boolean eitherRatedOtherOneStar(String driverName, String riderName) {
    Objects.requireNonNull(driverName);
    Objects.requireNonNull(riderName);

    boolean result = false;

    if (driversOneRatedRiders.containsKey(driverName)) {
      result = driversOneRatedRiders.get(driverName).contains(riderName);
    }
    if (ridersOneRatedDrivers.containsKey(riderName)) {
      result = ridersOneRatedDrivers.get(riderName).contains(driverName);
    }
    return result;
  }

  @Override
  public String dumpAllData() {
    StringBuilder sb = new StringBuilder();
    sb.append(riderService.dumpAllData());
    sb.append(driverService.dumpAllData());
    sb.append("1 Rating Info\n");
    driversOneRatedRiders.forEach(
        (driver, riders) -> {
          sb.append(driver + " rated 1 ");
          sb.append("[");
          riders.forEach(rider -> sb.append(rider).append(","));
          sb.append("]\n");
        });
    sb.append("\n");
    ridersOneRatedDrivers.forEach(
        (driver, riders) -> {
          sb.append(driver + " rated 1 ");
          sb.append("[");
          riders.forEach(rider -> sb.append(rider).append(","));
          sb.append("]\n");
        });
    return sb.toString();
  }
}

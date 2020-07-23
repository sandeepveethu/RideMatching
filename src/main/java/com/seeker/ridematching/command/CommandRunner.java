package com.seeker.ridematching.command;

import java.util.Arrays;

import com.seeker.ridematching.exception.InvalidCommandException;
import com.seeker.ridematching.exception.ValidationException;
import com.seeker.ridematching.service.DefaultRideMatchingService;
import com.seeker.ridematching.service.DefaultRideRatingService;
import com.seeker.ridematching.service.api.IRideMatchingService;
import com.seeker.ridematching.service.api.IRideRatingService;

public class CommandRunner {
  private IRideRatingService rideRatingService = DefaultRideRatingService.getInstance();
  private IRideMatchingService rideMatchingService = DefaultRideMatchingService.getInstance();

  public String runCommand(String command) throws InvalidCommandException {
    String[] tokens = command.split(" ");
    if (tokens.length >= 1) {
      switch (tokens[0]) {
        case "ADD":
          return addRating(Arrays.copyOfRange(tokens, 1, tokens.length));
        case "MATCHRIDE":
          return findMatch(Arrays.copyOfRange(tokens, 1, tokens.length));
        case "DUMP":
          return dumpAllData();
        default:
          throw new InvalidCommandException("Unknown command.");
      }
    } else {
      // empty command was issued, ignore
      return null;
    }
  }

  private String addRating(String[] tokens) throws InvalidCommandException {
    if (tokens.length != 4) {
      throw new InvalidCommandException(
          "Invalid input. Try, ADD <RIDER> <RIDER_RATING> <DRIVER> <DRIVER_RATING>");
    } else {
      String riderName = tokens[0];
      int riderRating;
      String driverName = tokens[2];
      int driverRating;

      try {
        riderRating = Integer.parseInt(tokens[1]);
        driverRating = Integer.parseInt(tokens[3]);
      } catch (NumberFormatException e) {
        throw new InvalidCommandException("Rating should be a number.");
      }

      try {
        return rideRatingService.recordRideRating(riderName, riderRating, driverName, driverRating);
      } catch (ValidationException e) {
        throw new InvalidCommandException(e.getMessage(), e);
      }
    }
  }

  private String findMatch(String[] tokens) throws InvalidCommandException {
    if (tokens.length != 1) {
      throw new InvalidCommandException("Invalid input. Try MATCHRIDE <RIDER>");
    } else {
      String riderName = tokens[0];
      try {
        return rideMatchingService.matchRide(riderName);
      } catch (ValidationException e) {
        throw new InvalidCommandException(e.getMessage(), e);
      }
    }
  }

  private String dumpAllData() {
    return rideRatingService.dumpAllData();
  }
}

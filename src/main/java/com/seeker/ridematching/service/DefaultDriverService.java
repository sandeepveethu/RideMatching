package com.seeker.ridematching.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.seeker.ridematching.domain.Driver;
import com.seeker.ridematching.exception.EntityExistsException;
import com.seeker.ridematching.exception.UnknownEntityException;
import com.seeker.ridematching.service.api.IDriverService;

public class DefaultDriverService implements IDriverService {
  private Map<String, Driver> driverStore = new HashMap<>();

  private DefaultDriverService() {};

  private static class SingletonHolder {
    public static DefaultDriverService instance = new DefaultDriverService();
  }

  public static DefaultDriverService getInstance() {
    return SingletonHolder.instance;
  }

  @Override
  public void addDriver(Driver driver) throws EntityExistsException {
    Objects.requireNonNull(driver);
    Objects.requireNonNull(driver.getName());
    if (driverStore.containsKey(driver.getName())) {
      throw new EntityExistsException(
          String.format("Driver[name=%s] already exists.", driver.getName()));
    } else {
      driverStore.put(driver.getName(), driver);
    }
  }

  @Override
  public Driver getDriver(String driverName) {
    Objects.requireNonNull(driverName);
    return driverStore.get(driverName);
  }

  @Override
  public void modifyDriver(Driver driver) throws UnknownEntityException {
    Objects.requireNonNull(driver);
    Objects.requireNonNull(driver.getName());
    if (driverStore.containsKey(driver.getName())) {
      driverStore.put(driver.getName(), driver);
    } else {
      throw new UnknownEntityException(
          String.format("Failed to modify. Driver[name=%s] doesn't exist.", driver.getName()));
    }
  }

  @Override
  public List<String> findDriversInRatingRange(double lRange, double rRange) {
    Set<Driver> driverSet =
        new TreeSet<Driver>(
            new Comparator<Driver>() {

              @Override
              public int compare(Driver o1, Driver o2) {
                if (o1.getAverageRating() <= o2.getAverageRating()) {
                  return 1;
                } else {
                  return -1;
                }
              }
            });

    driverStore.forEach(
        (name, driver) -> {
          if (driver.getAverageRating() >= lRange && driver.getAverageRating() <= rRange) {
            driverSet.add(driver);
          }
        });

    return driverSet.stream().map(Driver::getName).collect(Collectors.toList());
  }

  @Override
  public String dumpAllData() {
    StringBuilder sb = new StringBuilder();
    sb.append("DRIVERS\n");
    driverStore.forEach(
        (name, driver) -> {
          sb.append(name + " : " + driver.getAverageRating()).append("\n");
        });
    return sb.toString();
  }
}

package com.seeker.ridematching.service.api;

import java.util.List;

import com.seeker.ridematching.domain.Driver;
import com.seeker.ridematching.exception.EntityExistsException;
import com.seeker.ridematching.exception.UnknownEntityException;

public interface IDriverService {
  public void addDriver(Driver rider) throws EntityExistsException;

  public Driver getDriver(String riderId);

  public void modifyDriver(Driver rider) throws UnknownEntityException;

  List<String> findDriversInRatingRange(double lRange, double rRange);

  public String dumpAllData();
}

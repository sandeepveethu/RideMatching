package com.seeker.ridematching.service.api;

import com.seeker.ridematching.domain.Rider;
import com.seeker.ridematching.exception.EntityExistsException;
import com.seeker.ridematching.exception.UnknownEntityException;

public interface IRiderService {
  public void addRider(Rider rider) throws EntityExistsException;

  public Rider getRider(String riderId);

  public void modifyRider(Rider rider) throws UnknownEntityException;

  public String dumpAllData();
}

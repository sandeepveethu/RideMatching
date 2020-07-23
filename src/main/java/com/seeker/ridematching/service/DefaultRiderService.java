package com.seeker.ridematching.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.seeker.ridematching.domain.Rider;
import com.seeker.ridematching.exception.EntityExistsException;
import com.seeker.ridematching.exception.UnknownEntityException;
import com.seeker.ridematching.service.api.IRiderService;

public class DefaultRiderService implements IRiderService {
  private Map<String, Rider> riderStore = new HashMap<>();

  private DefaultRiderService() {};

  private static class SingletonHolder {
    public static DefaultRiderService instance = new DefaultRiderService();
  }

  public static DefaultRiderService getInstance() {
    return SingletonHolder.instance;
  }

  @Override
  public void addRider(Rider rider) throws EntityExistsException {
    Objects.requireNonNull(rider);
    Objects.requireNonNull(rider.getName());
    if (riderStore.containsKey(rider.getName())) {
      throw new EntityExistsException(
          String.format("Rider[name=%s] already exists.", rider.getName()));
    } else {
      riderStore.put(rider.getName(), rider);
    }
  }

  @Override
  public Rider getRider(String riderName) {
    Objects.requireNonNull(riderName);
    return riderStore.get(riderName);
  }

  @Override
  public void modifyRider(Rider rider) throws UnknownEntityException {
    Objects.requireNonNull(rider);
    Objects.requireNonNull(rider.getName());
    if (riderStore.containsKey(rider.getName())) {
      riderStore.put(rider.getName(), rider);
    } else {
      throw new UnknownEntityException(
          String.format("Failed to modify. Rider[name=%s] doesn't exist.", rider.getName()));
    }
  }

  @Override
  public String dumpAllData() {
    StringBuilder sb = new StringBuilder();
    sb.append("RIDERS\n");
    riderStore.forEach(
        (name, rider) -> {
          sb.append(name + " : " + rider.getAverageRating()).append("\n");
        });
    return sb.toString();
  }
}

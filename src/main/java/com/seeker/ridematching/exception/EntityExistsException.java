package com.seeker.ridematching.exception;

public class EntityExistsException extends Exception {
  public EntityExistsException() {
    super();
  }

  public EntityExistsException(String message) {
    super(message);
  }
}

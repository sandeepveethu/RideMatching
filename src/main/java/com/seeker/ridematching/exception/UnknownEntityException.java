package com.seeker.ridematching.exception;

/**
 * Thrown to indicate that entity doesn't exist in store.
 *
 * @author sandeep
 */
public class UnknownEntityException extends Exception {
  private static final long serialVersionUID = 1L;

  public UnknownEntityException(String message) {
    super(message);
  }
}

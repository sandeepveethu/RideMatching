package com.seeker.ridematching.exception;

/**
 * Thrown to indicate that invalid input parameters were passed.
 *
 * @author sandeep
 */
public class ValidationException extends Exception {

  private static final long serialVersionUID = 1L;

  public ValidationException(String message) {
    super(message);
  }
}

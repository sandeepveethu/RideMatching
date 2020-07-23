package com.seeker.ridematching.exception;

/**
 * Thrown to indicate that an invalid command was issued.
 *
 * @author sandeep
 */
public class InvalidCommandException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidCommandException() {
    super();
  }

  public InvalidCommandException(String message) {
    super(message);
  }

  public InvalidCommandException(String message, Throwable cause) {
    super(message, cause);
  }
}

package com.francesco.codeexercise.exception;

public class InputFileMalformedException extends RuntimeException {

  public InputFileMalformedException(String message, Exception cause) {
    super(message, cause);
  }

}

package com.crio.coderhack.dto.rest_input_format;

public class UserInput {
  private String username;
  private String userId;


  public UserInput(String username, String userId) {
    this.username = username;
    this.userId = userId;
  }


  public String getUsername() {
    return username;
  }


  public String getUserId() {
    return userId;
  }


}

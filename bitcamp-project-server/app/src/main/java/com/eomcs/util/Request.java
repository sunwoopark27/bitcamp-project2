package com.eomcs.util;

import java.util.List;

public class Request {
  private String command;
  private List<String> data;
  public String getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return "Request [command=" + command + ", data=" + data + "]";
  }

  public void setCommand(String command) {
    this.command = command;
  }
  public List<String> getData() {
    return data;
  }
  public void setData(List<String> data) {
    this.data = data;
  }


}

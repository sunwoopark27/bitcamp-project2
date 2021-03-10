package com.eomcs.util;

import java.util.List;

public class Request {

  private String Command;
  private List<String> data;

  @Override
  public String toString() {
    return "Request [Command=" + Command + ", data=" + data + "]";
  }

  public String getCommand() {
    return Command;
  }
  public void setCommand(String command) {
    Command = command;
  }
  public List<String> getData() {
    return data;
  }
  public void setData(List<String> data) {
    this.data = data;
  }


}

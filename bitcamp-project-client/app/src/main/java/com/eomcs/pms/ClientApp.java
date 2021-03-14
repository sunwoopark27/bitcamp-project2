package com.eomcs.pms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientApp {
  public static void main(String[] args) {
    try(Socket socket = new Socket("localhost", 8888);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());) {

      out.writeUTF("hello");
      out.flush();

      String response = in.readUTF();
      System.out.println(response);

    }catch (Exception e) {
      System.out.println("서버와 통신하는 중 오류 발생!");
    }
  }
}

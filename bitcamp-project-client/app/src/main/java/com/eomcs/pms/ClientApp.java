package com.eomcs.pms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import com.eomcs.util.Prompt;

public class ClientApp {
  String serverAddress;
  int port;

  public ClientApp(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
  }
  public static void main(String[] args) {
    ClientApp app = new ClientApp("localhost",8888);
    app.execute();
  }
  public void execute() {

    try(Socket socket = new Socket(this.serverAddress, this.port);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream())) {

      while(true) {
        String message = Prompt.inputString("명령> ");
        out.writeUTF(message);

        out.writeInt(1);

        out.writeUTF("aaaa");
        out.writeUTF("bbbb");
        out.writeUTF("cccc");

        out.flush();

        String response = in.readUTF();
        int length = in.readInt();

        ArrayList<String> data = null;
        if(length > 0) {
          data = new ArrayList<>();
          for (int i = 0; i < length; i++) {
            data.add(in.readUTF());
          }
        }

        System.out.println("----------------------------");
        System.out.printf("작업 결과: %s\n", response);
        System.out.printf("데이터 개수: %d\n", length);
        if (data != null) {
          System.out.println("데이터:");
          for (String str : data) {
            System.out.println(str);
          }
        }

        if(message.equals("quit")) {
          break;
        }
      }

      Prompt.close();

    } catch (Exception e) {
      System.out.println("서버와 통신하는 중에 오류 발생!");
    }

  }
}
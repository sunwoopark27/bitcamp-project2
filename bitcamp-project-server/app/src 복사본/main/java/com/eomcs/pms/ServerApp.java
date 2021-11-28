package com.eomcs.pms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import com.eomcs.util.concurrent.ThreadPool;

public class ServerApp {

  int port;

  public static void main(String[] args) {
    ServerApp app = new ServerApp(8888);
    app.service();
  }

  public ServerApp(int port) {
    this.port = port;
  }

  public void service() {


    // 스레드 풀 준비
    ThreadPool threadPool = new ThreadPool();
    // 클라이언트 연결을 기다리는 서버 소켓 생성
    try (ServerSocket serverSocket = new ServerSocket(this.port)) {

      System.out.println("서버 실행!");

      while (true) {
        Socket socket = serverSocket.accept();

        // 예전 방식: 직접 스레드를 만들어 작업을 실행시킴 
        //new Thread(() -> processRequest(socket)).start();

        // 새 방식: 작업 실행을 스레드풀에 맡긴다.
        threadPool.execute(() -> processRequest(socket));
      }

    } catch (Exception e) {
      System.out.println("서버 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }

  public void processRequest(Socket socket) {
    try (
        Socket clientSocket = socket; // 닫아주기 위해서 다시 한번 받아 
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream())) {

      loop : while (true) {
        // 클라이언트가 보낸 요청을 읽는다.
        String  requestLine = in.readLine();

        if (requestLine.equalsIgnoreCase ("exit") || requestLine.equalsIgnoreCase("quit")) {
          in.readLine();
          out.println("GoodBye!");
          out.println();
          out.flush();
          break loop;
        }

        // 클라이언트가 보낸 명령을 서버창에 출력한다.
        System.out.println(requestLine);

        // 클라이언트가 보낸 데이터를 읽는다.
        while (true) {
          String line = in.readLine();
          if (line.length() == 0) { 
            break;
          }
          // 클라이언트에서 보낸것을 서버창에 출력해보자!
          System.out.println(line);
        }

        System.out.println("-----------------------------");
        // 클라이언트에게 응답한다.
        out.println("OK");
        out.printf("====> %s\n", requestLine);
        out.println();
        out.flush();

      }

    } catch (Exception e) {
      System.out.println("클라이언트의 요청을 처리하는 중에 오류 발생!");
      e.printStackTrace();
    }
  }

}

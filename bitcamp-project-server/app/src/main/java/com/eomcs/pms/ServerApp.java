package com.eomcs.pms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.eomcs.pms.table.BoardTable;
import com.eomcs.pms.table.DataTable;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

// 데이터를 파일에 보관하고 꺼내는 일을 할 애플리케이션
public class ServerApp {

  int port;
  HashMap<String,DataTable> tableMap = new HashMap<>();

  public static void main(String[] args) {
    ServerApp app = new ServerApp(8888);
    app.service();
  }

  public ServerApp(int port) {
    this.port = port;
  }

  public void service() {

    // 요청을 처리할 테이블 객체를 준비한다.
    tableMap.put("board/", new BoardTable());

    // 클라이언트 연결을 기다는 서버 소켓 생성
    try (ServerSocket serverSocket = new ServerSocket(this.port)) {

      System.out.println("서버 실행!");

      processRequest(serverSocket.accept());//accept() 가 리턴할 때 이메서드 호출

    } catch (Exception e) {
      System.out.println("서버 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }

  private void processRequest(Socket socket) {
    // 소켓이 넘겨주는 아웃풋인풋 스트림 불편 우리는 그래서 데이터아웃풋인풋스트림 데코레이터 붙임
    try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream())) {

      while (true) {

        // 리퀘스트에 리턴해줌 요구정보를 받아서
        Request request = receiveRequest(in);
        // 뭐라고 요청했는지 눈으로 보고싶어서 출력 흐름을 먼저 보면서 코드 봐
        log(request);

        if (request.getCommand().equals("quit")) {
          sendResponse(out, "success");
          break;
        }

        DataTable dataTable = findDataTable(request.getCommand());

        if (dataTable != null) {
          Response response = new Response();
          try {
            dataTable.service(request, response);

            sendResponse(
                out,
                "success",
                response.getDataList().toArray(new String[response.getDataList().size()]));

          } catch (Exception e) {
            sendResponse(
                out,
                "error",
                e.getMessage() != null ? e.getMessage() : e.getClass().getName());
          }
        } else {
          sendResponse(out, "error", "해당 요청을 처리할 수 없습니다!");
        }

        out.flush();
      }

    } catch (Exception e) {
      System.out.println("클라이언트의 요청을 처리하는 중에 오류 발생!");
      e.printStackTrace();
    }
  }

  private DataTable findDataTable(String command) {
    Set<String> keySet = tableMap.keySet();
    for (String key : keySet) {
      if (command.startsWith(key)) {
        return tableMap.get(key);
      }
    }
    return null;
  }

  private Request receiveRequest(DataInputStream in) throws Exception{
    Request request = new Request();
    // 1) 명령어 문자열을 읽는다.
    request.setCommand(in.readUTF());

    // 2) 클라이언트가 보낸 데이터의 개수를 읽는다.
    int length = in.readInt();

    // 3) 클라이언트가 보낸 데이터를 읽어서 List 컬렉션에 담는다.
    ArrayList<String> data = null;
    if (length > 0) {
      data = new ArrayList<>();
      for (int i = 0; i < length; i++) {
        data.add(in.readUTF());
      }
      request.setData(data);
    }

    return request;
  }

  private void sendResponse(DataOutputStream out, String status, String... data) throws Exception {
    out.writeUTF(status);
    out.writeInt(data.length);
    for(int i = 0; i < data.length; i++) {
      out.writeUTF(data[i]);
    }
    out.flush();

  }

  private void log(Request request) {
    System.out.println("-------------------------------");
    System.out.printf("명령: %s\n", request.getCommand());

    List<String> data = request.getData();
    System.out.printf("데이터 개수: %d\n", data == null? 0 : data.size());
    if (data != null) {
      System.out.println("데이터:");
      for (String str : data) {
        System.out.println(str);
      }
    }
  }
}

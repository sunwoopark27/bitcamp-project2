package com.eomcs.pms.table;

import java.io.File;
import java.sql.Date;
import java.util.List;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

public class BoardTable implements DataTable {

  File jsonFile = new File("boards.json");
  List<Board> list;

  public BoardTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Board.class);
  }
  @Override
  public void service(Request request, Response response) throws Exception {
    Board board = null;
    String[] fields = null;

    switch(request.getCommand()) {
      case "board/insert":
        // data에서 CSV 형식으로 표현된 문자열을 꺼내 각 필드 값으로 분리한다.
        fields = request.getData().get(0).split(",");

        // CSV 데이터를 Board 객체에 저장한다.
        board = new Board();

        // 새 게시글의 번호
        if (list.size() > 0) {
          board.setNo(list.get(list.size() - 1).getNo() + 1);
        } else {
          board.setNo(1);
        }

        board.setTitle(fields[0]);
        board.setContent(fields[1]);
        board.setWriter(fields[2]);
        board.setRegisteredDate(new Date(System.currentTimeMillis()));

        list.add(board);

        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "board/selectall":
        break;
      case "board/select":
        break;
      case "board/update":
        break;
      case "board/delete":
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");

    }
  }

  private Board getBoard(int boardNo) {
    for(Board b : list) {
      if(b.getNo() == boardNo) {
        return b;
      }
    }
    return null;
  }
}

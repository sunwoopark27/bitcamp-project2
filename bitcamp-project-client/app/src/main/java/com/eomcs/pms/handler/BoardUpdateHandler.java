package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.Prompt;

public class BoardUpdateHandler implements Command {

  @Override
  public void service(DataInputStream in, DataOutputStream out) {
    try {
      System.out.println("[게시글 변경]");

      Board b = new Board();

      b.setNo(Prompt.inputInt("번호? "));
      b.setTitle(Prompt.inputString("제목? "));
      b.setContent(Prompt.inputString("내용? "));

      out.writeUTF("board/update");
      out.writeInt(1);
      out.writeUTF(String.format("%d,%s,%s", b.getNo(), b.getTitle(), b.getContent()));
      out.flush();

      String status = in.readUTF();
      in.readInt();

      if (status.equals("error")) {
        System.out.println(in.readUTF());
        return;
      }

      System.out.println("게시글을 변경하였습니다.");

    }catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}







package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.eomcs.util.Prompt;

public class BoardDeleteHandler implements Command {

  @Override
  public void service(DataInputStream in, DataOutputStream out) {
    try {
      System.out.println("[게시글 삭제]");

      int no = Prompt.inputInt("번호? ");
      out.writeUTF("board/delete");
      out.writeInt(1);
      out.writeUTF(Integer.toString(no));
      out.flush();

      String status = in.readUTF();
      in.readInt();

      if(status.equals("error")) {
        System.out.println(in.readUTF());
        return;
      }
      System.out.println("게시글을 삭제하였습니다.");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}







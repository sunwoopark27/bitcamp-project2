package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.domain.Task;
import com.eomcs.util.Prompt;

public class TaskAddHandler implements Command {

  MemberValidator memberValidator;

  public TaskAddHandler(MemberValidator memberValidator) {
    this.memberValidator = memberValidator;
  }


  @Override
  public void service() throws Exception {
    System.out.println("[작업 등록]");

    List<Project> projects = new ArrayList<>();
    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select" 
                + "    p.no,"
                + "    p.title,"
                + "    p.sdt,"
                + "    p.edt,"
                + "    m.no as owner_no,"
                + "    m.name as owner_name"
                + "  from pms_project p"
                + "    inner join pms_member m on p.owner=m.no"
                + "  order by title asc");
        ResultSet rs = stmt.executeQuery()) {

      while(rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
      }

      System.out.println("프로젝트들: ");
      if (projects.size() == 0) {
        System.out.println("현재 등록된 프로젝트가 없습니다!");
        return;
      }

      int selectedProjectNo = 0;
      loop : while (true) {
        String input = Prompt.inputString("프로젝트 번호?(취소: 빈 문자열) ");
        if (input.length() == 0) {
          System.out.println("작업 등록을 취소합니다. ");
          return;
        }
        try {
          selectedProjectNo = Integer.parseInt(input);
        } catch (Exception e) {
          System.out.println("숫자를 입력하세요!");
          continue;
        }
        for (Project p : projects) {
          if(p.getNo() == selectedProjectNo) {
            break loop;
          }
        }
      }
      System.out.println("유효하지 않은 프로젝트 번호 입니다.");


      Task t = new Task();
      t.setContent(Prompt.inputString("내용? "));
      t.setDeadline(Prompt.inputDate("마감일? "));
      t.setStatus(Prompt.inputInt("상태?\n0: 신규\n1: 진행중\n2: 완료\n> "));

      t.setOwner(memberValidator.inputMember("담당자?(취소: 빈 문자열) "));
      if (t.getOwner() == null) {
        System.out.println("작업 등록을 취소하였습니다.");
        return;
      }

      try (PreparedStatement stmt2 =
          con.prepareStatement("insert into pms_task(content, deadline, owner, status, project_no) values(?,?,?,?,?)");) {

        stmt2.setString(1, t.getContent());
        stmt2.setDate(2, t.getDeadline());
        stmt2.setInt(3, t.getOwner().getNo());
        stmt2.setInt(4, t.getStatus());
        stmt2.setInt(5, selectedProjectNo);

        stmt2.executeUpdate();

        System.out.println("게시글을 등록하였습니다.");
      }
    }
  }
}
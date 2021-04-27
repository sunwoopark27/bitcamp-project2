package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Task;
import com.eomcs.pms.service.TaskService;

@SuppressWarnings("serial")
@WebServlet("/task/add")
public class TaskAddHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/plain;charset=UTF-8");
    TaskService taskService = (TaskService) request.getServletContext().getAttribute("taskService");
    PrintWriter out = response.getWriter();

    out.println("[작업 등록]");

    // 4) 작업 정보를 입력 받는다.
    try {
      Task t = new Task();
      t.setProjectNo(Integer.parseInt(request.getParameter("projectNo")));
      t.setContent(request.getParameter("content"));
      t.setDeadline(Date.valueOf(request.getParameter("deadLine")));
      t.setStatus(Integer.parseInt(request.getParameter("status ")));
      Member loginUser = (Member) request.getSession().getAttribute("loginUser");
      t.setOwner(loginUser);
      if (t.getOwner() == null) {
        out.println("작업 등록을 취소하였습니다.");
        return;
      }
      taskService.add(t);

      out.println("작업을 등록했습니다.");
    } catch (Exception e) {
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);
      out.println(strWriter.toString());
    }
  }
}

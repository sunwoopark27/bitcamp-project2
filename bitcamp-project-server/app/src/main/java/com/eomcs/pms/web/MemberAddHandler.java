package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;

@WebServlet("/member/add")
@SuppressWarnings("serial")
public class MemberAddHandler extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/plain;charset=UTF-8");
    MemberService memberService = (MemberService) request.getServletContext().getAttribute("memberService");

    PrintWriter out = response.getWriter();

    out.println("[회원 등록]");

    Member m = new Member();
    m.setName(request.getParameter("name"));
    m.setEmail(request.getParameter("email"));
    m.setPassword(request.getParameter("password"));
    m.setPhoto(request.getParameter("photo"));
    m.setTel(request.getParameter("tel"));

    try {
      memberService.add(m);
    } catch (Exception e) {
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);
      out.println(strWriter.toString());
    }
    out.println("회원을 등록하였습니다.");

  }
}







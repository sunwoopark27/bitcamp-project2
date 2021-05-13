package com.eomcs.pms.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 다른 서블릿을 실행하는 중에 오류가 발생했을 때
// 포워딩 하는 서블릿이다.
// 이 서블릿의 주요 역할은 오류 내용을 출력하는 일이다.
// 1. HR 시스템 유지보수
//    - HR 시스템의 사용법을 익혀라.
//    - 기본적인 인사 관리 업무가 무엇인지 알아야 한다.
//    - 연습

@SuppressWarnings("serial")
@WebServlet("/error")
public class ErrorHandler extends HttpServlet {

  // 포워딩하는 측에서 클라이언트로 부터 GET 요청을 받은 상태라면
  // 포워딩 할 때 이 서블릿에 대해 GET으로 요청한다.
  // 포워딩 하는 측에서 클라이언트로 부터 POST 요청을 받은 상태라면
  // 포워딩 할 때 이 서블릿에 대해 POST로 요청할 것이다.
  // 결론!
  // - 따라서 이 서블릿은 GET 요청, POST 요청을 모두 처리할 수 있어야 한다.
  //
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
  }
}

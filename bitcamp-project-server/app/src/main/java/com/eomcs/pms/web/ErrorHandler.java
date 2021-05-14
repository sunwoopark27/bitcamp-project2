package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//다른 서블릿을 실행하는 중에 오류가 발생했을 때
//포워딩 하는 서블릿이다.
//이 서블릿의 주요 역할은 오류 내용을 출력하는 일이다.
//1. HR 시스템 유지보수
// - HR 시스템의 사용법을 익혀라.
// - 기본적인 인사 관리 업무가 무엇인지 알아야 한다.
// - 연습

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

    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();


    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<title>프로젝트 등록</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>요청 처리 중 오류 발생!</h1>");

    String message = (String) request.getAttribute("message");
    if (message != null) {
      out.printf("<p>%s</p>", message);
    }

    // web.xml에 예외처리를 설정한 경우,
    // 그 예외 객체는 서블릿 명세에 따라 정해진 이름으로 꺼내야한다.
    // 속성 이름과 의미:
    // - javax.servlet.error.status_code : 에러 상태 코드
    // - javax.servlet.error.exception_type : 예외 클래스
    // - javax.servlet.error.message : 오류 메세지 
    // - javax.servlet.error.request_uri : 예외가 발생한 URI
    // - javax_servlet.error.exception : 예외 객체 
    // - javax.servlet.error.servlet_name : 예외가 발생한 서블릿 이름

    Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
    if( e!= null) {
      // 예외 객체에 간단한 메세지가 있다면 출력한다.
      if (e.getMessage() != null) {
        out.printf("</p>%s</p>\n", e.getMessage());
      }
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);
      out.printf("<pre>%s</pre>\n", strWriter.toString());
    }

    out.println("</body>");
    out.println("</html>");
  }
}

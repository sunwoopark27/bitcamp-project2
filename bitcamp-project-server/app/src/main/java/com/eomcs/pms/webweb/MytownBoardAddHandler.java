package com.eomcs.pms.webweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.service.BoardService;

@SuppressWarnings("serial")
@WebServlet("/ourtown/add")
public class MytownBoardAddHandler extends HttpServlet{

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    BoardService boardService = (BoardService)request.getServletContext().getAttribute("boardService");

    MyTownBoard b = new MyTownBoard();

    request.setCharacterEncoding("UTF-8");

    b.setTitle(request.getParameter("title"));
    b.setContent(request.getParameter("content"));
    b.setWriter(request.getParameter(""));
    //    Member loginUser = (Member) httpRequest.getSession().getAttribute("loginUser");
    //    b.setWriter(loginUser);

    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<title>게시글 등록</title>");

    out.println("[게시글 등록]");

    try {
      boardService.add(b);

      out.println("<meta http-equiv='Refresh' content='1;url=list>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>게시글 등록</h1>");
      out.println("<p>게시글을 등록했습니다.<p>");
    } catch (Exception e) {
      // 상세 오류 내용을 StringWriter로 출력한다.
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace(printWriter);

      out.println("</head>");
      out.println("<body>");
      out.println("<h1>게시글 등록 오류</h1>");
      out.printf("<pre>%s</pre>\n", strWriter.toString());
      out.println("<a href='list'>목록</a></p>\n");
    }
    out.println("</body>");
    out.println("</html>");
  }

}

}

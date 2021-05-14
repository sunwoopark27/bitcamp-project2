package com.eomcs.pms.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.service.BoardService;


@SuppressWarnings("serial")
@WebServlet("/board/list")
public class BoardListHandler extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // 클라이언트가 /board/list 를 요청하면 톰캣 서버가 이 메서드를 호출한다. 

    BoardService boardService = (BoardService) request.getServletContext().getAttribute("boardService");

    try {
      // JSP 가 게시글 목록을 출력할 때 사용할 데이터를 준비한다. 
      List<Board> boards = boardService.list();

      // JSP가 사용할 수 있도록 ServletRequest 보관소에 저장한다.
      request.setAttribute("list", boards);

      // 목록 출력을 JSP에게 맡긴다.
      response.setContentType("text/html;charset=UTF-8");
      request.getRequestDispatcher("/jsp/board/list.jsp").include(request, response);

    } catch (Exception e) {
      throw new ServletException(e);
      //      // 예외가 발생하면 예외 정보를 ServletRequest 보관소에 담는다.
      //      request.setAttribute("exception",e);
      //
      //      // 예외 내용을 출력하기 위해 ErrorHandler에게 실행을 넘긴다.(포워딩)
      //      // - 이때 이 서블릿이 버퍼로 출력한 내용은 모두 버려질 것이다.
      //      RequestDispatcher 요청배달자 = request.getRequestDispatcher("/error");
      //      요청배달자.forward(request, response);
      //
      //      System.out.println("=======>오호라!");
      //      // 다른 서블릿으로 포워딩 한 이후, 그 서블릿의 실행을 마친 후 되돌아온다.
      //      // 되돌아 온 이후에 클라이언트로 출력하는 작업은 모두 무시된다.
      //      // 그러니 다음 문장을 실행할 이유가 없다.
      //      // 따라서 forwarding을 수행하고 리턴한 이후에 의미없이 명령을 실행하지 않도록
      //      // 이 메서드를 종료하게 만드는 것이 좋다.
      //      return;
    }
  }
}







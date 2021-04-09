package com.eomcs.pms.service.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.service.BoardService;

// 서비스 객체
// - 비즈니스 로직을 담고 있다.
// - 업무에 따라 트랜젝션을 제어하는 일을 한다.
// - 서비스 객체의 메서드는 가능한 업무관련 용어를 사용하여 메서드를 정의한다.
public class DefaultBoardService implements BoardService {
  SqlSession sqlSession;
  BoardDao boardDao;

  public DefaultBoardService(SqlSession sqlSession, BoardDao boardDao) {
    this.sqlSession = sqlSession;
    this.boardDao = boardDao;
  }

  // 게시글 등록 업무
  public int add(Board board) throws Exception {
    int count = boardDao.insert(board);
    sqlSession.commit();
    return count;
  }
  //게시글 목록 조회 업무
  public List<Board> list() throws Exception {
    return boardDao.findByKeyword(null);
  }
  //게시글 상세 조회 업무
  public Board get(int no) throws Exception {
    Board board = boardDao.findByNo(no);
    if(board != null) {
      boardDao.updateViewCount(no);
      sqlSession.commit();
    }
    return board;
  }

  //게시글 변경 업무

  public int update(Board board) throws Exception {
    int count = boardDao.update(board);
    sqlSession.commit();
    return count;
  }

  public int delete(int no) throws Exception {
    int count = boardDao.delete(no);
    sqlSession.commit();
    return count;
  }

  public List<Board> search(String keyword) throws Exception {
    return boardDao.findByKeyword(keyword);
  }

}

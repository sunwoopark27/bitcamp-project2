package com.eomcs.pms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.eomcs.pms.domain.Board;

@Service
public interface BoardService {

  int add(Board board) throws Exception;

  List<Board> list() throws Exception;

  Board get(int no) throws Exception;

  int update(Board board) throws Exception;

  int delete(int no) throws Exception;

  List<Board> search(String keyword) throws Exception;
}








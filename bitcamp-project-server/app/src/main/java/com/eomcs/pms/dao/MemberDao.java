package com.eomcs.pms.dao;

import java.util.Map;
import com.eomcs.pms.domain.Member;

public interface MemberDao {

  int insert(Member member) throws Exception;

  Member findAll() throws Exception;

  Member findByNo(int no) throws Exception;

  Member findByEmailPassword(Map<String,Object> params) throws Exception;

  int update(Member member) throws Exception;

  int delete(int no) throws Exception;

  Member findByName(String name) throws Exception;
}













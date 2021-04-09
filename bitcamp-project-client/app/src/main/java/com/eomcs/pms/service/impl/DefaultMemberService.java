package com.eomcs.pms.service.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;

// 서비스 객체
// - 비즈니스 로직을 담고 있다.
// - 업무에 따라 트랜젝션을 제어하는 일을 한다.
// - 서비스 객체의 메서드는 가능한 업무관련 용어를 사용하여 메서드를 정의한다.
public class DefaultMemberService implements MemberService {
  SqlSession sqlSession;
  MemberDao memberDao;

  public DefaultMemberService(SqlSession sqlSession, MemberDao memberDao) {
    this.sqlSession = sqlSession;
    this.memberDao = memberDao;
  }

  // 등록 업무
  public int add(Member member) throws Exception {
    int count = memberDao.insert(member);
    sqlSession.commit();
    return count;
  }
  // 목록 조회 업무
  public List<Member> list() throws Exception {
    return memberDao.findAll();
  }
  // 상세 조회 업무
  public Member get(int no) throws Exception {
    return memberDao.findByNo(no);
  }

  // 변경 업무

  public int update(Member member) throws Exception {
    int count = memberDao.update(member);
    sqlSession.commit();
    return count;
  }

  public int delete(int no) throws Exception {
    int count = memberDao.delete(no);
    sqlSession.commit();
    return count;
  }

  // 이름으로 찾기
  public Member search(String name) throws Exception {
    return memberDao.findByName(name);
  }

}

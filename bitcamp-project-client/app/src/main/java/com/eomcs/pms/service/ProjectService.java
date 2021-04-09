package com.eomcs.pms.service;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

// 서비스 객체
// - 비즈니스 로직을 담고 있다.
// - 업무에 따라 트랜젝션을 제어하는 일을 한다.
// - 서비스 객체의 메서드는 가능한 업무관련 용어를 사용하여 메서드를 정의한다.
public class ProjectService {
  SqlSession sqlSession;
  ProjectDao projectDao;
  TaskDao taskDao;

  public ProjectService(SqlSession sqlSession, ProjectDao projectDao, TaskDao taskDao) {
    this.sqlSession = sqlSession;
    this.projectDao = projectDao;
    this.taskDao = taskDao;
  }

  // 등록 업무
  public int add(Project project) throws Exception {
    try {
      // 1) 프로젝트 정보를 입력한다.
      int count = projectDao.insert(project);

      // 2) 멤버를 입력한다.
      projectDao.insertMembers(project.getNo(), project.getMembers());

      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }
  // 목록 조회 업무
  public List<Project> list() throws Exception {
    return projectDao.findByKeyword(null, null);
  }
  // 상세 조회 업무
  public Project get(int no) throws Exception {
    return projectDao.findByNo(no);
  }

  // 변경 업무

  public int update(Project project) throws Exception {
    try {
      int count = projectDao.update(project);
      deleteMembers(project.getNo());
      projectDao.insertMembers(project.getNo(), project.getMembers());

      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  public int delete(int no) throws Exception {
    try {

      taskDao.deleteByProjectNo(no);

      projectDao.deleteMembers(no);

      int count = projectDao.delete(no);
      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  // 찾기
  public List<Project> search(String title, String owner, String member) throws Exception {
    return projectDao.findByKeywords(title, owner, member);
  }

  public List<Project> search(String item, String keyword) throws Exception {
    return projectDao.findByKeyword(item, keyword);
  }


  public int deleteMembers(int projectNo) throws Exception{
    int count = projectDao.deleteMembers(projectNo);
    return count;
  }

  public int updateMembers(int projectNo, List<Member> members) throws Exception {
    try{
      projectDao.deleteMembers(projectNo);
      int count = projectDao.insertMembers(projectNo, members);
      sqlSession.commit();
      return count;
    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }
}

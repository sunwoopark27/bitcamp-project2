package com.eomcs.pms.service.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;
import com.eomcs.pms.service.TaskService;

// 서비스 객체
// - 비즈니스 로직을 담고 있다.
// - 업무에 따라 트랜젝션을 제어하는 일을 한다.
// - 서비스 객체의 메서드는 가능한 업무관련 용어를 사용하여 메서드를 정의한다.
public class DefaultTaskService implements TaskService {
  SqlSession sqlSession;
  TaskDao taskDao;

  public DefaultTaskService(SqlSession sqlSession, TaskDao taskDao) {
    this.sqlSession = sqlSession;
    this.taskDao = taskDao;
  }

  // 등록 업무
  public int add(Task task) throws Exception {
    int count = taskDao.insert(task);
    sqlSession.commit();
    return count;
  }
  // 목록 조회 업무
  public List<Task> list() throws Exception {
    return taskDao.findAll();
  }
  // 상세 조회 업무
  public Task get(int no) throws Exception {
    return taskDao.findByNo(no);
  }

  // 변경 업무

  public int update(Task task) throws Exception {
    int count = taskDao.update(task);
    sqlSession.commit();
    return count;
  }

  public int delete(int no) throws Exception {
    int count = taskDao.delete(no);
    sqlSession.commit();
    return count;
  }

  // 프로젝트 번호로 찾기
  public List<Task> listOfProject(int ProjectNo) throws Exception {
    return taskDao.findByProjectNo(ProjectNo);
  }



}

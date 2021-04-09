package com.eomcs.pms.service;

import java.util.List;
import com.eomcs.pms.domain.Task;

public interface TaskService {

  public List<Task> list() throws Exception;
  public Task get(int no) throws Exception;


  public int update(Task task) throws Exception;

  public int delete(int no) throws Exception;

  public List<Task> listOfProject(int ProjectNo) throws Exception;

}

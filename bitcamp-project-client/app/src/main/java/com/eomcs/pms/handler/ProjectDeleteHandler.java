package com.eomcs.pms.handler;

import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.util.Prompt;

public class ProjectDeleteHandler implements Command {
  ProjectDao projectDao;

  public ProjectDeleteHandler(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }
  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 삭제]");

    int no = Prompt.inputInt("번호? ");

    String input = Prompt.inputString("정말 삭제하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트 삭제를 취소하였습니다.");
      return;
    }

    projectDao.delete(no);
    System.out.println("프로젝트를 삭제하였습니다.");
  }
}









package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectDetailSearchHandler implements Command {

  ProjectDao projectDao;

  public ProjectDetailSearchHandler(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 상세 검색]");
    String title = Prompt.inputString("프로젝트명?(조건에서 제외: 빈 문자열) ");
    String owner = Prompt.inputString("관리자명?(조건에서 제외: 빈 문자열) ");
    String member = Prompt.inputString("팀원?(조건에서 제외: 빈 문자열) ");

    List<Project> list = projectDao.findByKeywords(title, owner, member);

    if (list.size() == 0) {
      System.out.println("검색어에 해당하는 게시글이 없습니다.");
      return;
    }

    System.out.println("번호, 프로젝트명, 시작일 ~ 종료일, 관리자, 팀원");

    for (Project p : list) {

      // 1) 프로젝트의 팀원 목록 가져오기
      StringBuilder strBuilder = new StringBuilder();
      List<Member> members = p.getMembers();
      for (Member m : members) {
        if (strBuilder.length() > 0) {
          strBuilder.append(",");
        }
        strBuilder.append(m.getName());
      }

      // 2) 프로젝트 정보를 출력
      System.out.printf("%d, %s, %s, %s, %s, [%s]\n", 
          p.getNo(), 
          p.getTitle(), 
          p.getStartDate(),
          p.getEndDate(),
          p.getOwner().getName(),
          strBuilder.toString());
    }
  }

}

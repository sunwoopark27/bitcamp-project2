package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDao {

  Connection con;

  public ProjectDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
  }

  public int insert(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_project(title,content,sdt,edt,owner) values(?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS)) {

      // 수동 커밋으로 설정한다.
      // - pms_project 테이블과 pms_member_project 테이블에 모두 성공적으로 데이터를 저장했을 때 
      //   작업을 완료한다.
      con.setAutoCommit(false); // 의미 => 트랜잭션 시작

      // 1) 프로젝트를 추가한다.
      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      int count = stmt.executeUpdate();

      // 프로젝트 데이터의 PK 값 알아내기
      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        project.setNo(keyRs.getInt(1));
      }

      // 2) 프로젝트에 팀원들을 추가한다.
      for (Member member : project.getMembers()) {
        insertMembers(member.getNo(), project.getNo());
      }

      // 프로젝트 정보 뿐만 아니라 팀원 정보도 정상적으로 입력되었다면,
      // 실제 테이블에 데이터를 적용한다.
      con.commit(); // 의미 : 트랜잭션 종료

      return count;

    } finally {
      // 트랜잭션 종료 후 auto commit 을 원래 상태로 설정한다.
      con.setAutoCommit(true);
    }
  }

  public int insertMembers(int projectNo, int memberNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_member_project(member_no,project_no) values(?,?)")) {

      stmt.setInt(1, projectNo);
      stmt.setInt(2, memberNo);
      return stmt.executeUpdate();
    }
  }

  public List<Project> findAll() throws Exception {
    List<Project> list = new ArrayList<>();
    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select" 
                + "    p.no,"
                + "    p.title,"
                + "    p.sdt,"
                + "    p.edt,"
                + "    m.no as owner_no,"
                + "    m.name as owner_name"
                + "  from pms_project p"
                + "    inner join pms_member m on p.owner=m.no"
                + "  order by title asc");
        ResultSet rs = stmt.executeQuery()) {

      while(rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        p.setOwner(owner);

        p.setMembers(findMembersList(p.getNo()));

        list.add(p);
      }
    }
    return list;
  }

  public List<Member> findMembersList(int projectNo) throws Exception {
    List<Member> members = new ArrayList<>();
    try (PreparedStatement stmt = con.prepareStatement(
        "select" 
            + "    m.no,"
            + "    m.name"
            + " from pms_member_project mp"
            + "     inner join pms_member m on mp.member_no=m.no"
            + " where "
            + "     mp.project_no=?")) {

      stmt.setInt(1, projectNo);

      try (ResultSet memberRs = stmt.executeQuery()) {
        while (memberRs.next()) {
          Member member = new Member();
          member.setNo(memberRs.getInt("no"));
          member.setName(memberRs.getString("name"));
          members.add(member);
        }
      }
    }
    return members;
  }

  public Project findByNo(int no) throws Exception {
    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select"
                + "    p.no,"
                + "    p.title,"
                + "    p.content,"
                + "    p.sdt,"
                + "    p.edt,"
                + "    m.no as owner_no,"
                + "    m.name as owner_name"
                + "  from pms_project p"
                + "    inner join pms_member m on p.owner=m.no"
                + " where p.no=?")){

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          return null; //해당 번호의 프로젝트 없다.
        }

        Project p = new Project();
        p.setNo(no);
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));
        Member member = new Member();
        member.setNo(rs.getInt("owner_no"));
        member.setName(rs.getString("owner_name"));
        p.setOwner(member);
        List<Member> members = findMembersList(p.getNo());
        p.setMembers(members);

        return p;
      }
    }
  }
  public int update(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_project set"
            + " title=?,"
            + " content=?,"
            + " sdt=?,"
            + " edt=?,"
            + " owner=?"
            + " where no=?")) { 

      con.setAutoCommit(false);

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      stmt.setInt(6, project.getNo());
      int count = stmt.executeUpdate();

      // 기존 프로젝트의 모든 멤버를 삭제한다.
      deleteMembers(project.getNo());

      // 프로젝트 멤버를 추가한다.
      for (Member member : project.getMembers()) {
        insertMembers(project.getNo(), member.getNo());
      }

      con.commit();

      return count;

    } finally {
      // 트랜잭션 종료 후 auto commit 을 원래 상태로 설정한다.
      con.setAutoCommit(true);
    }
  }

  public int deleteMembers(int projectNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement( 
        "delete from pms_member_project where project_no=?")) { 
      stmt.setInt(1, projectNo);
      return stmt.executeUpdate();
    }
  }

  public int delete(int no) throws Exception  {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_project where no=?")) {

      con.setAutoCommit(false);

      deleteMembers(no);

      stmt.setInt(1, no);
      int count = stmt.executeUpdate();
      con.commit();

      return count;

    } finally {

      con.setAutoCommit(true);
    }
  }
}
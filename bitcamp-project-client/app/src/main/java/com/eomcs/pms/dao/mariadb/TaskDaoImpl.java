package com.eomcs.pms.dao.mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Task;

public class TaskDaoImpl implements TaskDao {

  Connection con;

  public TaskDaoImpl()  throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");{
        }
  }

  @Override
  public int insert(Task task) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_task(content,deadline,owner,status,project_no) values(?,?,?,?,?)")) {

      stmt.setString(1, task.getContent());
      stmt.setDate(2, task.getDeadline());
      stmt.setInt(3, task.getOwner().getNo());
      stmt.setInt(4, task.getStatus());
      stmt.setInt(5, task.getProjectNo());
      return stmt.executeUpdate();
    }
  }

  @Override
  public List<Task> findAll() throws Exception  {
    return findAll(0);
  }

  @Override
  public List<Task> findByProjectNo(int projectNo) throws Exception {
    return findAll(projectNo);
  }

  public List<Task> findAll(int projectNo) throws Exception {

    List<Task> tasks = new ArrayList<>();
    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + "   t.no,"
            + "   t.content,"
            + "   t.deadline,"
            + "   t.status,"
            + "   m.no as owner_no,"
            + "   m.name as owner_name,"
            + "   p.no as project_no,"
            + "   p.title as project_title"
            + " from pms_task t "
            + "   inner join pms_member m on t.owner=m.no"
            + "   inner join pms_project p on t.project_no=p.no"
            + " where "
            + "   t.project_no=? or 0=?"
            + " order by p.no desc, t.content asc")) {

      stmt.setInt(1, projectNo);
      stmt.setInt(2, projectNo);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Task t = new Task();
        t.setNo(rs.getInt("no"));
        t.setContent(rs.getString("content")); 
        t.setDeadline(rs.getDate("deadline"));

        Member m = new Member();
        m.setNo(rs.getInt("owner_no"));
        m.setName(rs.getString("owner_name"));
        t.setOwner(m);

        t.setStatus(rs.getInt("status"));

        t.setProjectNo(rs.getInt("project_no"));
        t.setProjectTitle(rs.getString("project_title"));

        tasks.add(t);
      }

      return tasks;
    }
  }

  @Override
  public Task findByNo(int taskNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + "   t.no,"
            + "   t.content,"
            + "   t.deadline,"
            + "   t.status,"
            + "   m.no as owner_no,"
            + "   m.name as owner_name,"
            + "   p.no as project_no,"
            + "   p.title as project_title"
            + " from pms_task t "
            + "   inner join pms_member m on t.owner=m.no"
            + "   inner join pms_project p on t.project_no=p.no"
            + " where t.no=?")) {

      stmt.setInt(1, taskNo);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        Task t = new Task();

        t.setNo(rs.getInt("no"));
        t.setContent(rs.getString("content"));
        t.setDeadline(rs.getDate("deadline"));
        t.setStatus(rs.getInt("status"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        t.setOwner(owner);

        t.setProjectNo(rs.getInt("project_no"));
        t.setProjectTitle(rs.getString("project_title"));

        return t;

      }
    }
  }
  @Override
  public int update(Task task) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_task set content=?,deadline=?,owner=?,status=?,project_no=? where no=?")) {

      // 3) DBMS에게 게시글 변경을 요청한다.
      stmt.setString(1, task.getContent());
      stmt.setDate(2, task.getDeadline());
      stmt.setInt(3, task.getOwner().getNo());
      stmt.setInt(4, task.getStatus());
      stmt.setInt(5, task.getProjectNo());
      stmt.setInt(6, task.getNo());
      return stmt.executeUpdate();

    }
  }

  @Override
  public int delete(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_task where no=?")) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();
    }
  }
}


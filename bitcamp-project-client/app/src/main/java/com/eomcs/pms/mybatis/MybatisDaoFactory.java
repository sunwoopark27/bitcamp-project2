package com.eomcs.pms.mybatis;

import java.lang.reflect.Proxy;
import org.apache.ibatis.session.SqlSession;

public class MybatisDaoFactory {

  DaoWorker daoWorker;

  public MybatisDaoFactory(SqlSession sqlSession) {
    daoWorker = new DaoWorker(sqlSession);
  }

  @SuppressWarnings("unchecked")
  public <T> T createDao(Class<T> daoInterface) {

    return (T) Proxy.newProxyInstance(
        this.getClass().getClassLoader(),
        new Class<?>[] {daoInterface},
        daoWorker
        );
  }

}

package com.eomcs.pms.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

public class DaoWorker implements InvocationHandler {

  SqlSession sqlSession;

  public DaoWorker(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public Object invoke(Object daoproxy, Method method, Object[] args) throws Throwable {


    //    잘 호출하는지 알아보기 위
    //    System.out.printf(" %s.%s() 호출됨!\n",
    //        daoproxy.getClass().getInterfaces()[0].getName(),
    //        method.getName());

    //    Parameter[] params = method.getParameters();
    //    for (Parameter p : params) {
    //      System.out.printf("  %s %s\n", p.getType().getName(),p.getName());
    //    }
    //
    //    System.out.printf("  ==> %s\n", method.getReturnType().getName());

    // 1) SqlSession의 메서드를 호출할 때 넘겨 줄 SQL ID
    // => SQL ID는 인터페이스의 fully-qualified name과 같다고 가정하자.
    String sqlId = daoproxy.getClass().getInterfaces()[0].getName() + "." + method.getName();

    // argument로 넘어온 파라미터  
    Object param = (args == null) ? null : args[0];

    // 3) 메서드의 리턴 타입에 따라 적절한 SqlSession의 메서드를 호출한다. 
    if (method.getReturnType() == int.class ||
        method.getReturnType() == void.class) {
      return sqlSession.insert(sqlId,param);

    } else if (method.getReturnType() == List.class) {
      return sqlSession.selectList(sqlId,param);

    } else {
      return sqlSession.selectOne(sqlId,param);
    }

  }

}

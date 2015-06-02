package pifii.com.log.test;

import java.io.IOException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import pifii.com.log.entity.Tiger;
import pifii.com.log.util.MybatisUtil;

public class Mybatis {
	public static void main(String[] args) throws IOException {
        SqlSessionFactory sessionFactory = MybatisUtil.getSessionFactory();
        SqlSession session = sessionFactory.openSession();
        try {
            Tiger tiger = session.selectOne("selectTiger",1);
            System.out.println(tiger.getAge());
        } finally{
            session.close();
        }
    }
}

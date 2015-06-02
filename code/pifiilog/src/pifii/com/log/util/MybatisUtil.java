package pifii.com.log.util;


import java.io.IOException;
import java.io.InputStream;


import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
    
    private static SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSessionFactory() throws IOException {
        if(sqlSessionFactory==null){
            String resource = "mybatis-config.xml";
            InputStream inputStream = org.apache.ibatis.io.Resources.getResourceAsStream(resource);
            return new SqlSessionFactoryBuilder().build(inputStream);
        }else{
            return sqlSessionFactory;
        }
    }
}
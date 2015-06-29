package pifii.com.log.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.cfg.DefaultNamingStrategy;
public class ChangeNamingStrategy extends DefaultNamingStrategy {
	public static final ChangeNamingStrategy cns=new ChangeNamingStrategy();
	static SimpleDateFormat sdf=new SimpleDateFormat("_yyyy_MM_dd");
	/**
	 * 自定义Hibernate表名命名规则
	 */
	@Override
	public String classToTableName(String className) {

		return "bpbaselogtbl"+sdf.format(new Date());
	}
	
}

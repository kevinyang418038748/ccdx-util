package cn.edu.ccdx.util.db;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public abstract class DataSourceFactory {
	
	protected String jndi;
	
	private final Logger log = Logger.getLogger(getClass());
	
	private DataSource ds;
	
	private boolean create;
	
	protected DataSource createDataSource(){
		try{
			//从JNDI中获取数据源
			if(log.isInfoEnabled())
				log.info("jndi datasource");
			InitialContext initContext = new InitialContext();
			if(jndi.startsWith("java:/comp/env/"))
				return (DataSource) initContext.lookup(jndi);
			else
				return (DataSource) initContext.lookup("java:/comp/env/" + jndi);
		}catch(Exception ex){
			//如果JNDI获取失败，则构建dbcp数据库连接池
			//需要注意的是lib文件夹中得有tomcat-dbcp.jar文件
			log.warn(ex.getMessage(), ex);
			return null;
		}
	}
	
	protected DataSource getJndiDataSource() {
		if (create)
			return ds;
		synchronized (this) {
			if (create)
				return ds;
			ds = createDataSource();
			create = true;
			return ds;
		}
	}

}

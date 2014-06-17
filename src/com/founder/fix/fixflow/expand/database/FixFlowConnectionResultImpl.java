package com.founder.fix.fixflow.expand.database;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.founder.fix.fixflow.core.exception.FixFlowDbException;
import com.founder.fix.fixflow.core.impl.db.FixConnectionResult;
import com.ych.util.SpringBeanUtil;

/**
 * 此类复写了fixflow的默认数据源管理类，将数据源交给spring来管理
 * 由于此类编译到classes下，会优先于fixflow-core包中的类加载
 * 
 * 第二种方法是新建一套connectionManagerment，在fixflowconfig.xml文件中配置，那样有点复杂，时间关系，不写了。
 * @author ych
 *
 */
public class FixFlowConnectionResultImpl implements FixConnectionResult {

	protected String dbId;

	protected Connection connection;

	public FixFlowConnectionResultImpl(String dbId) {
		this.dbId = dbId;
	}

	public FixFlowConnectionResultImpl(String dbId, Connection connection) {
		this.dbId = dbId;
		this.connection = connection;
	}

	public FixFlowConnectionResultImpl(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		try {
			if (this.connection == null) {
				//通过spring取数据源，此处可以自己扩展，用spring注入测方式封装，保证灵活
				//此方法获取的数据源可以保证在spring的事务上下文中
				this.connection = DataSourceUtils.getConnection((DataSource)SpringBeanUtil.getBean("dataSource"));
				return this.connection;
			} else {
				return this.connection;

			}
		} catch (Exception e) {
			throw new FixFlowDbException(e.getMessage(), e);
		}
	}

	public void colseConnection() {
		//交给spring管理
	}

	public void commitConnection() {
		//交给spring管理
	}

	public void rollBackConnection() {
		//交给spring管理
	}

	public void openConnection() {

	}

	public String getDataBaseId() {
		return this.dbId;
	}

}

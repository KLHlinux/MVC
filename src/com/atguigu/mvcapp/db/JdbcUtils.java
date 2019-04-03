package com.atguigu.mvcapp.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ����JdbcUtils��ȡc3p0���ӳ����� JDBC�����Ĺ�����
 */
public class JdbcUtils {

	/**
	 * �ͷ�Connection����
	 * @param connection
	 */
	public static void releaseConnection(Connection connection) {
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static DataSource dataSource = null;

	static {
		// ����Դֻ�ܱ�����һ��
		dataSource = new ComboPooledDataSource("mvcapp");
	}

	/**
	 * ��������Դ��һ��Connection����
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getconnection() throws SQLException {
		return dataSource.getConnection();
	}
}

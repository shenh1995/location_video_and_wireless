package com.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.config.JdbcConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionDB {
  
	public static Connection getConnection() throws SQLException, PropertyVetoException {
		
		ComboPooledDataSource comb = new ComboPooledDataSource();
		comb.setDriverClass(JdbcConfig.jdbc_driver);
		comb.setJdbcUrl(JdbcConfig.jdbc_url);
		comb.setUser(JdbcConfig.jdbc_user);
		comb.setPassword(JdbcConfig.jdbc_password);
		
		Connection connection = comb.getConnection();
		
		 return connection;
	}
	
	public static void main(String [] args) throws SQLException, PropertyVetoException {
		Connection connection = getConnection();
		String sql = "select * from Wifi";
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}
	}
}

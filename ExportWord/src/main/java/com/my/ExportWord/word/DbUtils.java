package com.my.ExportWord.word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbUtils {

	private static final String url_temp = "jdbc:mysql://${dbIp}:3306/${db}?useUnicode=true&characterEncoding=utf-8";

	public static Connection getConn_test() throws ClassNotFoundException, SQLException {
		return getConn("192.168.X.XXX", "tysx_s", "root", "password");
	}

	//-----------------------------------------
	public static Connection getConn(String dbIp, String db, String username, String password) throws ClassNotFoundException, SQLException {
		
		System.out.println("Connection " + dbIp);
		
		Class.forName("com.mysql.jdbc.Driver");
		
		String url = url_temp.replace("${dbIp}", dbIp).replace("${db}", db);
		Connection conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(false);
		
		return conn;
	}

	//-----------------------------------------
	public static List<TableDto> searchTableColumn(String tableName){
		
		Connection conn = null;
		try {
			conn = DbUtils.getConn_test();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
//		String sql = "desc " + table;
		String sql = "show full fields from " + tableName;
		
		PreparedStatement pstat		= null;
		ResultSet rs				= null;
		TableDto tableBo			= new TableDto();
		List<TableDto> tableBoList	= new ArrayList<>();
		try {
			
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while(rs.next()){
				
				String field	= rs.getString("Field");
				String type		= rs.getString("Type");
				String key		= rs.getString("Key");
				String comment	= rs.getString("Comment");
				
				int index = type.indexOf("(");
				if(index > 0){
					type = type.substring(0, index);
				}
				
				TableDto tb = CloneUtils.clone(tableBo);
				tb.setField(field);
				tb.setType(type);
				tb.setKey(key);
				tb.setComment(comment);
				tableBoList.add(tb);
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			close(rs);
			close(pstat);
			close(conn);
		}
		
		return tableBoList;
	}

	public static void close(AutoCloseable c){
		if(c == null){
			return;
		}
		
		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.oreilly.servlet.*" %>
<%@ page import = "com.oreilly.servlet.multipart.*" %>
<%@ page import = "java.sql.*" %>
<%
	request.setCharacterEncoding("utf-8");

	String idx = request.getParameter("idea_idx");
	int idea_idx = Integer.parseInt(idx);
	
	System.out.println("idx :" + idea_idx);
	
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "6415";
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "delete from idea_table "
			+"where idea_idx = ?";
			
	PreparedStatement pstmt = db.prepareStatement(sql);
	pstmt.setInt(1, idea_idx);
	
	pstmt.execute();
	db.close();
%>
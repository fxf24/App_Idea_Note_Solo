<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import = "com.oreilly.servlet.*" %>
<%@ page import = "com.oreilly.servlet.multipart.*" %>
<%@ page import = "java.sql.*" %>
<%
	request.setCharacterEncoding("utf-8");

	String idx = request.getParameter("idea_idx");
	int idea_idx = Integer.parseInt(idx);
	
	String idea_title = request.getParameter("idea_title");
	String idea_desc = request.getParameter("idea_desc");
	String idea_link = request.getParameter("idea_link");
	String idea_date = request.getParameter("idea_date");
	
	System.out.println("idx : " + idea_idx);
	System.out.println("title :" + idea_title);
	System.out.println("memo :" + idea_desc);
	System.out.println("link :" + idea_link);
	System.out.println("date :" + idea_date);
	
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "6415";
	
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "update idea_table "
			+"set idea_title = ?, idea_desc = ?, idea_link = ?, idea_date = ?"
			+ "where idea_idx = ?";
			
	PreparedStatement pstmt = db.prepareStatement(sql);
	pstmt.setString(1, idea_title);
	pstmt.setString(2, idea_desc);
	pstmt.setString(3, idea_link);
	pstmt.setString(4, idea_date);
	pstmt.setInt(5, idea_idx);
	
	pstmt.execute();
	db.close();
%>
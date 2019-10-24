<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"  %>
<%@ page import="org.json.simple.*" %>
<%
	//JDBC 드라이버 로딩
	Class.forName("oracle.jdbc.OracleDriver");
	String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	String id = "scott";
	String pw = "6415";
	
	// 데이터 베이스 접속
	Connection db = DriverManager.getConnection(url, id, pw);
	
	String sql = "select *"
			+ "from idea_table order by idea_idx desc";
	
	PreparedStatement pstmt = db.prepareStatement(sql);
	ResultSet rs = pstmt.executeQuery();
	
	JSONArray root = new JSONArray();
	
	while(rs.next()){
		int idea_idx = rs.getInt("idea_idx");
		String idea_title = rs.getString("idea_title");
		String idea_desc = rs.getString("idea_desc");
		String idea_link = rs.getString("idea_link");
		String idea_date = rs.getString("idea_date");
		
		JSONObject obj = new JSONObject();
		obj.put("idea_idx", idea_idx);
		obj.put("idea_title", idea_title);
		obj.put("idea_desc", idea_desc);
		obj.put("idea_link", idea_link);
		obj.put("idea_date", idea_date);
		
		root.add(obj);
	}
	db.close();
%>
<%= root.toJSONString() %>

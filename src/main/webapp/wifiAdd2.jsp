<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="db.WifiFunction2"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
h1 {
	text-align: center;
}

#goHome {
	text-align: center;
}
</style>
</head>
<body>
	<%
	WifiFunction2 wififunction = new WifiFunction2();
	wififunction.SaveToDB();
	int totalNum = wififunction.TotalCnt();
	%>

	<h1><%=totalNum%>개의 WIFI 정보를 정상적으로 저장하였습니다.
	</h1>


	<div id="goHome">
		<a href="mainPage2.jsp">홈 으로 가기</a>
	</div>

</body>
</html>
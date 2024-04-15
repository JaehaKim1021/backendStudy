<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="db.LocationInfo2"%>
<%@page import="db.LocationService"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<style>
table {
	width: 100%;
}

table, th, td {
	border: 1px solid #D8D8D8;
	border-collapse: collapse;
}

#ttable tr:nth-child(even) {
	background-color: #FAFAFA;
}

#ttable tr:first-child {
	background-color: #04B45F;
	color: #FFFFFF;
	text-align: center;
	border: 1px solid white;
}

#delBtn {
	text-align: center;
}
</style>
</head>
<body>

	<%
	LocationService locationservice = new LocationService();
	List<LocationInfo2> locationlist = locationservice.list();
	%>

	<h1>위치 히스토리 목록</h1>

	<a href="mainPage2.jsp">홈</a> |
	<a href="locationHistory2.jsp">위치 히스토리 목록</a> |
	<a href="wifiAdd2.jsp">Open API 와이파이 정보 가져오기</a>
	<br>
	<br>

	<table id="ttable">
		<thead>
			<tr>
				<td>ID</td>
				<td>X좌표</td>
				<td>Y좌표</td>
				<td>조회일자</td>
				<td>비고</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<%
				for (LocationInfo2 locationinfo : locationlist) {
				%>
			
			<tr>
					<td>
						<%=locationinfo.getID()%>
					</td>
					<td><%=locationinfo.getLat()%></td>
					<td><%=locationinfo.getLnt()%></td>
					<td><%=locationinfo.getDateTime()%></td>
					<td id="delBtn" ><input type="submit" class="delBtn" value="삭제" /> <script>
						
					</script></td>

			</tr>

			<%
			}
			%>
			</tr>
		</tbody>
	</table>

	<script>
		// 버튼 클릭시 Row 값 가져오기
		$(".delBtn").click(function() {

			var str = ""
			var tdArr = new Array(); // 배열 선언
			var delBtn = $(this);

			var tr = delBtn.parent().parent();
			var td = tr.children();

			console.log("클릭한 Row의 모든 데이터 : " + tr.text());

			var idno = td.eq(0).text();
			console.log(idno);
			
			window.location.href="locationHistory2.jsp?idVal="+idno;
		});
	</script>
	
	<%
		if(request.getParameter("idVal")!=null){
			locationservice.withdraw(Integer.parseInt(request.getParameter("idVal")));
			
			%>
			<script>
			window.location.replace("locationHistory2.jsp");
			</script>
			<%
		}
		%>

	

</body>
</html>
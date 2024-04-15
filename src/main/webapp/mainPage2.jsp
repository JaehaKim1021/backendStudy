<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="db.Service2"%>
<%@page import="db.LocationInfo2"%>
<%@page import="db.LocationService" %>
<%@page import="db.WifiFunction2"%>
<%@page import="db.Wifi2"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<style>
table {
	width: 100%;
}

table, th, td {
  border: 1px solid #D8D8D8;
  border-collapse: collapse;
}
#ttable tr:nth-child(even){background-color: #FAFAFA;}
#ttable tr:first-child {background-color: #04B45F; color:#FFFFFF; text-align:center; border: 1px solid white;}
#noLoc {background-color:#FFFFFF;color:#000000; text-align:center;}
</style>
</head>
<body>
	<h1>와이파이 정보 구하기</h1>
	
	<%
	Service2 service2 = new Service2();
	LocationInfo2 locationinfo2 = new LocationInfo2();
	%>

	<a href="mainPage2.jsp">홈</a> |
	<a href="locationHistory2.jsp">위치 히스토리 목록</a> |
	<a href="wifiAdd2.jsp">Open API 와이파이 정보 가져오기</a>
	<br>
	<br>
	<form action='mainPage2.jsp' method="get">
		LAT : <input id='lat' name="lat" value="0.0" /> 
		LNT : <input id='lnt' name="lnt" value="0.0" />
		<button type="button" onclick="findLocation()">내 위치 가져오기</button>
		
		<script>
			const loc1 = document.getElementById("lat");
			const loc2 = document.getElementById("lnt");
			
			<%
			if (request.getParameter("lat") != null) {
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lnt = Double.parseDouble(request.getParameter("lnt"));
				String slat = String.valueOf(lat);
				System.out.println("자바코드");
				System.out.println(slat);
				System.out.println(lnt);
				locationinfo2.setLat(lat);
				locationinfo2.setLnt(lnt);
				service2.addHistoryData(locationinfo2);
				
				
			%>
			
				loc1.value = <%=lat%>;
				loc2.value = <%=lnt%>;
				
			

			<%
			}
			%>
			
			function findLocation() {
				if (navigator.geolocation) {
					navigator.geolocation.getCurrentPosition(showYourLocation);
				} else {
					loc.innerHTML = "이 문장은 사용자의 웹 브라우저가 Geolocation API를 지원하지 않을 때 나타납니다!";
				}
			}

			function showYourLocation(position) {
				loc1.value = position.coords.latitude;
				loc2.value = position.coords.longitude;
			}
		</script>
		<input type='submit' value='근처 WIFI 정보 보기'>
	</form>
	
	<br>
	
	<table id="ttable">
		<thead>
			<tr>
				<td>거리(Km)</td>
				<td>관리번호</td>
				<td>자치구</td>
				<td>와이파이명</td>
				<td>도로명주소</td>
				<td>상세주소</td>
				<td>설치위치(층)</td>
				<td>설치유형</td>
				<td>설치기관</td>
				<td>서비스구분</td>
				<td>망종류</td>
				<td>설치년도</td>
				<td>실내외구분</td>
				<td>WIFI접속환경</td>
				<td>X좌표</td>
				<td>Y좌표</td>
				<td>작업일자</td>
			</tr>
		</thead>
			
		
	<%
		if (request.getParameter("lat") != null) {
			//표만들기
		
			System.out.println("요청인자있음");
			System.out.println(request.getParameter("lat"));
			System.out.println(request.getParameter("lnt"));
			
			%>	
			
		
		<tbody>
			<tr>
			
			
				<%
				
				LocationService locationservice = new LocationService();
				double lat = Double.parseDouble(request.getParameter("lat"));
				double lnt = Double.parseDouble(request.getParameter("lnt"));
				List<Wifi2> wifiList = locationservice.listW(lat,lnt);
				
				for (Wifi2 wifi : wifiList) {
				%>
				<tr>
				<td><%=wifi.getDISTANCE()%></td>
				<td><%=wifi.getMGR_NO()%></td>
				<td><%=wifi.getWRDOFC()%></td>
				<td><%=wifi.getMAIN_NM()%></td>
				<td><%=wifi.getADRES1()%></td>
				<td><%=wifi.getADRES2()%></td>
				<td><%=wifi.getINSTL_FLOOR()%></td>
				<td><%=wifi.getINSTL_TY()%></td>
				<td><%=wifi.getINSTL_MBY()%></td>
				<td><%=wifi.getSVC_SE()%></td>
				<td><%=wifi.getCMCWR()%></td>
				<td><%=wifi.getCNSTC_YEAR()%></td>
				<td><%=wifi.getINOUT_DOOR()%></td>
				<td><%=wifi.getREMARS3()%></td>
				<td><%=wifi.getLAT()%></td>
				<td><%=wifi.getLNT()%></td>
				<td><%=wifi.getWORK_DTTM()%></td>
				</tr>

			<%
			}
			%>
			</tr>
		</tbody>
		
		
		<%		
		}
		else{
			
	%>
	<tr>
	<td id="noLoc" colspan='17'>위치 정보를 입력한 후에 조회해 주세요.</td>
	</tr>
	<%
		}
	%>
	
	
	</table>
	
	






</body>
</html>
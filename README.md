[ 코드 파일 설명 ] 
==================

[java 파일]
-----------
* Wifi2.java
: wifi 정보 데이터 저장을 위한 class

* LocationInfo2.java
: 사용자가 조회하는 위치 정보 데이터 저장을 위한 class

* WifiFunction2.java
  * GetDataTest 
: 공공API 사용하여 데이터 가져오는 기능
  * TotalCnt 
: API가 제공하는 데이터의 총 개수 파악하는 기능
  * SaveToDB 
: API로 가져온 모든 데이터를 DB에 저장하는 기능

* Service2.java
  * addHistoryData 
: 조회한 위도, 경도, 조회일자 정보를 DB에 저장하는 기능

* LocationService.java
  * list : DB에서 조회 데이터 (위도, 경도, 조회일자) 가져오는 기능
  * listW : DB에서 WIFI 데이터(*) 가져오는 기능
  * withdraw : 선택한 id에 해당하는 조회데이터 삭제


(*)listW
→ 현재 조회한 위치(위도, 경도)값과 wifi데이터의 LAT, LNT 항목을 시용하여 그 값이 DISTANCE COLUMN으로 하여 계산 -> DISTANCE 항목을 오름차순으로 하여 20개로 제한하여 데이터를 가져옴


[jsp 파일]
----------

* mainPage2.jsp

  * Open API 정보 가져오기 버튼 클릭 -> wifiAdd2.jsp로 이동
  * 홈 버튼 클릭 -> 현재 jsp 파일로 이동
  * 위치 히스토리 목록 버튼 클릭 -> LocationHistory2.jsp로 이동

  * 내 위치 가져오기 버튼 클릭 -> findLoction()함수 실행
-> navigation.geolocation을 통해 현재 위도와 경도 가져와서
  * LAT, LNT input 태그 값으로 넣어줌

  * 근처 WIFI 정보 보기 버튼 클릭 -> input 태그에 있는 LAT, LNT. 값을 현재 페이지의 파라미터로 전송

  * 현재 페이지에서 전송된 파라미터가 있는 경우
-> LocationService.java의 listW를 호출하여 근처 wifi 보여줌

* wifiAdd2.jsp
  * WifiFunction2.java의 SaveToDB를 호출
  * WifiFunction2.java의 TotalCnt를 호출하여 전체 데이터 수를 함께 출력

* locationHistory2.jsp
  * Open API 정보 가져오기 버튼 클릭 -> wifiAdd2.jsp로 이동
  * 홈 버튼 클릭 -> mainPage2.jsp 파일로 이동
  * 위치 히스토리 목록 버튼 클릭 -> 현재 jsp 파일로 이동

  * LocationService.java의 list()를 호출하여 받은 결과를 리스트로 받아와서 table에 노출

  * 삭제 버튼 클릭 -> 선택된 해당 데이터의 ID값을 현재 페이지의 파라미터로 전송
  * 전송된 파라미터가 있는 경우 -> LocationService.java의 withdraw를 사용하여 해당 ID를 가지고 있는 데이터 삭제 후 파라미터 없이 현재 페이지 재접속


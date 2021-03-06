package hdlnc.dev2.sangtae.cnm.global;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//SMApplicationServer는 C&M 모바일서비스에 필요한 메뉴 화면구성정보를 http프로토콜을 이용한 openAPI를 통해 제공한다.
public class XML_Address_Define {

	public static final int XML_CONNETION_TIME_OUT					= 30000;		// 파서 타임 아웃 시간 설정 값 (30초)
	//----------------XML URL---------------------------
	//Server Address
	//private static final String Server_URL			= "http://kgt.app.chtvn.com/";														// 기본 서버 주소
	//private static final String Server_URL					= "http://121.88.252.2/SMApplicationServer/";											// 임시 개발 서버 주소
	private static final String Server_URL					= "http://58.143.243.91/SMApplicationServer/";
	private static final String OldServer_URL					= "http://58.143.243.91/mobile/";

	private static String getURLEncoder(String aStr){
		try {
			return URLEncoder.encode(aStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	//----------------XML ADDRESS---------------------------
	public static class Authenticate{
		//Authenticate
		private static final String AuthenticateClient		= Server_URL + "AuthenticateClient.asp?terminalId=%s";								// 클라이언트를 인증하고 TerminalKey를 얻는다. (시스템관리자이용)
		private static final String GetAppVersionInfo		= Server_URL + "GetAppVersionInfo.asp";												// App 버전관리정보를 제공합니다.
		private static final String GetAppContentVersion	= Server_URL + "GetAppContentVersion.asp";											// SMApplication이 제공하는 모든 컨텐츠에대한 버전관리를 담당합니다.
		private static final String ClientSetTopBoxRegist	= Server_URL + "ClientSetTopBoxRegist.asp?deviceId=%s&authKey=%s";					// 사용할 셋톱을 등록합니다..(VOD서버와의 인터페이스 확인)
		private static final String CheckRegistUser			= Server_URL + "CheckRegistUser.asp?deviceId=%s";									// 사용회원으로 등록된 사용자인지 여부를 확인한다.
		private static final String AuthenticateAdult		= Server_URL + "AuthenticateAdult.asp?UserInfo=%s@%s";									// 성인사용자 여부를 인증합니다. (성인인증서버 연동)

		//Authenticate
		public static final String AUTHE_SETTOP_HD				= "HD"; 						//셋톱의 종류 ( HD/ SD/ PVR /SMART)
		public static final String AUTHE_SETTOP_SD				= "SD"; 						
		public static final String AUTHE_SETTOP_PVR				= "PVR"; 						
		public static final String AUTHE_SETTOP_SMART			= "SMART"; 						
		//----------------------------------------------
		public static String getAuthenticateclient(String aTerminalId) {
			return String.format(AuthenticateClient, aTerminalId);
		}
		public static String getGetappversioninfo() {
			return GetAppVersionInfo;
		}
		public static String getGetappcontentversion() {
			return GetAppContentVersion;
		}
		public static String getClientsettopboxregist(String aDeviceId, String aAuthKey) {
			return String.format(ClientSetTopBoxRegist, aDeviceId, aAuthKey);
		}
		public static String getCheckregistuser(String aDeviceId) {
			return String.format(CheckRegistUser, aDeviceId);
		}
		public static String getAuthenticateadult(String aUserNumber, String aUserName) {
			return String.format(AuthenticateAdult, aUserNumber, getURLEncoder(aUserName));
		}


	}

	public static class Channel{
		//Channel
		private static final String GetChannelGenre			= Server_URL + "GetChannelGenre.asp";												// 채널서비스 장르정보를 보여준다.
		private static final String GetChannelProduct		= Server_URL + "GetChannelProduct.asp?areaCode=%s";												// 채널서비스 상품정보를 보여준다
		private static final String GetChannelArea			= Server_URL + "GetChannelArea.asp";												// 채널서비스 지역정보를 보여준다
		private static final String GetChannelList			= Server_URL + "GetChannellist.asp?areaCode=%s&productCode=%s&genreCode=%s&mode=%s";	// 지역, 상품, 장르별 채널 리스트를 리턴한다.
		private static final String GetChannelSchedule		= Server_URL + "GetChannelSchedule.asp?channelId=%s&DateIndex=%s";					// 한채널의 일자별 편성정보를 제공한다
		private static final String GetChannelMyList		= Server_URL + "구현예정";															// 구현예정
		private static final String SetMyChannel			= Server_URL + "SetMyChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";					// 사용자가자신의 채널을 저장할 때 사용됩니다.
		private static final String SetMyHiddenChannel		= Server_URL + "SetMyHiddenChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";			// 사용자가자신의 히든 채널을 저장할 때 사용됩니다.
		private static final String SetMySchedule			= Server_URL + "SetMySchedule.asp?deviceId=%s&scheduleId=%s&mode=%s";				// 사용자가 스케줄 예약알람을 설정할 때 사용됩니다.
		//////////////////////////////////////////////////////////
		public static class CHANNEL_LSIT{
			public static final String PAY				= "PAY"; 						// //채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
			public static final String HD				= "HD"; 						// //채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
			public static final String ALL				= ""; 						// //채널검색모드 (PAY : 유료채널, HD: HD채널) 값없음 : 전체채널
		}
		//////////////////////////////////////////////////////////
		public static class LIMITED_AREA_CODE{
			public static final String KANGNAM			= "not supported"; 						// 강남인 경우
			public static final String UlSAN			= "not supported"; 						// 울산인 경우
		}
		//---------------------------------------------
		public static String getGetchannelgenre() {
			return GetChannelGenre;
		}
		public static String getGetchannelproduct(String aAreaCode) {
			return String.format(GetChannelProduct, aAreaCode);
		}
		public static String getGetchannelarea() {
			return GetChannelArea;
		}
		public static String getGetchannellist(String aAreaCode, String aProductCode, String aGenreCode, String aMode) {
			return String.format(GetChannelList, aAreaCode, aProductCode, aGenreCode, aMode);
		}
		public static String getGetchannelschedule(String aChannelId, String aDateIndexa) {
			return String.format(GetChannelSchedule, aChannelId, aDateIndexa);
		}
		public static String getGetchannelmylist() {
			return GetChannelMyList;
		}
		public static String getSetmychannel(String aDeviceId, String aChannelId, String aMode) {
			return String.format(SetMyChannel, aDeviceId, aChannelId, aMode);
		}
		public static String getSetmyhiddenchannel(String aDeviceId, String aChannelId, String aMode) {
			return String.format(SetMyHiddenChannel, aDeviceId, aChannelId, aMode);
		}
		public static String getSetmyschedule(String aDeviceId, String aScheduleId, String aMode) {
			return String.format(SetMySchedule, aDeviceId, aScheduleId, aMode);
		}
	}

	public static class Record{
		//Record
		private static final String SetRecord				= Server_URL + "SetRecord.asp?deviceId=%s&ChannelId=%s";							// 채널의 프로그램을 즉시녹화 요청합니다.	
		private static final String SetRecordSeries			= Server_URL + "SetRecord.asp?deviceId=%s&ChannelId=%s&StartTime=%s";							// 채널의 프로그램을 즉시녹화 요청합니다.	
		private static final String SetRecordStop			= Server_URL + "SetRecordStop.asp?deviceId=%s&ChannelId=%s";						// 채널의 프로그램 녹화를 즉시 중지합니다.
		private static final String SetRecordReserve		= Server_URL + "SetRecordReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s";		// 채널의 프로그램을 예약녹화 합니다. (단일 예약녹화)
		private static final String SetRecordSeriesReserve	= Server_URL + "SetRecordSeriesReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s";	// 채널의 프로그램을 예약녹화 합니다. (시리즈 예약녹화)
		private static final String SetRecordCancelReserve	= Server_URL + "SetRecordCancelReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s&ReserveCancel=%s";	// 채널의 프로그램에 대한 예약녹화를 취소합니다.
		private static final String SetRecordNameReplace	= Server_URL + "SetRecordNameReplace.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s&RecordReplaceName=%s"; // 녹화된 컨텐츠의 이름을 변경합니다.
		private static final String GetRecordReserveList	= Server_URL + "getrecordReservelist.asp?deviceId=%s";								// 예약녹화물 리스트를 제공합니다. (예약녹화삭제는 7.3.5 참조)
		private static final String GetRecordList			= Server_URL + "getrecordlist.asp?deviceId=%s";										// 녹화목록 리스트를 제공합니다.
		private static final String SetRecordDele			= Server_URL + "SetRecordDele.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&deleteType=%s";	// 녹화목록을 삭제합니다 (협의후 요청파라미터가 변경될수 있음)
		private static final String SetRecordSeriesDele		= Server_URL + "SetRecordSeriesDele.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s";	// 시리즈 녹화목록을 삭제합니다 (협의후 요청파라미터가 변경될수 있음)
		private static final String SetRecordProtection		= Server_URL + "SetRecordProtection.asp?RecordId=%s&deviceId=%s&ChannelId=%s&protection=%s";	// 녹화물을 보호합니다.
		//////////////////////////////////////////////////////////
		public static final String RECORD_DEL_ONE			= "2"; 						// 0: 단편삭제(시리즈단편), 1:전체삭제
		public static final String RECORD_DEL_ALL			= "1"; 						// 0: 단편삭제(시리즈단편), 1:전체삭제 
		public static final String RECORD_PROTECTION_YES	= "Y"; 						// Y:보호, N:해제
		public static final String RECORD_PROTECTION_NO		= "N"; 						// Y:보호, N:해제
		//---------------------------------------------
		public static String getSetrecord(String aDeviceId, String aChannelId) {
			return String.format(SetRecord, aDeviceId, aChannelId);
		}
		public static String getSetrecordseries(String aDeviceId, String aChannelId, String aStartTime) {
			return String.format(SetRecordSeries, aDeviceId, aChannelId, getURLEncoder(aStartTime) );
		}
		public static String getSetrecordstop(String aDeviceId, String aChannelId) {
			return String.format(SetRecordStop, aDeviceId, aChannelId);
		}
		public static String getSetrecordreserve(String aDeviceId, String aChannelId, String aStartTime) {
			
			return String.format(SetRecordReserve, aDeviceId, aChannelId, getURLEncoder(aStartTime));
		}
		public static String getSetrecordseriesreserve(String aDeviceId, String aChannelId, String aStartTime, String aSeriesId) {
			return String.format(SetRecordSeriesReserve, aDeviceId, aChannelId, getURLEncoder(aStartTime), aSeriesId);
		}
		public static String getSetrecordcancelreserve(String aDeviceId, String aChannelId, String aStartTime, String aSeriesId, String aReserveCancel) {
			return String.format(SetRecordCancelReserve, aDeviceId, aChannelId, getURLEncoder(aStartTime), aSeriesId, aReserveCancel);
		}
		public static String getSetrecordnamereplace(String aRecordID, String aDeviceId, String aChannelId, String aStartTime, String aSeriesId, String aRecordReplaceName) {
			return String.format(SetRecordNameReplace, aRecordID, aDeviceId, aChannelId, getURLEncoder(aStartTime), aSeriesId, getURLEncoder(aRecordReplaceName));
		}
		public static String getGetrecordreservelist(String aDeviceId) {
			return String.format(GetRecordReserveList, aDeviceId);
		}
		public static String getGetrecordlist(String aDeviceId) {
			return String.format(GetRecordList, aDeviceId);
		}
		public static String getSetrecorddele(String aRecordId, String aDeviceId, String aChannelId, String aStartTime, String aDeleteType) {
			return String.format(SetRecordDele, aRecordId, aDeviceId, aChannelId, getURLEncoder(aStartTime), aDeleteType);
		}
		public static String getSetrecordseriesdele(String aRecordId, String aDeviceId, String aChannelId) {
			return String.format(SetRecordSeriesDele, aRecordId, aDeviceId, aChannelId);
		}
		public static String getSetrecordprotection(String aRecordId, String aDeviceId, String aChannelId, String aProtection) {
			return String.format(SetRecordProtection, aRecordId, aDeviceId, aChannelId, aProtection);
		}
	}

	public static class RemoteController{
		//RemoteController
		private static final String SetRemoteChannelControl	= Server_URL + "SetRemoteChannelControl.asp?deviceId=%s&ChannelId=%s";				// 셋탑의 채널번호를 변경합니다.
		private static final String SetRemoteVolumeControl	= Server_URL + "SetRemoteVolumeControl.asp?deviceId=%s&volume=%s";					// 셋탑의 볼륨을 조정합니다.
		private static final String SetRemotePowerControl	= Server_URL + "SetRemotePowerControl.asp?deviceId=%s&power=%s";					// 셋탑의 파워를 켯다 끔니다.
		private static final String SetRemoteMessage		= Server_URL + "SetRemoteMessage.asp?deviceId=%s&msg=%s";							// 셋탑에 문자 메시지를 날립니다.
		private static final String GetSetTopStatus			= Server_URL + "GetSetTopStatus.asp?deviceId=%s";									// 셋탑 STATE API 
		/*result - 0성공, 1실패
			state - 0 av
 			1 vod
 			2 독립형
 			3 pip동시 실행중
 			4 stand by
 			5 개인미디어실행중*/
		///////////////////////////////////////////////////////////////////
		public static final String REMOTE_CON_VOLUM_UP			= "UP"; 						// 볼륨올림
		public static final String REMOTE_CON_VOLUM_DOWN		= "DN"; 						// 볼륨내림
		public static final String REMOTE_CON_POW_ON			= "ON"; 						// 켜짐
		public static final String REMOTE_CON_POW_OFF			= "OFF"; 						// 꺼짐
		//---------------------------------------------------------
		public static String getSetremotechannelcontrol(String aDeviceId, String aChannelId) {
			return String.format(SetRemoteChannelControl, aDeviceId, aChannelId);
		}
		public static String getSetremotevolumecontrol(String aDeviceId, String aVolume) {
			return String.format(SetRemoteVolumeControl, aDeviceId, aVolume);
		}
		public static String getSetremotepowercontrol(String aDeviceId, String aPOWER) {
			return String.format(SetRemotePowerControl, aDeviceId, aPOWER);
		}
		public static String getSetremotemessage(String aDeviceId, String aMSG) {
			return String.format(SetRemoteMessage, aDeviceId, getURLEncoder(aMSG));
		}
		public static String getGetsettopstatus(String aDeviceId) {
			return String.format(GetSetTopStatus, aDeviceId);
		}

	}

	public static class Vod{
		//Sponsor
		private static final String GetVodGenre				= Server_URL + "GetVodGenre.asp?genreId=%s";													// VOD장르 정보를 제공합니다
		private static final String GetVodGenreInfo			= Server_URL + "GetVodGenreInfo.asp?genreId=%s";									// VOD장르 상세 정보를 제공합니다. ( Genre_More가 NO일경우 장르?의 VOD정보제공)
		private static final String GetVodMovie				= Server_URL + "GetVodMovie.asp";													// VOD 최신영화 리스트를 제공합니다.
		private static final String GetVodTv				= Server_URL + "GetVodTv.asp";														// TV다시보기 리스트를 제공합니다.
		private static final String GetVodTag				= Server_URL + "GetVodTag.asp?vod_tag=%s";											// VOD 중 예고편내의 검색을 통한 tag정보를 제공합니다.
		private static final String GetVodTrailer			= Server_URL + "GetVodTrailer.asp";													// VOD중 예고편 리스트 정보를 리턴합니다.
		// VOD - 찜 관련
		// 찜목록
		// http://58.143.243.91/SMApplicationServer/GetVodMyList.asp?deviceId=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String GetVodMyList			= Server_URL + "GetVodMyList.asp?deviceId=%s";										// 사용자 디바이스에서 MyVod에 담은 리스트를 제공합니다.
		private static final String SetMyVod				= Server_URL + "SetMyVod.asp?deviceId=%s&assetId=%s&genreId=%s&mode=%s";			// 사용자가 MyVod로 저장할 때 사용됩니다. (현재 서비스중이므로 테스트시 주의할 것) ( Vod 서버와 연동을 통하여 인터페이스가 구현됨)
		// 1차 인터페이스
		// 찜하기
		// http://58.143.243.91/mobile/MyVod_insert.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String InsertMyVod				= OldServer_URL + "MyVod_insert.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";			// 찜 관련은 예전 인터페이스 사용함.
		// 찜해제
		// http://58.143.243.91/mobile/MyVod_DELETE.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String DeleteMyVod				= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";			// 찜 관련은 예전 인터페이스 사용함.
		// 찜목록 초기화
		// http://58.143.243.91/Mobile/myvod_delete.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String DeleteAllMyVod				= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s";			// 찜 관련은 예전 인터페이스 사용함.		
		
		// VOD 상세정보
		private static final String SetVodSetTopDisplayInfo	= Server_URL + "SetVodSetTopDisplayInfo.asp?deviceId=%s&assetId=%s";				// 셋탑에 VOD상세 정보를 Display 합니다. (STB서버와 연동을 통하여 인터페이스 구현됨)
		private static final String Notification			= Server_URL + "Notification.asp?userId=%s&assetId=%s&type=%s";					// 셋탑박스에서 MyVOD를 삭제할경우 사용됩니다. (VOD서버와 연동을 통하여 인터페이스 구현됨)
		///////////////////////////////////////////////////////////////////
		public static final String NOTIFICATION_WISHITEM_TYPE	= "wishItemRemoved"; 						// userRemoved : device의 모든asset 삭제, deviceId 삭제
		public static final String NOTIFICATION_USER_TYPE		= "userRemoved"; 
		public static final String VOD_MY_MODE_INPUT						= "0"; 						// 0:입력, 1:특정컨텐츠삭제,2:해당유저의 모든커텐츠삭젝 3: 유저 및 모든데이터삭제
		public static final String VOD_MY_MODE_SPECIFIC_CONTENT_DELETE		= "1"; 						
		public static final String VOD_MY_MODE_USER_ALL_CONTENT_DELETE		= "2"; 						
		public static final String VOD_MY_MODE_USER_AND_CONTENT_ALL_DELETE	= "3";
		//----------------------------------------------------------
		public static String getGetvodgenre(String aGenreId) {
			return String.format(GetVodGenre, aGenreId);
		}
		public static String getGetvodgenreinfo(String aGenreId) {
			return String.format(GetVodGenreInfo, aGenreId);
		}
		public static String getGetvodmovie() {
			return GetVodMovie;
		}
		public static String getGetvodtv() {
			return GetVodTv;
		}
		public static String getGetvodtag(String aVod_Tag) {
			return String.format(GetVodTag, getURLEncoder(aVod_Tag));
		}
		public static String getGetvodtrailer() {
			return GetVodTrailer;
		}
		public static String getGetvodmylist(String aDeviceId) {
			return String.format(GetVodMyList, aDeviceId);
		}
		public static String getSetmyvod(String aDeviceId, String aAssetId, String aGenreId, String aMode) {
			return String.format(SetMyVod, aDeviceId, aAssetId, aGenreId, aMode);
		}
		// 찜하기
		public static String getInsertmyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(InsertMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// 찜해제
		public static String getDeletemyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(DeleteMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// 찜목록 초기화
		public static String getDeleteallmyvod(String aDeviceId) {
			return String.format(DeleteAllMyVod, aDeviceId);
		}
		public static String getSetvodsettopdisplayinfo(String aDeviceId, String aAssetId) {
			return String.format(SetVodSetTopDisplayInfo, aDeviceId, aAssetId);
		}
		public static String getNotification(String aUserId, String aAssetId, String aType) {
			return String.format(Notification, aUserId, aAssetId, aType);
		}
	}

	public static class Search{
		//Search
		private static final String SearchChannel			= Server_URL + "SearchChannel.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// 채널정보를 검색합니다
		private static final String SearchProgram			= Server_URL + "SearchProgram.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// 프로그램 정보를 검색합니다.
		private static final String SearchVod				= Server_URL + "SearchVod.asp?search_string=%s&pageSize=%s&pageIndex=%s&sortType=%s";	// VOD정보를 검색합니다.
		/////////////////////////////
		/*		정렬방식 (기본 : ChannelName Asc)
		최신등록순 : ChannelNoAsc
		최신수정순 : ChannelNoDesc
		이름순 : ChannelNameAsc
		이름순 역순 : ChannelNameDesc			*/
		public static final String ChannelNoAsc 	=	"ChannelNoAsc";
		public static final String ChannelNoDesc 	=	"ChannelNoAsc";
		public static final String ChannelNameAsc 	=	"ChannelNoAsc";
		public static final String ChannelNameDesc 	=	"ChannelNoAsc";

		//-------------------------------------------------------------
		public static String getSearchchannel(String aSerch_Str, String aPageSize, String aPageIndex, String aAreaCode, String aProductCode) {
			return String.format(SearchChannel, getURLEncoder(aSerch_Str), aPageSize, aPageIndex, aAreaCode, aProductCode);
		}
		public static String getSearchprogram(String aSerch_Str, String aPageSize, String aPageIndex, String aAreaCode, String aProductCode) {
			return String.format(SearchProgram, getURLEncoder(aSerch_Str), aPageSize, aPageIndex, aAreaCode, aProductCode);
		}
		public static String getSearchvod(String aSerch_Str, String aPageSize, String aPageIndex, String aSortType) {
			return String.format(SearchVod, getURLEncoder(aSerch_Str), aPageSize, aPageIndex, aSortType);
		}
	}


	public static class Service{
		//Service
		private static final String GetGuideCategory		= Server_URL + "GetGuideCategory.asp?CategoryID=%s";											// 서비스 가이드 리스트를 제공합니다.
		private static final String GetServiceGuideList		= Server_URL + "GetServiceguideList.asp";											// 서비스 가이드 리스트를 제공합니다.
		private static final String GetServiceGuideInfo		= Server_URL + "GetServiceguideInfo.asp?CategoryID=%s";								// 서비스 가이드 상세정보를 제공합니다.
		private static final String GetServiceGuideImage	= Server_URL + "GetServiceGuideImage.asp";											// 셋탑 미등록 및 상품 안내 정보를 제공합니다.
		private static final String GetServiceJoinNList		= Server_URL + "GetServiceJoyNlist.asp";											// 조이엔 리스트를 제공합니다.
		private static final String GetServiceJoinNInfo		= Server_URL + "GetServiceJoyNInfo.asp?joyNId=%s";									// 하나의 조이엔에 대한 상세정보를 제공합니다.
		private static final String GetServiceNoticeInfo	= Server_URL + "GetServiceNoticeInfo.asp?areaCode=%s&productCode=%s";									// 하나의 조이엔에 대한 상세정보를 제공합니다.
		//	//	//	//	//	//	//	//	//	//
		public static final String REMOCON_UNREGISTERED 	=	"1";	
		public static final String SD_SETTOP_REGISTERED 	=	"2";
		public static final String PVR_UNREGISTERED 		=	"3";
		public static final String SD_HD_REGISTERED			=	"4";
		//---------------------------------------------------------------
		
		public static String getGetserviceguidelist() {
			return GetServiceGuideList;
		}
		public static String getGetguidecategory(String aCategoryID) {
			if (aCategoryID == null)
				aCategoryID = "";
			return String.format(GetGuideCategory, aCategoryID);
		}
		public static String getGetserviceguideinfo(String aCategoryID) {
			return String.format(GetServiceGuideInfo, aCategoryID) ;
		}
		
		public static String getGetserviceguideimage() {
			return GetServiceGuideImage;
		}
		public static String getGetservicejoinnlist() {
			return GetServiceJoinNList;
		}
		public static String getGetservicejoinninfo(String aJoyNId) {
			return String.format(GetServiceJoinNInfo, aJoyNId);
		}
		public static String getGetserviceNoticeinfo(String aAreaCode, String aProductCode) {
			return String.format(GetServiceNoticeInfo, aAreaCode, aProductCode);
		}
	}

	public static class ErrorCode{
		//8.1 공통
		public static final String ERROR_100	= "100";		//성공(Success)
		public static final String ERROR_200	= "200";		//알수없는 에러(General Error)
		public static final String ERROR_201	= "201";		//지원하지 않는 프로토콜(Unsupported Protocol)
		public static final String ERROR_202	= "202";		//인증실패(Authentication Failure)
		public static final String ERROR_203	= "203";		//지원하지 않는 프로파일(Unsupported Profile)
		public static final String ERROR_204	= "204";		//잘못된 파라미터값(Invalid Parameter)
		public static final String ERROR_205	= "205";		//해당아이템 없음(Not Found)
		public static final String ERROR_206	= "206";		//내부서버에러(Internal Server Error)
		public static final String ERROR_207	= "207";		//내부프로세싱 에러(Internal Processing Error)
		public static final String ERROR_211	= "211";		//일반 DB에러(DB General Error)
		public static final String ERROR_221	= "221";		//이미처리되었음(Already Done)
		public static final String ERROR_223	= "223";		//이미추가된 항목(Duplicated Insertion)
		public static final String ERROR_231	= "231";		//인증코드발급실패(AuthCode Issue Failure)
		public static final String ERROR_232	= "232";		//만료된인증코드(AuthCode Expired)

		public class RecordResponCode{
			//8.2 녹화요청
			public static final String	ERROR_001	= "001";			//Mac불일치(Invalid MacAddress)
			public static final String	ERROR_002	= "002";			//중복 녹화 예약요청 (Duplicated Recording Reserve Request)
			public static final String	ERROR_003	= "003";			//디스크 용량부족(Disk Capacity Not Enough)
			public static final String	ERROR_004	= "004";			//튜너를 모두사용하고 있어 녹화/튜닝불가 (Authentication Failure)
			public static final String	ERROR_005	= "005";			//녹화 불가 채널(Unsupported Recording Channel)
			public static final String	ERROR_006	= "006";			//이미 녹화 예약됨(Already Recording Reserve Done)
			public static final String	ERROR_007	= "007";			//프로그램 정보가 없음(Program Not Found)
			public static final String	ERROR_008	= "008";			//재생중인 녹화물 삭제불가( Recording Delete Error)
			public static final String	ERROR_009	= "009";			//채널 없음(Channel Not Found)
			public static final String	ERROR_010	= "010";			//PIP사용중(Using PIP Service)
			public static final String	ERROR_011	= "011";			//다른채널 녹화중(Already Other Channel Recording)
			public static final String	ERROR_012	= "012";			//시청제한 채널(LimitedView Channel)
			public static final String	ERROR_013	= "013";			//제한 채널(Limited Channel)
			public static final String	ERROR_014	= "014";			//대기 모드(Hold Mode)
			public static final String	ERROR_015	= "015";			//이미 녹화중 (Already Recording)
			public static final String	ERROR_016	= "016";			//삭제 오류 (Delete Processing Error)
			public static final String	ERROR_017	= "017";			//이름 변경 오류 (Name Replace Error)
			public static final String	ERROR_018	= "018";			//VOD상세 화면 띄우기 오류 (VOD DetailView Error)
			public static final String	ERROR_019	= "019";			//개인미디어 재생중 (Private Media Playing)
			public static final String	ERROR_020	= "020";			//독립형(데이터 서비스) 실행중 (Already Playing DataService )
			public static final String	ERROR_021	= "021";			//VOD재생중 (VOD Playing)
			public static final String	ERROR_028	= "028";			//전원 꺼짐 STB Status Power Off
		}

		//8.3. 녹화/ 튜닝불가 상세코드
		public class RecordRejectCode{
			public static final String	ERROR_005_1	= "005_1";			//PIP가 실행중이고 공통 전문의 시청채널과 다른 채널을 즉시녹화할 경우							|즉시 녹화불가
			public static final String	ERROR_005_2	= "005_2";			//VOD나 데이터 방송(JOY&LIFE)시청중 PIP가 실행중일경우									|즉시 녹화불가
			public static final String	ERROR_005_3	= "005_3";			//VOD나 데이터 방송(JOY&LIFE)을 시청중이고 다른채널이 녹화중일경우 또 다른 채널을 녹화 요청시		|즉시 녹화불가
			public static final String	ERROR_005_4	= "005_4";			//각기 다른 두채널이 녹화중이고 이때 또 다른채널을 녹화요청 할 경우							|즉시 녹화불가
			public static final String	ERROR_003	= "003";			//디스크 용량이 부족할 경우															|즉시 녹화불가
			public static final String	ERROR_002	= "002";			//중복된 녹화예약일 경우																|녹화 예약불가
			public static final String	ERROR_020	= "020";			//JOY&LIFE, VOD, 미디어 앨범 실행 중일 경우											|채널 튜닝불가
		}
	}

}

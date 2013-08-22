package hdlnc.dev2.sangtae.cnm.global;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//SMApplicationServer�� C&M ����ϼ��񽺿� �ʿ��� �޴� ȭ�鱸�������� http���������� �̿��� openAPI�� ���� �����Ѵ�.
public class XML_Address_Define {

	public static final int XML_CONNETION_TIME_OUT					= 30000;		// �ļ� Ÿ�� �ƿ� �ð� ���� �� (30��)
	//----------------XML URL---------------------------
	//Server Address
	//private static final String Server_URL			= "http://kgt.app.chtvn.com/";														// �⺻ ���� �ּ�
	//private static final String Server_URL					= "http://121.88.252.2/SMApplicationServer/";											// �ӽ� ���� ���� �ּ�
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
		private static final String AuthenticateClient		= Server_URL + "AuthenticateClient.asp?terminalId=%s";								// Ŭ���̾�Ʈ�� �����ϰ� TerminalKey�� ��´�. (�ý��۰������̿�)
		private static final String GetAppVersionInfo		= Server_URL + "GetAppVersionInfo.asp";												// App �������������� �����մϴ�.
		private static final String GetAppContentVersion	= Server_URL + "GetAppContentVersion.asp";											// SMApplication�� �����ϴ� ��� ������������ ���������� ����մϴ�.
		private static final String ClientSetTopBoxRegist	= Server_URL + "ClientSetTopBoxRegist.asp?deviceId=%s&authKey=%s";					// ����� ������ ����մϴ�..(VOD�������� �������̽� Ȯ��)
		private static final String CheckRegistUser			= Server_URL + "CheckRegistUser.asp?deviceId=%s";									// ���ȸ������ ��ϵ� ��������� ���θ� Ȯ���Ѵ�.
		private static final String AuthenticateAdult		= Server_URL + "AuthenticateAdult.asp?UserInfo=%s@%s";									// ���λ���� ���θ� �����մϴ�. (������������ ����)

		//Authenticate
		public static final String AUTHE_SETTOP_HD				= "HD"; 						//������ ���� ( HD/ SD/ PVR /SMART)
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
		private static final String GetChannelGenre			= Server_URL + "GetChannelGenre.asp";												// ä�μ��� �帣������ �����ش�.
		private static final String GetChannelProduct		= Server_URL + "GetChannelProduct.asp?areaCode=%s";												// ä�μ��� ��ǰ������ �����ش�
		private static final String GetChannelArea			= Server_URL + "GetChannelArea.asp";												// ä�μ��� ���������� �����ش�
		private static final String GetChannelList			= Server_URL + "GetChannellist.asp?areaCode=%s&productCode=%s&genreCode=%s&mode=%s";	// ����, ��ǰ, �帣�� ä�� ����Ʈ�� �����Ѵ�.
		private static final String GetChannelSchedule		= Server_URL + "GetChannelSchedule.asp?channelId=%s&DateIndex=%s";					// ��ä���� ���ں� �������� �����Ѵ�
		private static final String GetChannelMyList		= Server_URL + "��������";															// ��������
		private static final String SetMyChannel			= Server_URL + "SetMyChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";					// ����ڰ��ڽ��� ä���� ������ �� ���˴ϴ�.
		private static final String SetMyHiddenChannel		= Server_URL + "SetMyHiddenChannel.asp?deviceId=%s&ChannelId=%s&mode=%s";			// ����ڰ��ڽ��� ���� ä���� ������ �� ���˴ϴ�.
		private static final String SetMySchedule			= Server_URL + "SetMySchedule.asp?deviceId=%s&scheduleId=%s&mode=%s";				// ����ڰ� ������ ����˶��� ������ �� ���˴ϴ�.
		//////////////////////////////////////////////////////////
		public static class CHANNEL_LSIT{
			public static final String PAY				= "PAY"; 						// //ä�ΰ˻���� (PAY : ����ä��, HD: HDä��) ������ : ��üä��
			public static final String HD				= "HD"; 						// //ä�ΰ˻���� (PAY : ����ä��, HD: HDä��) ������ : ��üä��
			public static final String ALL				= ""; 						// //ä�ΰ˻���� (PAY : ����ä��, HD: HDä��) ������ : ��üä��
		}
		//////////////////////////////////////////////////////////
		public static class LIMITED_AREA_CODE{
			public static final String KANGNAM			= "not supported"; 						// ������ ���
			public static final String UlSAN			= "not supported"; 						// ����� ���
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
		private static final String SetRecord				= Server_URL + "SetRecord.asp?deviceId=%s&ChannelId=%s";							// ä���� ���α׷��� ��ó�ȭ ��û�մϴ�.	
		private static final String SetRecordSeries			= Server_URL + "SetRecord.asp?deviceId=%s&ChannelId=%s&StartTime=%s";							// ä���� ���α׷��� ��ó�ȭ ��û�մϴ�.	
		private static final String SetRecordStop			= Server_URL + "SetRecordStop.asp?deviceId=%s&ChannelId=%s";						// ä���� ���α׷� ��ȭ�� ��� �����մϴ�.
		private static final String SetRecordReserve		= Server_URL + "SetRecordReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s";		// ä���� ���α׷��� �����ȭ �մϴ�. (���� �����ȭ)
		private static final String SetRecordSeriesReserve	= Server_URL + "SetRecordSeriesReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s";	// ä���� ���α׷��� �����ȭ �մϴ�. (�ø��� �����ȭ)
		private static final String SetRecordCancelReserve	= Server_URL + "SetRecordCancelReserve.asp?deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s&ReserveCancel=%s";	// ä���� ���α׷��� ���� �����ȭ�� ����մϴ�.
		private static final String SetRecordNameReplace	= Server_URL + "SetRecordNameReplace.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s&RecordReplaceName=%s"; // ��ȭ�� �������� �̸��� �����մϴ�.
		private static final String GetRecordReserveList	= Server_URL + "getrecordReservelist.asp?deviceId=%s";								// �����ȭ�� ����Ʈ�� �����մϴ�. (�����ȭ������ 7.3.5 ����)
		private static final String GetRecordList			= Server_URL + "getrecordlist.asp?deviceId=%s";										// ��ȭ��� ����Ʈ�� �����մϴ�.
		private static final String SetRecordDele			= Server_URL + "SetRecordDele.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&deleteType=%s";	// ��ȭ����� �����մϴ� (������ ��û�Ķ���Ͱ� ����ɼ� ����)
		private static final String SetRecordSeriesDele		= Server_URL + "SetRecordSeriesDele.asp?RecordId=%s&deviceId=%s&ChannelId=%s&StartTime=%s&seriesId=%s";	// �ø��� ��ȭ����� �����մϴ� (������ ��û�Ķ���Ͱ� ����ɼ� ����)
		private static final String SetRecordProtection		= Server_URL + "SetRecordProtection.asp?RecordId=%s&deviceId=%s&ChannelId=%s&protection=%s";	// ��ȭ���� ��ȣ�մϴ�.
		//////////////////////////////////////////////////////////
		public static final String RECORD_DEL_ONE			= "2"; 						// 0: �������(�ø������), 1:��ü����
		public static final String RECORD_DEL_ALL			= "1"; 						// 0: �������(�ø������), 1:��ü���� 
		public static final String RECORD_PROTECTION_YES	= "Y"; 						// Y:��ȣ, N:����
		public static final String RECORD_PROTECTION_NO		= "N"; 						// Y:��ȣ, N:����
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
		private static final String SetRemoteChannelControl	= Server_URL + "SetRemoteChannelControl.asp?deviceId=%s&ChannelId=%s";				// ��ž�� ä�ι�ȣ�� �����մϴ�.
		private static final String SetRemoteVolumeControl	= Server_URL + "SetRemoteVolumeControl.asp?deviceId=%s&volume=%s";					// ��ž�� ������ �����մϴ�.
		private static final String SetRemotePowerControl	= Server_URL + "SetRemotePowerControl.asp?deviceId=%s&power=%s";					// ��ž�� �Ŀ��� �ִ� ���ϴ�.
		private static final String SetRemoteMessage		= Server_URL + "SetRemoteMessage.asp?deviceId=%s&msg=%s";							// ��ž�� ���� �޽����� �����ϴ�.
		private static final String GetSetTopStatus			= Server_URL + "GetSetTopStatus.asp?deviceId=%s";									// ��ž STATE API 
		/*result - 0����, 1����
			state - 0 av
 			1 vod
 			2 ������
 			3 pip���� ������
 			4 stand by
 			5 ���ι̵�������*/
		///////////////////////////////////////////////////////////////////
		public static final String REMOTE_CON_VOLUM_UP			= "UP"; 						// �����ø�
		public static final String REMOTE_CON_VOLUM_DOWN		= "DN"; 						// ��������
		public static final String REMOTE_CON_POW_ON			= "ON"; 						// ����
		public static final String REMOTE_CON_POW_OFF			= "OFF"; 						// ����
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
		private static final String GetVodGenre				= Server_URL + "GetVodGenre.asp?genreId=%s";													// VOD�帣 ������ �����մϴ�
		private static final String GetVodGenreInfo			= Server_URL + "GetVodGenreInfo.asp?genreId=%s";									// VOD�帣 �� ������ �����մϴ�. ( Genre_More�� NO�ϰ�� �帣?�� VOD��������)
		private static final String GetVodMovie				= Server_URL + "GetVodMovie.asp";													// VOD �ֽſ�ȭ ����Ʈ�� �����մϴ�.
		private static final String GetVodTv				= Server_URL + "GetVodTv.asp";														// TV�ٽú��� ����Ʈ�� �����մϴ�.
		private static final String GetVodTag				= Server_URL + "GetVodTag.asp?vod_tag=%s";											// VOD �� �������� �˻��� ���� tag������ �����մϴ�.
		private static final String GetVodTrailer			= Server_URL + "GetVodTrailer.asp";													// VOD�� ������ ����Ʈ ������ �����մϴ�.
		// VOD - �� ����
		// ����
		// http://58.143.243.91/SMApplicationServer/GetVodMyList.asp?deviceId=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String GetVodMyList			= Server_URL + "GetVodMyList.asp?deviceId=%s";										// ����� ����̽����� MyVod�� ���� ����Ʈ�� �����մϴ�.
		private static final String SetMyVod				= Server_URL + "SetMyVod.asp?deviceId=%s&assetId=%s&genreId=%s&mode=%s";			// ����ڰ� MyVod�� ������ �� ���˴ϴ�. (���� �������̹Ƿ� �׽�Ʈ�� ������ ��) ( Vod ������ ������ ���Ͽ� �������̽��� ������)
		// 1�� �������̽�
		// ���ϱ�
		// http://58.143.243.91/mobile/MyVod_insert.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String InsertMyVod				= OldServer_URL + "MyVod_insert.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";			// �� ������ ���� �������̽� �����.
		// ������
		// http://58.143.243.91/mobile/MyVod_DELETE.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529&AssetID=www.hchoice.co.kr%7CM0121727LSG117227301&Genre_ID=304547
		private static final String DeleteMyVod				= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s&AssetID=%s&Genre_ID=%s";			// �� ������ ���� �������̽� �����.
		// ���� �ʱ�ȭ
		// http://58.143.243.91/Mobile/myvod_delete.asp?MemberID=FE0873D5-D937-5110-8EFF-F192A13B4529
		private static final String DeleteAllMyVod				= OldServer_URL + "MyVod_DELETE.asp?MemberID=%s";			// �� ������ ���� �������̽� �����.		
		
		// VOD ������
		private static final String SetVodSetTopDisplayInfo	= Server_URL + "SetVodSetTopDisplayInfo.asp?deviceId=%s&assetId=%s";				// ��ž�� VOD�� ������ Display �մϴ�. (STB������ ������ ���Ͽ� �������̽� ������)
		private static final String Notification			= Server_URL + "Notification.asp?userId=%s&assetId=%s&type=%s";					// ��ž�ڽ����� MyVOD�� �����Ұ�� ���˴ϴ�. (VOD������ ������ ���Ͽ� �������̽� ������)
		///////////////////////////////////////////////////////////////////
		public static final String NOTIFICATION_WISHITEM_TYPE	= "wishItemRemoved"; 						// userRemoved : device�� ���asset ����, deviceId ����
		public static final String NOTIFICATION_USER_TYPE		= "userRemoved"; 
		public static final String VOD_MY_MODE_INPUT						= "0"; 						// 0:�Է�, 1:Ư������������,2:�ش������� ���Ŀ�������� 3: ���� �� ��絥���ͻ���
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
		// ���ϱ�
		public static String getInsertmyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(InsertMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// ������
		public static String getDeletemyvod(String aDeviceId, String aAssetId, String aGenreId) {
			return String.format(DeleteMyVod, aDeviceId, aAssetId, aGenreId);
		}
		// ���� �ʱ�ȭ
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
		private static final String SearchChannel			= Server_URL + "SearchChannel.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// ä�������� �˻��մϴ�
		private static final String SearchProgram			= Server_URL + "SearchProgram.asp?Search_String=%s&pageSize=%s&pageIndex=%s&areaCode=%s&productCode=%s";	// ���α׷� ������ �˻��մϴ�.
		private static final String SearchVod				= Server_URL + "SearchVod.asp?search_string=%s&pageSize=%s&pageIndex=%s&sortType=%s";	// VOD������ �˻��մϴ�.
		/////////////////////////////
		/*		���Ĺ�� (�⺻ : ChannelName Asc)
		�ֽŵ�ϼ� : ChannelNoAsc
		�ֽż����� : ChannelNoDesc
		�̸��� : ChannelNameAsc
		�̸��� ���� : ChannelNameDesc			*/
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
		private static final String GetGuideCategory		= Server_URL + "GetGuideCategory.asp?CategoryID=%s";											// ���� ���̵� ����Ʈ�� �����մϴ�.
		private static final String GetServiceGuideList		= Server_URL + "GetServiceguideList.asp";											// ���� ���̵� ����Ʈ�� �����մϴ�.
		private static final String GetServiceGuideInfo		= Server_URL + "GetServiceguideInfo.asp?CategoryID=%s";								// ���� ���̵� �������� �����մϴ�.
		private static final String GetServiceGuideImage	= Server_URL + "GetServiceGuideImage.asp";											// ��ž �̵�� �� ��ǰ �ȳ� ������ �����մϴ�.
		private static final String GetServiceJoinNList		= Server_URL + "GetServiceJoyNlist.asp";											// ���̿� ����Ʈ�� �����մϴ�.
		private static final String GetServiceJoinNInfo		= Server_URL + "GetServiceJoyNInfo.asp?joyNId=%s";									// �ϳ��� ���̿��� ���� �������� �����մϴ�.
		private static final String GetServiceNoticeInfo	= Server_URL + "GetServiceNoticeInfo.asp?areaCode=%s&productCode=%s";									// �ϳ��� ���̿��� ���� �������� �����մϴ�.
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
		//8.1 ����
		public static final String ERROR_100	= "100";		//����(Success)
		public static final String ERROR_200	= "200";		//�˼����� ����(General Error)
		public static final String ERROR_201	= "201";		//�������� �ʴ� ��������(Unsupported Protocol)
		public static final String ERROR_202	= "202";		//��������(Authentication Failure)
		public static final String ERROR_203	= "203";		//�������� �ʴ� ��������(Unsupported Profile)
		public static final String ERROR_204	= "204";		//�߸��� �Ķ���Ͱ�(Invalid Parameter)
		public static final String ERROR_205	= "205";		//�ش������ ����(Not Found)
		public static final String ERROR_206	= "206";		//���μ�������(Internal Server Error)
		public static final String ERROR_207	= "207";		//�������μ��� ����(Internal Processing Error)
		public static final String ERROR_211	= "211";		//�Ϲ� DB����(DB General Error)
		public static final String ERROR_221	= "221";		//�̹�ó���Ǿ���(Already Done)
		public static final String ERROR_223	= "223";		//�̹��߰��� �׸�(Duplicated Insertion)
		public static final String ERROR_231	= "231";		//�����ڵ�߱޽���(AuthCode Issue Failure)
		public static final String ERROR_232	= "232";		//����������ڵ�(AuthCode Expired)

		public class RecordResponCode{
			//8.2 ��ȭ��û
			public static final String	ERROR_001	= "001";			//Mac����ġ(Invalid MacAddress)
			public static final String	ERROR_002	= "002";			//�ߺ� ��ȭ �����û (Duplicated Recording Reserve Request)
			public static final String	ERROR_003	= "003";			//��ũ �뷮����(Disk Capacity Not Enough)
			public static final String	ERROR_004	= "004";			//Ʃ�ʸ� ��λ���ϰ� �־� ��ȭ/Ʃ�׺Ұ� (Authentication Failure)
			public static final String	ERROR_005	= "005";			//��ȭ �Ұ� ä��(Unsupported Recording Channel)
			public static final String	ERROR_006	= "006";			//�̹� ��ȭ �����(Already Recording Reserve Done)
			public static final String	ERROR_007	= "007";			//���α׷� ������ ����(Program Not Found)
			public static final String	ERROR_008	= "008";			//������� ��ȭ�� �����Ұ�( Recording Delete Error)
			public static final String	ERROR_009	= "009";			//ä�� ����(Channel Not Found)
			public static final String	ERROR_010	= "010";			//PIP�����(Using PIP Service)
			public static final String	ERROR_011	= "011";			//�ٸ�ä�� ��ȭ��(Already Other Channel Recording)
			public static final String	ERROR_012	= "012";			//��û���� ä��(LimitedView Channel)
			public static final String	ERROR_013	= "013";			//���� ä��(Limited Channel)
			public static final String	ERROR_014	= "014";			//��� ���(Hold Mode)
			public static final String	ERROR_015	= "015";			//�̹� ��ȭ�� (Already Recording)
			public static final String	ERROR_016	= "016";			//���� ���� (Delete Processing Error)
			public static final String	ERROR_017	= "017";			//�̸� ���� ���� (Name Replace Error)
			public static final String	ERROR_018	= "018";			//VOD�� ȭ�� ���� ���� (VOD DetailView Error)
			public static final String	ERROR_019	= "019";			//���ι̵�� ����� (Private Media Playing)
			public static final String	ERROR_020	= "020";			//������(������ ����) ������ (Already Playing DataService )
			public static final String	ERROR_021	= "021";			//VOD����� (VOD Playing)
			public static final String	ERROR_028	= "028";			//���� ���� STB Status Power Off
		}

		//8.3. ��ȭ/ Ʃ�׺Ұ� ���ڵ�
		public class RecordRejectCode{
			public static final String	ERROR_005_1	= "005_1";			//PIP�� �������̰� ���� ������ ��ûä�ΰ� �ٸ� ä���� ��ó�ȭ�� ���							|��� ��ȭ�Ұ�
			public static final String	ERROR_005_2	= "005_2";			//VOD�� ������ ���(JOY&LIFE)��û�� PIP�� �������ϰ��									|��� ��ȭ�Ұ�
			public static final String	ERROR_005_3	= "005_3";			//VOD�� ������ ���(JOY&LIFE)�� ��û���̰� �ٸ�ä���� ��ȭ���ϰ�� �� �ٸ� ä���� ��ȭ ��û��		|��� ��ȭ�Ұ�
			public static final String	ERROR_005_4	= "005_4";			//���� �ٸ� ��ä���� ��ȭ���̰� �̶� �� �ٸ�ä���� ��ȭ��û �� ���							|��� ��ȭ�Ұ�
			public static final String	ERROR_003	= "003";			//��ũ �뷮�� ������ ���															|��� ��ȭ�Ұ�
			public static final String	ERROR_002	= "002";			//�ߺ��� ��ȭ������ ���																|��ȭ ����Ұ�
			public static final String	ERROR_020	= "020";			//JOY&LIFE, VOD, �̵�� �ٹ� ���� ���� ���											|ä�� Ʃ�׺Ұ�
		}
	}

}

package hdlnc.dev2.sangtae.cnm.global;

public class Intent_KEY_Define {

	public final static String NAVI_TITLE					=  "Navi_TITLE";				// 타이틀바 제목
	//----------------XML Informaion---------------------------
	public class SETTOP{
		public final static String STATUS						=  "SETUP_STATUS";				// 사용회원으로 등록된 사용자인지 여부를 확인한다.
		
		public class MODE{
			public final static String UNREGISTERED				=  "Unregistered";				// 미등록
			public final static String NOT_SUPPORT				=  "NOT_SUPPORT";				// 미지원
			public final static String SUPPORT					=  "SUPPORT";					// 지원
			public final static String LIMITED_AREA				=  "LIMITED_AREA";				// 서비스 제한 지역
		}
	}
	
	public class MAIN_TAB{
		public final static String SELECT_MAIN_TAB		=  "SELECT_MAIN_TAB";				// 타이틀바 제목
		public class TYPE{
			public final static String TV_CHANNEL				=  "MAIN_TV_CHANNEL";				
			public final static String VOD						=  "MAIN_VOD";	
			public final static String REMOCON					=  "MAIN_REMOCON";	
			public final static String PVR						=  "MAIN_PVR";	
			public final static String SETTING					=  "MAIN_SETTING";
		}
	}
	
	public class Set_Mode{
		public final static String Externe_Setting				=  "Externe_Setting";				// 타이틀바 제목
		public class MODE{
			public final static int NORMAL					=  9000;
			public final static int SETTOP_JOIN				=  9001;				
			public final static int AUDLT_JOIN				=  9002;
			public final static int GUIDE_INFO				=  9003;
		}
	}
	
	public class Recording{
		public final static String ChannelRecoddingMode		=  "Channel_Recording_MODE";				// 사업자 채널 녹화 제한
	}
	
	
}

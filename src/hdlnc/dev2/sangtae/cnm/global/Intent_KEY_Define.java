package hdlnc.dev2.sangtae.cnm.global;

public class Intent_KEY_Define {

	public final static String NAVI_TITLE					=  "Navi_TITLE";				// Ÿ��Ʋ�� ����
	//----------------XML Informaion---------------------------
	public class SETTOP{
		public final static String STATUS						=  "SETUP_STATUS";				// ���ȸ������ ��ϵ� ��������� ���θ� Ȯ���Ѵ�.
		
		public class MODE{
			public final static String UNREGISTERED				=  "Unregistered";				// �̵��
			public final static String NOT_SUPPORT				=  "NOT_SUPPORT";				// ������
			public final static String SUPPORT					=  "SUPPORT";					// ����
			public final static String LIMITED_AREA				=  "LIMITED_AREA";				// ���� ���� ����
		}
	}
	
	public class MAIN_TAB{
		public final static String SELECT_MAIN_TAB		=  "SELECT_MAIN_TAB";				// Ÿ��Ʋ�� ����
		public class TYPE{
			public final static String TV_CHANNEL				=  "MAIN_TV_CHANNEL";				
			public final static String VOD						=  "MAIN_VOD";	
			public final static String REMOCON					=  "MAIN_REMOCON";	
			public final static String PVR						=  "MAIN_PVR";	
			public final static String SETTING					=  "MAIN_SETTING";
		}
	}
	
	public class Set_Mode{
		public final static String Externe_Setting				=  "Externe_Setting";				// Ÿ��Ʋ�� ����
		public class MODE{
			public final static int NORMAL					=  9000;
			public final static int SETTOP_JOIN				=  9001;				
			public final static int AUDLT_JOIN				=  9002;
			public final static int GUIDE_INFO				=  9003;
		}
	}
	
	public class Recording{
		public final static String ChannelRecoddingMode		=  "Channel_Recording_MODE";				// ����� ä�� ��ȭ ����
	}
	
	
}

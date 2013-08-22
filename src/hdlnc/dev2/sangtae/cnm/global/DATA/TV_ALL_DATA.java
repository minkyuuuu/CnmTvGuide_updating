package hdlnc.dev2.sangtae.cnm.global.DATA;


public class TV_ALL_DATA implements Cloneable {

	private String  Tag="";
	private String	ID;
	private String	NUMBER;
	private String	NAME;
	private String	INFO;
	private String	PROGRAM_onAIR_ID;
	private Boolean	PROGRAM_onAIR_HD;
	private String	CHANNEL_onAir_TIME;
	private String	CHANNEL_onAIR_TITLE;
	private String	PROGRAM_nextID;
	private String	CHANNEL_nextTIME;
	private String	CHANNEL_nextTITLE;
	private String	CHANNEL_LOG_IMG;
	private String	CHANNEL_VIEW;
	private Boolean	CHANNEL_RECORDING;
	
	public Boolean getPROGRAM_onAIR_HD() {
		return PROGRAM_onAIR_HD;
	}
	
	public void setPROGRAM_onAIR_HD(String pROGRAM_onAIR_HD) {
		if (pROGRAM_onAIR_HD.equals("YES")) {
			PROGRAM_onAIR_HD = true;
		}else {
			PROGRAM_onAIR_HD = false;
		}
	}
	public String getCHANNEL_VIEW() {
		return CHANNEL_VIEW;
	}

	public void setCHANNEL_VIEW(String cHANNEL_VIEW) {
		CHANNEL_VIEW = cHANNEL_VIEW;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (TV_ALL_DATA)super.clone();
	}
	
	// set
	public void setID(String iD) {
		ID = iD;
	}
	public void setNUMBER(String nUMBER) {
		NUMBER = nUMBER;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public void setINFO(String iNFO) {
		INFO = iNFO;
	}
	public void setPROGRAM_onAIR_ID(String pROGRAM_onAIR_ID) {
		PROGRAM_onAIR_ID = pROGRAM_onAIR_ID;
	}
	public void setCHANNEL_onAir_TIME(String cHANNEL_onAir_TIME) {
		CHANNEL_onAir_TIME = cHANNEL_onAir_TIME;
	}
	public void setCHANNEL_onAIR_TITLE(String cHANNEL_onAIR_TITLE) {
		CHANNEL_onAIR_TITLE = cHANNEL_onAIR_TITLE;
	}
	public void setPROGRAM_nextID(String pROGRAM_nextID) {
		PROGRAM_nextID = pROGRAM_nextID;
	}
	public void setCHANNEL_nextTIME(String cHANNEL_nextTIME) {
		CHANNEL_nextTIME = cHANNEL_nextTIME;
	}
	public void setCHANNEL_nextTITLE(String cHANNEL_nextTITLE) {
		CHANNEL_nextTITLE = cHANNEL_nextTITLE;
	}
	public void setCHANNEL_LOG_IMG(String cHANNEL_LOG_IMG) {
		CHANNEL_LOG_IMG = cHANNEL_LOG_IMG;
	}
	
	//get
	public String getTag() {
		// TODO Auto-generated method stub
		return Tag;
	}
	public String getID() {
		return ID;
	}
	public String getNUMBER() {
		return NUMBER;
	}
	public String getNAME() {
		return NAME;
	}
	public String getINFO() {
		return INFO;
	}
	public String getPROGRAM_onAIR_ID() {
		return PROGRAM_onAIR_ID;
	}
	public String getCHANNEL_onAir_TIME() {
		return CHANNEL_onAir_TIME;
	}
	public String getCHANNEL_onAIR_TITLE() {
		return CHANNEL_onAIR_TITLE;
	}
	public String getPROGRAM_nextID() {
		return PROGRAM_nextID;
	}
	public String getCHANNEL_nextTIME() {
		return CHANNEL_nextTIME;
	}
	public String getCHANNEL_nextTITLE() {
		return CHANNEL_nextTITLE;
	}
	public String getCHANNEL_LOG_IMG() {
		return CHANNEL_LOG_IMG;
	}

	public Boolean getCHANNEL_RECORDING() {
		return CHANNEL_RECORDING;
	}

	public void setCHANNEL_RECORDING(String cHANNEL_RECORDING) {
		if (cHANNEL_RECORDING.equals("YES")) {
			CHANNEL_RECORDING = true;
		}else {
			CHANNEL_RECORDING = false;
		}
	}
	
	
}

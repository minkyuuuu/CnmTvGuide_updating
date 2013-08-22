package hdlnc.dev2.sangtae.cnm.global.DATA;

public class SettopStatus_DATA implements Cloneable {

	private String	Version = "NULL";
	private String	TransactionId = "NULL";
	private String	ResultCode = "NULL";
	private String	ErrorString = "NULL";
	private String	MacAddress = "NULL";
	private String	Result = "NULL";
	private String	State = "NULL";
	private String	Errcode = "NULL";
	private String	Totalsize = "NULL";
	private String	Usagesize = "NULL";
	private String	Recordingchannel1 = "NULL";
	private String	Recordingchannel2 = "NULL";
	private String	Watchingchannel = "NULL";
	private String	Pipchannel = "NULL";
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (SettopStatus_DATA)super.clone();
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(String transactionId) {
		TransactionId = transactionId;
	}

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getErrorString() {
		return ErrorString;
	}

	public void setErrorString(String errorString) {
		ErrorString = errorString;
	}

	public String getMacAddress() {
		return MacAddress;
	}

	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getErrcode() {
		return Errcode;
	}

	public void setErrcode(String errcode) {
		Errcode = errcode;
	}

	public String getTotalsize() {
		return Totalsize;
	}

	public void setTotalsize(String totalsize) {
		Totalsize = totalsize;
	}

	public String getUsagesize() {
		return Usagesize;
	}

	public void setUsagesize(String usagesize) {
		Usagesize = usagesize;
	}

	public String getRecordingchannel1() {
		return Recordingchannel1;
	}

	public void setRecordingchannel1(String recordingchannel1) {
		Recordingchannel1 = recordingchannel1;
	}

	public String getRecordingchannel2() {
		return Recordingchannel2;
	}

	public void setRecordingchannel2(String recordingchannel2) {
		Recordingchannel2 = recordingchannel2;
	}

	public String getWatchingchannel() {
		return Watchingchannel;
	}

	public void setWatchingchannel(String watchingchannel) {
		Watchingchannel = watchingchannel;
	}

	public String getPipchannel() {
		return Pipchannel;
	}

	public void setPipchannel(String pipchannel) {
		Pipchannel = pipchannel;
	}

	
	
	
	
}


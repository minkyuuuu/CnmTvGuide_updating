package hdlnc.dev2.sangtae.cnm.global.DATA;

public class RESPONSE_DATA implements Cloneable {

	private String	Version = "NULL";
	private String	TransactionId = "NULL";
	private String	ResultCode = "NULL";
	private String	ErrorString = "NULL";
	private String	MacAddress = "NULL";
	private String	SoId = "NULL";
	private String	IpAddress = "NULL";
	private String	SetTopBoxKind = "NULL";
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (RESPONSE_DATA)super.clone();
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = new String(version);
	}

	public String getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(String transactionId) {
		TransactionId = new String(transactionId);
	}

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = new String(resultCode);
	}

	public String getErrorString() {
		return ErrorString;
	}

	public void setErrorString(String errorString) {
		ErrorString = new String(errorString);
	}

	public String getMacAddress() {
		return MacAddress;
	}

	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}

	public String getSoId() {
		return SoId;
	}

	public void setSoId(String soId) {
		SoId = soId;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public String getSetTopBoxKind() {
		return SetTopBoxKind;
	}

	public void setSetTopBoxKind(String setTopBoxKind) {
		SetTopBoxKind = setTopBoxKind;
	}	
	
	
}


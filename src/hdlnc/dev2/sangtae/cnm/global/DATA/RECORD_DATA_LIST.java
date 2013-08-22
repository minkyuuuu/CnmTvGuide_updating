package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class RECORD_DATA_LIST extends ArrayList<RECORD_DATA> implements Cloneable{
	private String transactionId;
	private String resultCode;
	private String errorString;
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (RECORD_DATA_LIST)super.clone();
	}


	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
}

package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class TV_ALL_DATA_LIST extends ArrayList<TV_ALL_DATA> implements Cloneable{
	private String resultCode;
	private String PageNum;
	private String transactionId;
	private String errorString;
	private String genreCode;
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (TV_ALL_DATA_LIST)super.clone();
	}


	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}


	public String getPageNum() {
		return PageNum;
	}

	public void setPageNum(String pageNum) {
		PageNum = pageNum;
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

	public String getGenreCode() {
		return genreCode;
	}

	public void setGenreCode(String genreCode) {
		this.genreCode = genreCode;
	}
	

}

package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class ADULT_AUTH_DATA extends ArrayList<GUIDE_DATA> implements Cloneable{
	private String Result="";
	private String Code="";
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (ADULT_AUTH_DATA)super.clone();
	}
	
	//set
	public void setResult(String arg) {
		Result = arg;
	}
	public void setResultCode(String arg) {
		Code = arg;
	}

	//get
	public String getResult() {
		return Result;
	}
	public String getResultCode() {
		return Code;
	}
}

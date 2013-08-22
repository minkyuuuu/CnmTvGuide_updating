package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class SETTOP_AUTH_DATA extends ArrayList<GUIDE_DATA> implements Cloneable{
	private String Result="";
	private String ErrString="";
	private String Code="";
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (SETTOP_AUTH_DATA)super.clone();
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

	public void setErrString(String errString) {
		ErrString = errString;
	}

	public String getErrString() {
		return ErrString;
	}
}

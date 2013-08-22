package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class UPDATE_INFO_LIST extends ArrayList<UPDATE_DATA> implements Cloneable {
	private String RESULT;
	
	@Override
	public UPDATE_INFO_LIST clone() {
		// TODO Auto-generated method stub
		return (UPDATE_INFO_LIST)super.clone();
	}
	//get
	public String getRESULT() {
		return RESULT;
	}
	//set
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

}

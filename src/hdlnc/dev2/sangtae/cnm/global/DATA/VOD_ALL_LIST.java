package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class VOD_ALL_LIST extends ArrayList<VOD_INFO> implements Cloneable {

	private String RESULT;

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (VOD_ALL_LIST)super.clone();
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

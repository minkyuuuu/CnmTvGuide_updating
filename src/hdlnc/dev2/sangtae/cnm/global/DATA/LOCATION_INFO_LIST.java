package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class LOCATION_INFO_LIST extends ArrayList<LOCATION_INFO> implements Cloneable {
	private String RESULT;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (LOCATION_INFO_LIST)super.clone();
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	// set
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}


}

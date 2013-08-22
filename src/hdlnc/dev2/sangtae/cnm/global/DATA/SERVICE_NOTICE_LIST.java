package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class SERVICE_NOTICE_LIST extends ArrayList<SERVICE_NOTICE_INFO> implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String RESULT;

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (SERVICE_NOTICE_LIST)super.clone();
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

package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class PRODUCT_INFO_LIST extends ArrayList<PRODUCT_INFO> implements Cloneable {

	private String RESULT;
	private int	   DefaultIndx;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (PRODUCT_INFO_LIST)super.clone();
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	public int getDefaultIndx() {
		return DefaultIndx;
	}
	//set
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}
	public void setDefaultIndx(int defaultIndx) {
		DefaultIndx = defaultIndx;
	}
	


}

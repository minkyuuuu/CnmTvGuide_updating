package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class SCUEDULE_DATA_LIST extends ArrayList<SCUEDULE_DATA> implements Cloneable {
	private String RESULT;
	private int		Count;
	
	@Override
	public SCUEDULE_DATA_LIST clone() {
		// TODO Auto-generated method stub
		return (SCUEDULE_DATA_LIST)super.clone();
	}
	//get
	public int getCount() {
		return Count;
	}
	public String getRESULT() {
		return RESULT;
	}
	//set
	public void setCount(int count) {
		Count = count;
	}
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

}

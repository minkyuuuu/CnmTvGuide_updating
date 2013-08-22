package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class JOYN_DATA_LIST extends ArrayList<JOYN_DATA> implements Cloneable{
	private String RESULT;
	private String JOYLIST_ITEM;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (JOYN_DATA_LIST)super.clone();
	}
	
	//set
	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}
	public void setJOYLIST_ITEM(String aJOYLIST_ITEM) {
		JOYLIST_ITEM = aJOYLIST_ITEM;
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	public String getJOYLISTITEM() {
		return JOYLIST_ITEM;
	}
}

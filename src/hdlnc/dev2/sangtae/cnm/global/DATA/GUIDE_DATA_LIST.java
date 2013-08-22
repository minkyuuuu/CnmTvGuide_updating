package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;

public class GUIDE_DATA_LIST extends ArrayList<GUIDE_DATA> implements Cloneable{
	private String RESULT;
	private String USEGUIDE_LIST_ITEM;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (GUIDE_DATA_LIST)super.clone();
	}
	
	//set
	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}
	public void setGUIDE_LIST_ITEM(String aGUIDE_ITEM) {
		USEGUIDE_LIST_ITEM = aGUIDE_ITEM;
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	public String getGUIDE_LIST_ITEM() {
		return USEGUIDE_LIST_ITEM;
	}
}

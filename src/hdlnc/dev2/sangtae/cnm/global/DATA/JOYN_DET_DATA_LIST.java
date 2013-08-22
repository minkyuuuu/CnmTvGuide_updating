package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.util.ArrayList;
import java.util.Iterator;

public class JOYN_DET_DATA_LIST extends ArrayList<JOYN_DET_DATA> implements Cloneable{
	private String RESULT;
	private String JOYITEM_ITEM;
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return (JOYN_DET_DATA_LIST)super.clone();
	}
	
	//set
	public void setRESULT(String aRESULT) {
		RESULT = aRESULT;
	}
	public void setJOYLIST_ITEM(String aJOYITEM_ITEM) {
		JOYITEM_ITEM = aJOYITEM_ITEM;
	}

	//get
	public String getRESULT() {
		return RESULT;
	}
	public JOYN_DET_DATA getJOYLISTITEMbyID(String aID) {
		JOYN_DET_DATA ret = null;
		for(Iterator itr = this.iterator(); itr.hasNext(); )
		{
			JOYN_DET_DATA joy_det_data = (JOYN_DET_DATA)itr.next();
			if(joy_det_data.getID().equals(aID))
				ret = joy_det_data;
		}
		return ret;
	}
}

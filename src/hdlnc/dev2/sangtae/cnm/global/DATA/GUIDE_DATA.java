package hdlnc.dev2.sangtae.cnm.global.DATA;

public class GUIDE_DATA implements Cloneable {

	private String	ID;
	private String	TITLE;
	private Boolean More;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (GUIDE_DATA)super.clone();
	}
	
	// set
	public void setID(String iD) {
		ID = iD;
	}
	public void setTITLE(String aTITLE) {
		TITLE = aTITLE;
	}
	
	//get
	public String getID() {
		return ID;
	}
	public String getTITLE() {
		return TITLE;
	}

	public Boolean getMore() {
		return More;
	}

	public void setMore(String aMore) {
		if (aMore.equals("YES")) {
			More = true;
		}else {
			More = false;
		}
		
	}
	
	
}


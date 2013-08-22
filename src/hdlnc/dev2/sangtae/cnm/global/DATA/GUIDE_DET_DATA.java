package hdlnc.dev2.sangtae.cnm.global.DATA;

public class GUIDE_DET_DATA implements Cloneable {

	private String  RESULT;
	private String	ID;
	private String	Title;
	private String	CONTENT;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (GUIDE_DET_DATA)super.clone();
	}
	
	
	public String getTitle() {
		return Title;
	}


	public void setTitle(String title) {
		Title = title;
	}


	// set
	public void setRESULT(String aResult) {
		RESULT = aResult;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setCONTENT(String aCONTENT) {
		CONTENT = aCONTENT;
	}
	
	//get
	
	public String getRESULT() {
		return RESULT;
	}
	public String getID() {
		return ID;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	
}


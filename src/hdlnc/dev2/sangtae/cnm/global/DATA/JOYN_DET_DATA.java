package hdlnc.dev2.sangtae.cnm.global.DATA;

public class JOYN_DET_DATA implements Cloneable {

	private String  RESULT;
	private String	ID;
	private String	TITLE;
	private String  SUBTITLE;
	private String	CONTENT;
	private String	IMG;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (JOYN_DET_DATA)super.clone();
	}
	
	// set
	public void setRESULT(String aResult) {
		RESULT = aResult;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setTITLE(String aTITLE) {
		TITLE = aTITLE;
	}
	public void setSUBTITLE(String aSUBTITLE) {
		SUBTITLE = aSUBTITLE;
	}
	public void setCONTENT(String aCONTENT) {
		CONTENT = aCONTENT;
	}
	public void setIMG(String aIMG) {
		IMG = aIMG;
	}
	
	//get
	
	public String getRESULT() {
		return RESULT;
	}
	public String getID() {
		return ID;
	}
	public String getTITLE() {
		return TITLE;
	}
	
	public String getSUBTITLE() {
		return SUBTITLE;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public String getIMG() {
		return IMG;
	}
	
}


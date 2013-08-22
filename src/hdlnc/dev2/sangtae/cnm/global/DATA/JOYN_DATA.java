package hdlnc.dev2.sangtae.cnm.global.DATA;

public class JOYN_DATA implements Cloneable {

	private String	ID;
	private String	TITLE;
	private String	SUBTITLE;
	private String	CONTENT;
	private String	THUMBNAIL_IMG;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (JOYN_DATA)super.clone();
	}
	
	// set
	public void setID(String iD) {
		ID = iD;
	}
	public void setTITLE(String aTITLE) {
		TITLE = aTITLE;
	}
	public void setSUBTITLE(String arg) {
		SUBTITLE = arg;
	}
	public void setCONTENT(String aCONTENT) {
		CONTENT = aCONTENT;
	}
	public void setTHUMBNAIL_IMG(String aTHUMBNAIL_IMG) {
		THUMBNAIL_IMG = aTHUMBNAIL_IMG;
	}
	
	//get
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
	public String getTHUMBNAIL_IMG() {
		return THUMBNAIL_IMG;
	}
}


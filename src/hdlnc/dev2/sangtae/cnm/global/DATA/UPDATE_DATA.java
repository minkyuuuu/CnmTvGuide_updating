package hdlnc.dev2.sangtae.cnm.global.DATA;

public class UPDATE_DATA implements Cloneable {
	public String  	Tag="";

	private String		ID;
	private String		Name;
	private String		Date;

	/**
	 * @param aData
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (UPDATE_DATA)super.clone();
	}
	public UPDATE_DATA() {
		// TODO Auto-generated constructor stub
	}

	public UPDATE_DATA(UPDATE_DATA aData) {
		// TODO Auto-generated constructor stub
		ID = aData.ID;
		Name = aData.Name;
		Date = aData.Date;
	}


	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
}

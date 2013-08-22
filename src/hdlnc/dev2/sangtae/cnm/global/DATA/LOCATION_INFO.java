package hdlnc.dev2.sangtae.cnm.global.DATA;

public class LOCATION_INFO implements Cloneable {
	private String ID;
	private String Name;
	private String Name2;
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (LOCATION_INFO)super.clone();
	}
	
	//set
	public void setID(String iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setName2(String name2) {
		Name2 = name2;
	}
	//get
	public String getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public String getName2() {
		return Name2;
	}
}

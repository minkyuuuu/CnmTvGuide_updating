package hdlnc.dev2.sangtae.cnm.global.DATA;

public class PRODUCT_INFO implements Cloneable {
	private String ID;
	private String Name;

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PRODUCT_INFO)super.clone();
	}
	//get	
	public String getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	//set
	public void setID(String iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
}

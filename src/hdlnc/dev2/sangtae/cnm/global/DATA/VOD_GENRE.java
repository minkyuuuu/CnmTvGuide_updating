package hdlnc.dev2.sangtae.cnm.global.DATA;

/*
 * XML ���Ǽ� ǥ����� �������� Ŭ���� �̸��� �����Ͽ����� ���ظ� �������� �ΰ������� �ʿ�
 * GENRE_ALL �� VOD �޴��� �帣�� �� ����Ʈ�� �ǹ�  
 */

public class VOD_GENRE {
	private String ID="";
	private String Title="";
	private String More="";

	//get	
	public String getID() {
		return ID;
	}
	public String getTitle() {
		return Title;
	}
	public String getMore() {
		return More;
	}
	//set
	public void setID(String id) {
		ID = id;
	}
	public void setTitle(String name) {
		Title = name;
	}
	public void setMore(String more) {
		More = more;
	}
}

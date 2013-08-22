package hdlnc.dev2.sangtae.cnm.global.DATA;

/*
 * XML 정의서 표기법을 기준으로 클래스 이름을 정의하였으나 이해를 돕기위해 부가설명이 필요
 * GENRE_ALL 은 VOD 메뉴의 장르별 탭 리스트를 의미  
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

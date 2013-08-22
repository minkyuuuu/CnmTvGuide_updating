package hdlnc.dev2.sangtae.cnm.global.DATA;

import java.io.Reader;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
/*
 * VOD 정보를 저장하는 공통 데이터 클래스
 * XML 정의서_ver1_2_release 참조
 * 문서상 공통 클래스로 사용할 수 있는 구조로 설계되어 있으나
 * 앞으로의 변동사항에 대하여 유기적으로 대처하기 위해 아래의 클래스를 각각 별도로 생성해둠
 *  VOD_NEWMOVIE.java / VOD_PREVIEW.java / VOD_TVREPLAY.java / VOD_GENRE_DETAIL.java
 *  
 *  20110105 인텐트로 송수신 가능한 객체를 만들기 위해 Parcelable 구현
*/

public class VOD_INFO implements Parcelable{
	private String DBID="";
	private String ID="";
	private String Title="";
	private String More="";
	private String Tag="";
	private String PosterUrl="";
	private String VideoUrl="";
	private String Director="";
	private String Actor="";
	private String Grade="";
	private String Contents="";
	private String RunningTime="";
	private String HD="";
	private String Price="";
	private String CATEGORY="";

	/*
	public VOD_INFO(Parcel in)
	{
		readFromParcel(in);
	}
	*/
	
	public String getCATEGORY() {
		return CATEGORY;
	}
	public void setCATEGORY(String cATEGORY) {
		CATEGORY = cATEGORY;
	}
	//get	
	public String getID() {
		return ID;
	}
	public String getDBID() {
		return DBID;
	}
	public String getTitle() {
		return Title;
	}
	public String getMore() {
		return More;
	}
	
	public String getTag() {
		return Tag;
	}
	public String getPosterUrl() {
//		PosterUrl = "http://58.143.243.91/UploadFiles/JoyN/201011101840180.jpg";
		return PosterUrl;
	}
	public String getVideoUrl() {
		return VideoUrl;
	}
	public String getDirector() {
		return Director;
	}
	public String getActor() {
		return Actor;
	}
	public String getGrade() {
		return Grade;
	}
	public String getContents() {
		return Contents;
	}
	public String getRunningTime() {
		return RunningTime;
	}
	public String getHD() {
		return HD;
	}
	public String getPrice() {
		return Price;
	}
	
	
	//set
	public void setID(String arg) {
		
		ID = arg;
		arg = arg.replace(".", "");
		arg = arg.replace("|", "");
		DBID = arg.trim();
		
	}
	public void setTitle(String arg) {
		Title = arg.trim();
	}
	public void setMore(String arg) {
		More = arg.trim();
	}
	public void setTag(String arg) {
		Tag = arg.trim();
	}
	public void setPosterUrl(String arg) {
		PosterUrl = arg.trim();
	}
	public void setVideoUrl(String arg) {
		VideoUrl = arg.trim();
	}
	public void setDirector(String arg) {
		Director = arg.trim();
	}
	public void setActor(String arg) {
		Actor = arg.trim();
	}
	public void setGrade(String arg) {
		Grade = arg.trim();
	}
	public void setContents(String arg) {
		Contents = arg.trim();
	}
	public void setRunningTime(String arg) {
		RunningTime = arg.trim();
	}
	public void setHD(String arg) {
		HD = arg.trim();
	}
	public void setPrice(String arg) {
		Price = arg.trim();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(ID);
		dest.writeString(Title);
		dest.writeString(More);
		dest.writeString(Tag);
		dest.writeString(PosterUrl);
		dest.writeString(VideoUrl);
		dest.writeString(Director);
		dest.writeString(Actor);
		dest.writeString(Grade);
		dest.writeString(Contents);
		dest.writeString(RunningTime);
		dest.writeString(HD);
		dest.writeString(Price);
	}
	
	public void readFromParcel(Parcel in)
	{
		ID = in.readString();
		Title= in.readString();
		More= in.readString();
		Tag = in.readString();
		PosterUrl = in.readString();
		VideoUrl = in.readString();
		Director = in.readString();
		Actor = in.readString();
		Grade = in.readString();
		Contents = in.readString();
		RunningTime = in.readString();
		HD = in.readString();
		Price = in.readString();
	}
	
	/*
	public static final Parcelable.Creator<VOD_INFO> CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new VOD_INFO(source);
		}

		@Override
		public Object[] newArray(int size) {
			// TODO Auto-generated method stub
			return new VOD_INFO[size];
		}
	};
	
	*/
	
}

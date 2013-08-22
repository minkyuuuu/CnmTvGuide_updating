package hdlnc.dev2.sangtae.cnm.global.DATA;

public class SERVICE_NOTICE_INFO {
	private String ID="";
	private String Title="";
	private String Content="";
	private String Location="";
	private String StartDate="";
	private String EndDate="";

	/*
	public VOD_INFO(Parcel in)
	{
		readFromParcel(in);
	}
	*/
	
	public void setID(String arg) {
		
		ID = arg;
	}
	public String getID() {
		return ID;
	}
	public void setTitle(String arg) {
		Title = arg.trim();
	}
	public String getTitle() {
		return Title;
	}	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
}
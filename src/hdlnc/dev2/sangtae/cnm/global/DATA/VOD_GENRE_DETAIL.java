package hdlnc.dev2.sangtae.cnm.global.DATA;

public class VOD_GENRE_DETAIL {
	private String ID;
	private String Title;
	private String More;
	private String Tag;
	private String PosterUrl;
	private String VideoUrl;
	private String Director;
	private String Actor;
	private String Grade;
	private String Contents;
	private String RunningTime;
	private String HD;

	//get	
	public String getID() {
		return ID;
	}
	public String getName() {
		return Title;
	}
	public String getMore() {
		return More;
	}
	
	public String getTag() {
		return Tag;
	}
	public String getPosterUrl() {
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
	
	
	//set
	public void setID(String arg) {
		ID = arg;
	}
	public void setTitle(String arg) {
		Title = arg;
	}
	public void setMore(String arg) {
		More = arg;
	}
	public void setTag(String arg) {
		Tag = arg;
	}
	public void setPosterUrl(String arg) {
		PosterUrl = arg;
	}
	public void setVideoUrl(String arg) {
		VideoUrl = arg;
	}
	public void setDirector(String arg) {
		Director = arg;
	}
	public void setActor(String arg) {
		Actor = arg;
	}
	public void setGrade(String arg) {
		Grade = arg;
	}
	public void setContents(String arg) {
		Contents = arg;
	}
	public void setRunningTime(String arg) {
		RunningTime = arg;
	}
	public void setHD(String arg) {
		HD = arg;
	}
}

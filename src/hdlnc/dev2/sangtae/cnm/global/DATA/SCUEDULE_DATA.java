package hdlnc.dev2.sangtae.cnm.global.DATA;

public class SCUEDULE_DATA implements Cloneable {
	private String  	Tag="";
	private String		Date;
	private String		ID;		// 프로그램의 고유 아이디
	private String		Title;	// 프로그램 제목
	private String		Content;
	private String		Time;	// 방영 시간
	
	private String		Seq;	// 방영물 일련번호
	private Boolean		HD;		// HD 방송 여부
	private String		Grade;	// 방영 등급
	private String      Alarm_time;
	
	private String      SeriesId;
	private String      ProgramKey;
	

	public String getAlarm_time() {
		return Alarm_time;
	}
	public void setAlarm_time(String alarm_time) {
		Alarm_time = alarm_time;
	}
	/**
	 * @param aData
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (SCUEDULE_DATA)super.clone();
	}
	public SCUEDULE_DATA() {
		// TODO Auto-generated constructor stub
	}
	
	public SCUEDULE_DATA(SCUEDULE_DATA aData) {
		// TODO Auto-generated constructor stub
		Date = aData.getDate();
		ID = aData.ID;
		Title = aData.Title;
		Content = aData.Content;
		Time = aData.Time;
		
		Seq = aData.Seq;
		HD = aData.getHD();
		Grade = aData.Grade;
	}

	//get
	public String getTag() {
		return Tag;
	}
	public String getContent() {
		return Content;
	}
	public String getDate() {
		return Date;
	}
	public String getID() {
		return ID;
	}
	public String getTime() {
		return Time;
	}
	public String getTitle() {
		return Title;
	}
	
	
	public String getSeq() {
		return Seq;
	}
	public Boolean getHD() {
		return HD;
	}
	public String getGrade() {
		return Grade;
	}

	//set
	public void setTag(String tag) {
		Tag = tag;
	}
	public void setContent(String content) {
		Content = content;
	}
	public void setDate(String date) {
		Date = date;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setTime(String time) {
		Time = time;
	}
	public void setTitle(String title) {
		Title = title;
	}
	
	public void setSeq(String seq) {
		Seq = seq;
	}
	public void setHD(Boolean hD) {
		HD = hD;
	}
	public String getSeriesId() {
		return SeriesId;
	}
	public void setSeriesId(String seriesId) {
		SeriesId = seriesId;
	}
	public String getProgramKey() {
		return ProgramKey;
	}
	public void setProgramKey(String programKey) {
		ProgramKey = programKey;
	}
	public void setGrade(String grade) {
		Grade = grade;
	}
	
	

}

package hdlnc.dev2.sangtae.cnm.global.DATA;


public class RECORD_DATA implements Cloneable {

	private Boolean RecordingType;
	private String	RecordId;
	private String	SeriesId;
	private String	ScheduleId;
	private String	ChannelId;
	private String	ChannelNo;
	private String	ChannelName;
	private String	Program_Grade;
	private String	Channel_logo_img;
	private String	ProgramName;
	private String	RecordStartTime;
	private String	RecordEndTime;
	private Boolean	RecordHD;
	private Boolean	RecordPaytype;
	private Boolean	RecordProtection;
	private Boolean	RecordStatus;
	
	public Boolean getRecordingType() {
		return RecordingType;
	}

	public void setRecordingType(String recordingType) {
		if (recordingType.contains("0")) {
			RecordingType = true;
		}else {
			RecordingType = false;
		}
	}

	
	public String getRecordId() {
		return RecordId;
	}

	public void setRecordId(String recordId) {
		RecordId = recordId;
	}

	public String getSeriesId() {
		return SeriesId;
	}

	public void setSeriesId(String seriesId) {
		SeriesId = seriesId;
	}

	public String getScheduleId() {
		return ScheduleId;
	}

	public void setScheduleId(String scheduleId) {
		ScheduleId = scheduleId;
	}

	public String getChannelId() {
		return ChannelId;
	}

	public void setChannelId(String channelId) {
		ChannelId = channelId;
	}

	public String getChannelNo() {
		return ChannelNo;
	}

	public void setChannelNo(String channelNo) {
		ChannelNo = channelNo;
	}

	public String getChannelName() {
		return ChannelName;
	}

	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}

	public String getProgram_Grade() {
		return Program_Grade;
	}

	public void setProgram_Grade(String program_Grade) {
		Program_Grade = program_Grade;
	}

	public String getChannel_logo_img() {
		return Channel_logo_img;
	}

	public void setChannel_logo_img(String channel_logo_img) {
		Channel_logo_img = channel_logo_img;
	}

	public String getProgramName() {
		return ProgramName;
	}

	public void setProgramName(String programName) {
		ProgramName = programName;
	}

	public String getRecordStartTime() {
		return RecordStartTime;
	}

	public void setRecordStartTime(String recordStartTime) {
		RecordStartTime = recordStartTime;
	}

	public String getRecordEndTime() {
		return RecordEndTime;
	}

	public void setRecordEndTime(String recordEndTime) {
		RecordEndTime = recordEndTime;
	}

	public Boolean getRecordHD() {
		return RecordHD;
	}

	public void setRecordHD(String recordHD) {
		if (recordHD.contains("Y")) {
			RecordHD = true;
		}else {
			RecordHD = false;
		}
	}

	public Boolean getRecordPaytype() {
		return RecordPaytype;
	}

	public void setRecordPaytype(String recordPaytype) {
		if (recordPaytype.contains("0")) {
			RecordPaytype = true;
		}else {
			RecordPaytype = false;
		}
	}
	
	public Boolean getRecordProtection() {
		return RecordProtection;
	}

	public void setRecordProtection(String aRecordProtection) {
		if (aRecordProtection.contains("Y")) {
			RecordProtection = true;
		}else {
			RecordProtection = false;
		}
	}

	public Boolean getRecordStatus() {
		return RecordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		if (recordStatus.contains("Y")) {
			RecordStatus = true;
		}else {
			RecordStatus = false;
		}
	}
	
	
	
	
	
}

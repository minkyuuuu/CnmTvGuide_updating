package hdlnc.dev2.sangtae.cnm.global;

import java.io.Serializable;

public class TVCH_CheckList implements Serializable 
{

	private static final long serialVersionUID = 1L;

	private String	DB_ID;
	//private String	DB_Alarm_Time;
	private String	DB_MyChannel_Time;
	//private Boolean	DB_Alarm;
	private Boolean DB_MyChannel;
	//private Boolean DB_Hide;

	//private boolean complete;

	public TVCH_CheckList(String ID) {
		// TODO Auto-generated constructor stub
		DB_ID=ID;
		//DB_Alarm_Time		= "NULL";
		//DB_Alarm	= false;
		DB_MyChannel	= false;
		//DB_Hide			= false;
	}
	//get
	public String getID() {
		return DB_ID;
	}
	public Boolean getMyChannel() {
		return DB_MyChannel;
	}
	public String get_MyChannel_Time() {
		return DB_MyChannel_Time;
	}
	/*public Boolean getAlarm() {
		return DB_Alarm;
	}
	public String getAlarm_Time() {
		return DB_Alarm_Time;
	}*/


	//set
	public void setID(String iD) {
		DB_ID = iD;
	}
	public void setMyChannel(Boolean myChannel) {
		DB_MyChannel = myChannel;
	}
	public void set_MyChannel_Time(String dB_MyChannel_Time) {
		DB_MyChannel_Time = dB_MyChannel_Time;
	}
	/*public void setAlarm(Boolean Alarm) {
		DB_Alarm = Alarm;
	}
	public void setAlarmTime(String aTime) {
		DB_Alarm_Time = aTime;
	}*/





}

package hdlnc.dev2.sangtae.cnm.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TV_MSG_SQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DBPR_NAME = "tvmsg.db";
	//public static final String DBPR_NAME = "tvch_check_.db";
	public final static int VERSION = 1;

	public static final String TV_MSG_TABLE			= "remote_tv_msg_table";
	public static final String MESSAGE_ID			= "_id";
	public static final String MESSAGE_USER 		= "message_user";
	public static final String MESSAGE_DATE			= "message_date";
	public static final String MESSAGE_BODY			= "message_body";

	
	//private String CREATE = "CREATE TABLE IF NOT EXISTS ";
	
	public TV_MSG_SQLiteOpenHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DBPR_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL("CREATE TABLE " + TV_MSG_TABLE + " ("
                  + MESSAGE_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT,"//" TEXT PRIMARY KEY,"
                  + MESSAGE_DATE + " TEXT,"
                  + MESSAGE_BODY + " TEXT"
                 // + PROGRAM_ALARM + " TEXT,"
                 // + PROGRAM_ALARM_TIME + " TEXT"
                  + ");");
		  
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TV_MSG_TABLE);
		onCreate(db);
	}

}

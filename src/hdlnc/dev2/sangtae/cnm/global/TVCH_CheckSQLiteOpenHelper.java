package hdlnc.dev2.sangtae.cnm.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TVCH_CheckSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "tvch_check_.db";
	public static final int VERSION = 1;

	public static final String TVCH_CHECK_TABLE		= "tvch_check";
	public static final String CHANNEL_ID			= "_channel_id";
	public static final String MY_CHANNEL			= "_channel";
	public static final String MY_CHANNEL_TIME		= "_channel_time";
	/*public static final String PROGRAM_ALARM 		= "_reserve";
	public static final String PROGRAM_ALARM_TIME	= "_program";
	public static final String PROGRAM_COMPLETE		= "_complete";*/

	
	private String CREATE = "CREATE TABLE IF NOT EXISTS ";
	
	public TVCH_CheckSQLiteOpenHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL("CREATE TABLE " + TVCH_CHECK_TABLE + " ("
                  + CHANNEL_ID + " TEXT PRIMARY KEY,"
                  + MY_CHANNEL + " TEXT,"
                  + MY_CHANNEL_TIME + " TEXT"
                  /*+ PROGRAM_ALARM + " TEXT,"
                  + PROGRAM_ALARM_TIME + " TEXT"*/
                  + ");");
		  
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TVCH_CHECK_TABLE);
		onCreate(db);
	}

}

package sipl.bombino.databseOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sipl.bombino.helper.CommonFunction;

public class DataBaseHandlerDelete extends DataBaseHandler {

	String Qstr = "";
	DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	Date date = new Date();

	public DataBaseHandlerDelete(Context context) {
		super(context);
	}
	
	// function to delete old record from PODDetail
	public long deleteOldPodDetailTable(String ServerDate) {
		int result = -1;
		try {
			SQLiteDatabase dbDelete = this.getWritableDatabase();
			result = dbDelete.delete(Table_POD, Key_RunsheetDate + "<>?"
					,new String[] {ServerDate});
			dbDelete.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
	
	// Function to delete old record from InsertPODEntry
		public long DeleteFromInsertPODEntry(String ServerDate) {
			SQLiteDatabase db = this.getWritableDatabase();
			long result = -1;
			try {
				result = db.delete(Table_Insert_POD, Key_Ins_RunsheetDate + "<>?",
						new String[] { ServerDate});
				db.close();
			} catch (Exception e) {
				db.close();
				if(CommonFunction.LOG)
					Log.e("Error ",e.getMessage());
			}
			return result;
		}

	// Function To Delete Old Record From LoginDetail Table.
	public long deleteLoginDetail(String CreatedDate) {
		long lastDeletedID=-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			lastDeletedID=db.delete(Table_Login, "ServerDate<>?", new String [] {CreatedDate});
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return lastDeletedID;
	}

	

	// Function to delete old data from deviceverify table
	public void deleteFromDeviceVerify(String CreatedDate) {
		try {
			@SuppressWarnings("unused")
			int lastDeletedID=-1;
			SQLiteDatabase db = this.getWritableDatabase();
			lastDeletedID= db.delete(TABLE_DEVICE_VERIFICATION,
					KEY_DEVICE_ServerDate+"<>?", new String[] {CreatedDate});
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// function to delete System Updated record from PODDetail
	public void funDeleteSystemUpdatedRecord(String AwbNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			@SuppressWarnings("unused")
			int result = db.delete(Table_POD, Key_Awbno + "=?",
					new String[] { AwbNo });
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}
	
	// function to delete MeterReader data
	public int deleteMeterReaderValue(String id) {
		int result =-1;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			 result = db.delete(Table_MeterReading, Key_MR_ID + "=?",new String[] { id });
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
	
	//function to delete record from table
	public long deleteTableRecord(String tableName){
		long result=-1;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			 result = db.delete(tableName, null,null);
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
}

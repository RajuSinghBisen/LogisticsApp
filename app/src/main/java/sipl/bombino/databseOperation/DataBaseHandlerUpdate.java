package sipl.bombino.databseOperation;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.PodGetterSetter;

public class DataBaseHandlerUpdate extends DataBaseHandler {

	public DataBaseHandlerUpdate(Context context) {
		super(context);
	}

	// function to update PODDetail table
	public long upDatePodDetailTable(String AwbNo, String PktStatus) {
		long result=-1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put(Key_PktStatus, PktStatus);
			values.put(Key_IsUpdate, "1");
			result=db.update(Table_POD, values, Key_Awbno+"=?", new String[] {AwbNo});
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
	
	//function update login table serverdate
	public long updateLoginServerDate(String Serverdate){
		long result=-1;
		try{
			SQLiteDatabase db=this.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put(Key_Log_ServerDate, Serverdate);
			result=db.update(Table_Login, values, null, null);
		}
		catch(Exception e){
			
		}
		return result;
	}
	
	//funtion will change Password
	public long upDateLoginTableForChangePassword(String NewPassword){
		long result=-1;
		try{
			SQLiteDatabase db=this.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put(Key_Log_Password, NewPassword);
			result=db.update(Table_Login, values, null, null);
			db.close();
		}
		catch(Exception e){
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}



	public void updateAndroidVersion(String Version) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Key_Log_AppVersion, Version);
			db.update(Table_Login, values, null, null);
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	public void funToUpdatePODDetail(String AWBNo) {
		long Result = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Key_IsUpdatedOnLive, "1");
			Result = db.update(Table_POD, values, Key_Awbno + "=?",
					new String[] { AWBNo });
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		Log.d("","");
	}

	// updating record to isUpdateOnLive to 1
	public long updatePODInsertTable(String AwbNo) {
		long id = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Key_Ins_IsUpdatedOnLive, "1");
			id = db.update(Table_Insert_POD, values, Key_Ins_AWBNo + "=?",
					new String[] { AwbNo });
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return id;
	}

	@SuppressLint("SimpleDateFormat") public long updatePODDetailTable(PodGetterSetter info) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		long result = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Key_Signature, info.getSignature());
			values.put(Key_POD_Pic, info.getPODPic());
			values.put(Key_RC_Name, info.getRCName());
			values.put(Key_RC_Phone, info.getRCPhone());
			values.put(Key_Relation, info.getRCRelation());
			values.put(Key_PacketStatus, info.getPktStatus());
			values.put(Key_Remarks, info.getRCRemarks());
			values.put(Key_IMEINo, info.getIMEINo());
			values.put(Key_Payment_Type, info.getPaymentType());
			values.put(Key_COD_Amount, info.getCODAmount());
			values.put(Key_Latitude, info.getLatitude());
			values.put(Key_Longitude, info.getLongitude());
			values.put(Key_DeliveryTime, sdf.format(cal.getTime()));
			values.put(Key_IDProofType, info.getIDProofType());
			values.put(Key_IDProofTypeNo, info.getIDProofNo());
			values.put(Key_PODRemarks, info.getPODRemarks());
			values.put(Key_Ecode, info.getECode());
			values.put(Key_IsUpdatedOnLive, "0");
			values.put(Key_UD_Remarks, info.getUDRemarks());
			result = db.update(Table_POD, values, Key_Awbno + "=? and "
					+ Key_RunsheetNo + "=?", new String[] { info.getAwbNo(),
					info.getRunsheetNo() });
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
	
	// updating record to isUpdateOnLive to 1
		public long updateMeterReaderToOne(String meterID) {
			long id = 0;
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			try {
				values.put(Key_MR_IsUpdatedOnLive, "1");
				id = db.update(Table_MeterReading, values, Key_MR_ID + "=?",
						new String[] { meterID });
			} catch (Exception e) {
				if(CommonFunction.LOG)
					Log.e("Error ",e.getMessage());
			}
			return id;
		}
}

package sipl.bombino.databseOperation;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.AndroidLog;
import sipl.bombino.properties.DeviceInfo;
import sipl.bombino.properties.DeviceVerificationGetterSetter;
import sipl.bombino.properties.GetRcRelationGetterSetter;
import sipl.bombino.properties.GetRemarksGetterSetter;
import sipl.bombino.properties.MeterReadingInfo;
import sipl.bombino.properties.PODRemarksMasterInfo;
import sipl.bombino.properties.PODUser;
import sipl.bombino.properties.PacketStatusGetterSetter;
import sipl.bombino.properties.PodGetterSetter;
import sipl.bombino.properties.PodInsertGetterSetter;
import sipl.bombino.properties.ValidationInfo;

public class DataBaseHandlerInsert extends DataBaseHandler {

	public DataBaseHandlerInsert(Context context) {
		super(context);
	}
	
	// Function To Insert Record Into LoginTable
	public void addRecordIntoLogin(PODUser user) {
		DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		try {
			values.put(Key_Log_UserID, user.getUserID());
			values.put(Key_Log_UserName, user.get_UserName());
			values.put(Key_Log_Password, user.getPassword());
			values.put(Key_Log_IEMINO, user.getIEMEINO());
			values.put(Key_Log_AppVersion, user.get_AppVersion());
			values.put(Key_Log_Created_Date, dateformat.format(date));
			values.put(Key_Log_ServerDate,user.get_ServerDate());
			db.insert(Table_Login, null, values);
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// function to insert record into podDetail table
	public void addRecordIntoPODdetail(PodGetterSetter podObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(Key_Awbno, podObj.getAwbNo());
			values.put(Key_InvoiceAmount, podObj.getInvoiceAmount());
			values.put(Key_Consignee, podObj.getConsignee());
			values.put(Key_RunsheetDate, podObj.getRunsheetDate());
			values.put(Key_IsDownOnPDA, podObj.getIsDownonPDA());
			values.put(Key_Address, podObj.getAddress());
			values.put(Key_Phone, podObj.getPhone());
			values.put(Key_IsUpdate,"0");
			values.put(Key_IsUpdatedOnLive, "0");
			values.put(Key_P_CreatedDate, podObj.getCreatedDate());
			values.put(Key_P_ServerDate, podObj.getServerdate());
			values.put(Key_RunsheetNo, podObj.getRunsheetNo());
			db.insert(Table_POD, null, values);
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// function to insert record into packetStatus table
	public long addRecordIntoPacketStatusTable(List<PacketStatusGetterSetter> podObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		long result=-1;
		try {
			ContentValues values = new ContentValues();
			db.beginTransaction();
			for(PacketStatusGetterSetter info:podObj){
				values.put(KEY_PKT_STATUS_CODE, info.getPacketStatusCode());
				values.put(KEY_PKT_STATUS, info.getPacketStatus());
				result=db.insert(TABLE_PACKET_STATUS, null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}

	// function to insert record into RcRelation table
	public long addRecordIntoRcRelationTable(List<GetRcRelationGetterSetter> RcObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		long result=-1;
		try {
			ContentValues values = new ContentValues();
			db.beginTransaction();
			for(GetRcRelationGetterSetter info:RcObj){
				values.put(KEY_RC_RELATION_CODE, info.getRcRelationCode());
				values.put(KEY_RC_RELATION, info.getRcRelation());
				result=db.insert(TABLE_GET_RC_RELATION, null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}

	// function to insert record into Remarks table
	public long addRecordIntoRemarksTable(List<GetRemarksGetterSetter> RemObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		long result=-1;
		try {
			ContentValues values = new ContentValues();
			db.beginTransaction();
			for(GetRemarksGetterSetter info:RemObj){
				values.put(KEY_RC_REMARKS_CODE, info.getRcRemarkCode());
				values.put(KEY_RC_REMARKS, info.getRcRemark());
				result=db.insert(TABLE_GET_REMARKS, null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error",e.getMessage());
		}
		return result;
	}
	
	
	// function to insert record into Remarks table
		public long addRecordIntoPODRemarksMaster(List<PODRemarksMasterInfo> RemObj) {
			SQLiteDatabase db = this.getWritableDatabase();
			long result=-1;
			try {
				ContentValues values = new ContentValues();
				db.beginTransaction();
				for(PODRemarksMasterInfo info:RemObj){
					values.put(Key_PR_PODRemarks, info.getPodRemarks());
					result=db.insert(Table_PODRemarksMaster, null, values);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				db.close();
			} catch (Exception e) {
				db.close();
				if(CommonFunction.LOG)
					Log.e("Error ",e.getMessage());
			}
			return result;
		}
	
	// function to insert record into PodInsert Table
	public long addRecordIntoPodInsertTable(PodInsertGetterSetter PodInsObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		long result = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(Key__Ins_CONSIGNEE, PodInsObj.getConsignee());
			values.put(Key_Ins_AWBNo, PodInsObj.getAwbNo());
			values.put(Key_Ins_Invoice_Amount, PodInsObj.getInvoiceAmount());
			values.put(Key_Ins_COD_Amount, PodInsObj.getCODAmount());
			values.put(Key_Ins_Address, PodInsObj.getAddress());
			values.put(Key_Ins_Phone, PodInsObj.getPhone());
			values.put(Key_PktStatus, PodInsObj.getPktStatus());
			values.put(Key_Ins_IsUpdatedOnLive, "0");
			values.put(Key_Ins_RunsheetDate, PodInsObj.getRunSheetDate());
			values.put(Key_Ins_RunsheetNo, PodInsObj.getRunSheetNo());
			values.put(Key_Ins_CreatedDate, PodInsObj.getCreatedDate());
			values.put(Key_Ins_ServerDate, PodInsObj.getServerDate());
			result = db.insert(Table_Insert_POD, null, values);
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}

	// function to add device verification status
	public void addRecordIntoDeviceVeri(DeviceVerificationGetterSetter DeviceObj) {
		SQLiteDatabase db = this.getWritableDatabase();
		DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_DEVICE_CREATED_DATE, dateformat.format(date));
			values.put(KEY_DEVICE_STATUS, DeviceObj.getDeviceStatus());
			db.insert(TABLE_DEVICE_VERIFICATION, null, values);
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// inserting DeviceInfo
	public void addRecordInDeviceInfo(DeviceInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_DEVICEINFO_ECODE, info.getEcode());
			values.put(KEY_DEVICEINFO_IMEINO, info.getIMEINo());
			values.put(KEY_DEVICEINFO_DEVICENAME, info.getDeviceName());
			values.put(KEY_DEVICEINFO_INTERNALSTORAGE,info.getInternalStorage());
			values.put(KEY_DEVICEINFO_AVAILABEL_INTERNALSTORAGE,info.getAvailableInternaleStorage());
			values.put(KEY_DEVICEINFO_EXTERNALSTORAGE,info.getExternalStorage());
			values.put(KEY_DEVICEINFO_AVAILABEL_EXTERNALSTORAGE,info.getAvailableExternalStorage());
			values.put(KEY_DEVICEINFO_CREATEDDATE, info.getCreatedDate());
			db.insert(TABLE_DEVICEINFO, null, values);
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// inserting Application log
	public void addRecordInAndroidLog(AndroidLog logInfo) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(Key_L_Ecode, logInfo.getEcode());
			values.put(Key_L_LogText, logInfo.getLogText());
			values.put(Key_L_ServiceRequestTime, logInfo.getServiceRequestTime());
			values.put(Key_L_ServiceResponseTime,logInfo.getServiceResponseTime());
			values.put(Key_L_LogType,logInfo.getLogType());
			values.put(Key_L_IsUpdatedOnLive,logInfo.getIsUpdatedOnLive());
			values.put(Key_L_ConnectionTypeUsed, logInfo.getConnectionUsed());
			values.put(Key_L_CreatedDate,logInfo.getCreatedDate());
			db.insert(TABLE_AndroidLog, null, values);
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}
	
	// inserting into MeterReading
	public long insertIntoMeterReading(MeterReadingInfo logInfo) {
		long result=-1;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(Key_MR_MeterReading, logInfo.getMeterReadingText());
			values.put(Key_MR_MeterReadingImage, logInfo.getMeterReadingImage());
			values.put(Key_MR_CreatedDate, logInfo.getCreatedDate());
			values.put(Key_MR_Latitude, logInfo.getLatitude());
			values.put(Key_MR_Longitude, logInfo.getLongitude());
			result=db.insert(Table_MeterReading, null, values);
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}

	// inserting into MeterReading
	public long insertIntoAndroid(List<ValidationInfo> listValidation) {
		long result=-1;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			for(ValidationInfo info: listValidation){
				values.put(Key_VM_FieldName, info.getValidationField());
				values.put(Key_VM_IsValidationRequired, info.getIsValidationRequired());
				result=db.insert(Table_ValidationMaster, null, values);
			}
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return result;
	}
}

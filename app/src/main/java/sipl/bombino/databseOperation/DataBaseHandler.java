package sipl.bombino.databseOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sipl.bombino.helper.CommonFunction;

public class DataBaseHandler extends SQLiteOpenHelper {
	protected static final int DATABASE_VERSION = 40;
	protected static final String DATABASE_NAME = "Bombino.db";

	// Login Table
	protected static final String Table_Login = "LoginDetail";
	protected static final String Key_Log_login_ID = "_id";
	protected static final String Key_Log_UserID = "UserID";
	protected static final String Key_Log_UserName = "UserName";
	protected static final String Key_Log_Password = "Password";
	protected static final String Key_Log_IEMINO = "IMEINo";
	protected static final String Key_Log_AppVersion = "AppVersion";
	protected static final String Key_Log_Created_Date = "CreatedDate";
	protected static final String Key_Log_ServerDate = "ServerDate";
	// Login Table Ends

	// Table TABLE_POD Start
	protected static String Table_POD = "PODDetail";
	protected static String Key_Id = "_id";
	protected static String Key_Awbno = "awbno";
	protected static String Key_InvoiceAmount = "invoiceamount";
	protected static String Key_Consignee = "consignee";
	protected static String Key_RunsheetNo = "RunsheetNo";
	protected static String Key_RunsheetDate = "runsheetdate";
	protected static String Key_IsDownOnPDA = "isdownonpda";
	protected static String Key_Address = "address";
	protected static String Key_Phone = "phone";
	protected static String Key_IsUpdate = "isupdate";
	protected static String Key_IsUpdatedOnLive = "IsUpdatedOnLive";
	protected static String Key_Signature = "signature";
	protected static String Key_POD_Pic = "podpic";
	protected static String Key_RC_Name = "rcname";
	protected static String Key_RC_Phone = "rcphone";
	protected static String Key_Relation = "rcrelation";
	protected static String Key_PacketStatus = "pktstatus";
	protected static String Key_Remarks = "rcremarks";
	protected static String Key_IMEINo = "imeino";
	protected static String Key_Payment_Type = "paymenttype";
	protected static String Key_COD_Amount = "codamount";
	protected static String Key_Latitude = "latitude";
	protected static String Key_Longitude = "longitude";
	protected static String Key_DeliveryTime = "deliverytime";
	protected static String Key_IDProofType = "IDProofType";
	protected static String Key_IDProofTypeNo = "IDProofTypeNo";
	protected static String Key_PODRemarks = "PodRemarks";
	protected static String Key_Ecode = "ecode";
	protected static String Key_P_CreatedDate = "CreatedDate";
	protected static String Key_P_ServerDate = "ServerDate";
	protected static String Key_UD_Remarks = "RcUDremarks";
	// Table TABLE_POD End

	// Table PodInsert Start
	protected static String Table_Insert_POD = "InsertPODEntry";
	protected static String Key_Ins_ID = "_id";
	protected static String Key__Ins_CONSIGNEE = "Consignee";
	protected static String Key_Ins_AWBNo = "Awbno";
	protected static String Key_Ins_Invoice_Amount = "InvoiceAmount";
	protected static String Key_Ins_COD_Amount = "CODAmount";
	protected static String Key_Ins_Address = "Address";
	protected static String Key_Ins_Phone = "Phone";
	protected static String Key_PktStatus = "Pktstatus";
	protected static String Key_Ins_IsUpdatedOnLive = "IsUpdatedOnLive";
	protected static String Key_Ins_RunsheetDate = "RunsheetDate";
	protected static String Key_Ins_RunsheetNo = "RunsheetNo";
	protected static String Key_Ins_CreatedDate = "CreatedDate";
	protected static String Key_Ins_ServerDate = "ServerDate";
	// Table PodInsert End

	// Table GetPacketStatus Start
	protected static final String TABLE_PACKET_STATUS = "PacketStatus";
	protected static final String KEY_PKT_STATUS_ID = "_id";
	protected static final String KEY_PKT_STATUS_CODE = "PktStatusCode";
	protected static final String KEY_PKT_STATUS = "PktStatus";
	// Table GetPacketStatus End

	// Table GetRcRelation Start
	protected static final String TABLE_GET_RC_RELATION = "RcRelation";
	protected static final String KEY_RELATION_ID = "_id";
	protected static final String KEY_RC_RELATION_CODE = "RcRelationCode";
	protected static final String KEY_RC_RELATION = "RcRelation";
	// Table GetRcRelation End

	// TABLE GetRemarks Start
	protected static final String TABLE_GET_REMARKS = "RcRemarks";
	protected static final String KEY_REMARKS_ID = "_id";
	protected static final String KEY_RC_REMARKS_CODE = "RcRemarksCode";
	protected static final String KEY_RC_REMARKS = "RcRemarks";
	// TABLE GetRemarks End

	// Table DeviceVerification Starts
	protected static String TABLE_DEVICE_VERIFICATION = "deviceverify";
	protected static String KEY_DEVICE_ID = "_id";
	protected static String KEY_DEVICE_CREATED_DATE = "createddate";
	protected static String KEY_DEVICE_STATUS = "status";
	protected static String KEY_DEVICE_ServerDate = "Serverdate";
	// Table DeviceVerification End

	// Table DeviceInfo
	protected static String TABLE_DEVICEINFO = "DeviceInfo";
	protected static String KEY_DEVICEINFO_ID = "_id";
	protected static String KEY_DEVICEINFO_ECODE = "Ecode";
	protected static String KEY_DEVICEINFO_IMEINO = "IMEINo";
	protected static String KEY_DEVICEINFO_DEVICENAME = "DeviceName";
	protected static String KEY_DEVICEINFO_INTERNALSTORAGE = "InternalStorage";
	protected static String KEY_DEVICEINFO_AVAILABEL_INTERNALSTORAGE = "AvailableInternaleStorage";
	protected static String KEY_DEVICEINFO_EXTERNALSTORAGE = "ExternalStorage";
	protected static String KEY_DEVICEINFO_AVAILABEL_EXTERNALSTORAGE = "AvailableExternalStorage";
	protected static String KEY_DEVICEINFO_CREATEDDATE = "CreatedDate";
	protected static String KEY_DEVICEINFO_ServerDate = "ServerDate";
	// Table DeviceInfo
	
	// Android Log
	protected static String TABLE_AndroidLog = "AndroidLog";
	protected static String Key_L_Log_ID = "_id";
	protected static String Key_L_Ecode = "Ecode";
	protected static String Key_L_LogText = "LogText";
	protected static String Key_L_ServiceRequestTime = "ServiceRequestTime";
	protected static String Key_L_ServiceResponseTime = "ServiceResponseTime";
	protected static String Key_L_LogType = "LogType";
	protected static String Key_L_IsUpdatedOnLive = "IsUpdatedOnLive";
	protected static String Key_L_ConnectionTypeUsed = "ConnectionTypeUsed";
	protected static String Key_L_CreatedDate = "CreatedDate";
	protected static String Key_L_ServerDate = "ServerDate";
	
	//Meter Reading
	protected static String Table_MeterReading="MeterReading";
	protected static String Key_MR_ID = "_id";
	protected static String Key_MR_MeterReading = "MeterReadingText";
	protected static String Key_MR_MeterReadingImage = "MeterReadingImage";
	protected static String Key_MR_CreatedDate = "CreatedDate";
	protected static String Key_MR_IsUpdatedOnLive = "IsUpdatedOnLive";
	protected static String Key_MR_Latitude = "latitude";
	protected static String Key_MR_Longitude = "longitude";
	
	//Meter Reading
	protected static String Table_PODRemarksMaster="PODRemarksMaster";
	protected static String Key_PR_ID = "_id";
	protected static String Key_PR_PODRemarks = "PODRemarks";

	//Meter Reading
	protected static String Table_ValidationMaster="ValidationMaster";
	protected static String Key_VM_ID = "id";
	protected static String Key_VM_FieldName = "FieldName";
	protected static String Key_VM_IsValidationRequired = "IsValidationRequired";

	
	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {

			String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ Table_Login + " (" + Key_Log_login_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Key_Log_UserID
					+ " TEXT," +Key_Log_UserName+ " Text, "
					+ Key_Log_Password + " TEXT," + Key_Log_IEMINO
					+ " TEXT," + Key_Log_AppVersion + " TEXT,"
					+ Key_Log_Created_Date + " DateTime," + Key_Log_ServerDate
					+ " Text " + " )";

			String CREATE_POD_TABLE = "CREATE TABLE IF NOT EXISTS " + Table_POD
					+ " ( " + Key_Id + " Integer Primary Key AutoIncrement,"
					+ Key_Consignee + " Text, " + Key_Awbno + " TEXT,"
					+ Key_InvoiceAmount + " Text," + Key_Address + " Text,"
					+ Key_Phone + " Text," + Key_IsDownOnPDA + " Text,"
					+ Key_RunsheetDate + " Text," + Key_IsUpdate + " Integer,"
					+ Key_IsUpdatedOnLive + " Text, " + Key_P_CreatedDate
					+ " Text," + Key_P_ServerDate + " Text," + Key_Signature
					+ " Text," + Key_POD_Pic + " Text," + Key_RC_Name
					+ " Text," + Key_RC_Phone + " Text," + Key_Relation
					+ " Text," + Key_PacketStatus + " Text," + Key_Remarks
					+ " Text, " + Key_IMEINo + " Text," + Key_Payment_Type
					+ " Text," + Key_COD_Amount + " Text," + Key_Latitude
					+ " Text," + Key_Longitude + " Text," + Key_DeliveryTime
					+ " Text," + Key_IDProofType + " Text," + Key_IDProofTypeNo
					+ " Text," + Key_PODRemarks + " Text," + Key_Ecode
					+ " Text," + Key_RunsheetNo + " Text ," + Key_UD_Remarks + " Text " + ")";

			String CREATE_PKT_STATUS_DESC_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_PACKET_STATUS + " (" + KEY_PKT_STATUS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ KEY_PKT_STATUS_CODE + " TEXT, " + KEY_PKT_STATUS
					+ " TEXT" + ")";

			String CREATE_GET_RC_RELATION_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_GET_RC_RELATION + " (" + KEY_RELATION_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ KEY_RC_RELATION_CODE + " TEXT," + KEY_RC_RELATION
					+ " TEXT" + " )";

			String CREATE_RC_REMARKS = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_GET_REMARKS + " (" + KEY_REMARKS_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ KEY_RC_REMARKS_CODE + " TEXT," + KEY_RC_REMARKS + " TEXT"
					+ ")";

			String CREATE_POD_INSERT_TABLE = "Create Table If Not Exists "
					+ Table_Insert_POD + " (" + Key_Ins_ID
					+ " Integer Primary Key AutoIncrement,"
					+ Key__Ins_CONSIGNEE + " Text," + Key_Ins_AWBNo + " Text,"
					+ Key_Ins_Invoice_Amount + " Text,"
					+ Key_Ins_COD_Amount + " Text," + Key_Ins_Address
					+ " Text," + Key_Ins_Phone + " Text," + Key_PktStatus
					+ " Text," + Key_Ins_IsUpdatedOnLive + " Text,"
					+ Key_Ins_RunsheetDate + " Text," + Key_Ins_RunsheetNo
					+ " Text," + Key_Ins_CreatedDate + " Text,"
					+ Key_Ins_ServerDate + " Text" + ")";

			String CREATE_DEVICE_VERIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_DEVICE_VERIFICATION
					+ " ( "
					+ KEY_DEVICE_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ KEY_DEVICE_CREATED_DATE
					+ " TEXT,"
					+ KEY_DEVICE_STATUS
					+ " TEXT," + KEY_DEVICE_ServerDate + " Text " + ")";

			String CREATE_TABLE_DEVICEINFO = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_DEVICEINFO + "(" + KEY_DEVICEINFO_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ KEY_DEVICEINFO_ECODE + " TEXT," + KEY_DEVICEINFO_IMEINO
					+ " TEXT," + KEY_DEVICEINFO_DEVICENAME + " TEXT,"
					+ KEY_DEVICEINFO_INTERNALSTORAGE + " TEXT,"
					+ KEY_DEVICEINFO_AVAILABEL_INTERNALSTORAGE + " TEXT,"
					+ KEY_DEVICEINFO_EXTERNALSTORAGE + " TEXT,"
					+ KEY_DEVICEINFO_AVAILABEL_EXTERNALSTORAGE + " TEXT,"
					+ KEY_DEVICEINFO_CREATEDDATE + " TEXT,"
					+ KEY_DEVICEINFO_ServerDate + " Text )";

			String Create_Table_AndroidLog = "Create Table If Not Exists "
					+ TABLE_AndroidLog + " ( " + Key_L_Log_ID
					+ " Integer Primary Key AutoIncrement," + Key_L_Ecode
					+ " Text," + Key_L_LogText + " Text,"
					+ Key_L_ServiceRequestTime + " Text,"
					+ Key_L_ServiceResponseTime + " Text," + Key_L_LogType
					+ " Text," + Key_L_IsUpdatedOnLive + " Text,"
					+ Key_L_ConnectionTypeUsed + " Text," + Key_L_CreatedDate
					+ " Text," + Key_L_ServerDate + " Text ) ";
			
			String Create_Table_MeterReading = "Create Table If Not Exists "
					+ Table_MeterReading + "(" + Key_MR_ID
					+ " Integer Primary Key AutoIncrement," + Key_MR_MeterReading
					+ " Text," + Key_MR_MeterReadingImage + " Text,"
					+ Key_MR_CreatedDate + " Text,"
					+ Key_MR_IsUpdatedOnLive + " Text,"
					+ Key_MR_Latitude + " Text,"
					+ Key_MR_Longitude+ " Text )";
			
			String CREATE_RC_PODRemarksMaster = "CREATE TABLE IF NOT EXISTS "
					+ Table_PODRemarksMaster + " (" + Key_PR_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Key_PR_PODRemarks + " TEXT )";


			String CREATE_VM_ValidationMaster = "CREATE TABLE IF NOT EXISTS "
					+ Table_ValidationMaster + " (" + Key_VM_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Key_VM_FieldName + " TEXT, "
					+Key_VM_IsValidationRequired+" TEXT )";
			
			db.execSQL(CREATE_LOGIN_TABLE);
			db.execSQL(CREATE_POD_TABLE);
			db.execSQL(CREATE_PKT_STATUS_DESC_TABLE);
			db.execSQL(CREATE_GET_RC_RELATION_TABLE);
			db.execSQL(CREATE_RC_REMARKS);
			db.execSQL(CREATE_POD_INSERT_TABLE);
			db.execSQL(CREATE_DEVICE_VERIFICATION_TABLE);
			db.execSQL(CREATE_TABLE_DEVICEINFO);
			db.execSQL(Create_Table_AndroidLog);
			db.execSQL(Create_Table_MeterReading);
			db.execSQL(CREATE_RC_PODRemarksMaster);
			db.execSQL(CREATE_VM_ValidationMaster);

		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion > oldVersion) {
			try {
				//added on Version 1.0.0.2
				db.execSQL("ALTER TABLE PODDetail ADD COLUMN RcUDremarks Text ");
			} catch (Exception e) {
				Log.e("Alter Error", "" + e.getMessage());
			}
			try {
				//added on Version 1.0.0.3
				db.execSQL("ALTER TABLE LoginDetail ADD COLUMN UserName Text ");
			} catch (Exception e) {
				Log.e("Alter Error", "" + e.getMessage());
			}
		}
		onCreate(db);
	}
}

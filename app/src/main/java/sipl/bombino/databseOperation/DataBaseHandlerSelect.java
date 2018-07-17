package sipl.bombino.databseOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.AndroidLog;
import sipl.bombino.properties.MeterReadingInfo;
import sipl.bombino.properties.PODUser;
import sipl.bombino.properties.PodGetterSetter;

public class DataBaseHandlerSelect extends DataBaseHandler {

	public DataBaseHandlerSelect(Context context) {
		super(context);
	}
	
	//function to get userid
	public String getUserID(){
		String UserID="";
		String Query="Select UserID From LoginDetail  limit 1";
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			Cursor cursor=db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					UserID = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		}
		catch(Exception e){
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return UserID;
	}
	
	//function to get userpassword
	public String getUserPassword(){
		String Password="";
		String Query="Select Password From LoginDetail  limit 1";
		SQLiteDatabase db = this.getReadableDatabase();
		try{
			Cursor cursor=db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					Password = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		}
		catch(Exception e){
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return Password;
	}

	// function to get CurrentDate
	public String getCurrentDate() {
		String Date = "";
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "SELECT STRFTIME('%d-%m-%Y',DATE('now')) As CDATE";
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					Date = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error  ",e.getMessage());
		}
		return Date;
	}

	// function to get CurrentServer date from LoginTable
	public String getCurrentServerDate() {
		String Date = "";
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "Select ServerDate From LoginDetail "
				+ " Order by _id Desc Limit 1";
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					Date = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error  ",e.getMessage());
		}
		return Date;
	}

	// funtion to check duplicate awbno. if exist return true
	public boolean checkAwbNoForDuplication(String AwbNo) {
		SQLiteDatabase db= null;
		boolean flag = false;
		String Query = "Select * from PodDetail Where AwbNo='" + AwbNo
				+ "' And runsheetdate='"+getCurrentServerDate()+"'";
		db= this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.getCount() > 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error",e.getMessage());
		}
		return flag;
	}

	/*
	 * public List<PodGetterSetter> getAllContacts() { List<PodGetterSetter>
	 * podStatusList = new ArrayList<PodGetterSetter>(); SQLiteDatabase db =
	 * this.getReadableDatabase(); try { String SelectQuery = "SELECT * FROM " +
	 * TABLE_POD; Cursor cursor = db.rawQuery(SelectQuery, null); if
	 * (cursor.moveToFirst()) { do { PodGetterSetter podStatusObj = new
	 * PodGetterSetter(); podStatusObj.setAwbNo(cursor.getString(0));
	 * podStatusObj.setConsignee(cursor.getString(1));
	 * podStatusObj.setDepartmentcode(cursor.getString(2));
	 * podStatusObj.setEcode(cursor.getString(3));
	 * podStatusObj.setEname(cursor.getString(4));
	 * podStatusObj.setIBCode(cursor.getString(5));
	 * podStatusObj.setIMEINo(cursor.getString(6));
	 * podStatusObj.setUserpass(cursor.getString(7));
	 * podStatusList.add(podStatusObj); } while (cursor.moveToNext()); }
	 * db.close(); } catch (Exception e) { db.close();
	 * Log.i("Page: DataBaseHandler : Method: getAllContacts", e.getMessage());
	 * } return podStatusList; }
	 */
	
	public boolean CheckVersion(String AppVersion) {
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "Select AppVersion From LoginDetail Order by _id Desc Limit 1";
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					if (AppVersion.equalsIgnoreCase(cursor.getString(0)))
						flag = true;
					else
						flag = false;
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return flag;
	}

	public List<PodGetterSetter> getPodInsertedResultForUpdateInLive(String AwbNo) {
		List<PodGetterSetter> podInsertedList = new ArrayList<PodGetterSetter>();

		SQLiteDatabase db = null;
		try {
			String SelectQuery = " SELECT _id, IFNULL(signature,'') AS signature,IFNULL(podpic,'') AS podpic "
					+ ",awbno,IFNULL(pktstatus,'') AS pktstatus ,IFNULL(codamount,'0') AS codamount,ecode,IFNULL(rcname,'') AS rcname"
					+ ",IFNULL(rcrelation,'') AS rcrelation,IfNull(deliverytime,'') As deliveryTime,IfNull(paymenttype,'') As Paymenttype,"
					+ "IfNull(latitude,'') as latitude,IfNull(longitude,'') As longitude,IFNULL(rcphone,'') AS rcphone,IFNULL(rcremarks,'') rcremarks,"
					+ "IFNULL(imeino,'') As imeino,IFNULL(IDProofType,'') AS IDProofType ,IFNULL(IDProofTypeNo,'' ) AS IDProofTypeNo,"
					+ " IFNULL(PodRemarks,'') As PodRemarks , IFNULL(RcUDremarks,'') As RcUDremarks "
					+ "FROM PODDetail Where IfNull(IsUpdatedOnLive,'0')=1 And awbno='"
					+ AwbNo
					+ "' and ServerDate='"
					+ getCurrentServerDate()
					+ "' " +" And runsheetdate='"
					+ getCurrentServerDate() + "'";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					PodGetterSetter podInsertObj = new PodGetterSetter();
					podInsertObj.setId(Integer.parseInt(cursor.getString(0)));
					podInsertObj.setSignature(cursor.getString(1));
					podInsertObj.setPODPic(cursor.getString(2));
					podInsertObj.setAwbNo(cursor.getString(3));
					podInsertObj.setPktStatus(cursor.getString(4));
					podInsertObj.setCODAmount(cursor.getString(5));
					podInsertObj.setECode(cursor.getString(6));
					podInsertObj.setRCName(cursor.getString(7));
					podInsertObj.setRCRelation(cursor.getString(8));
					podInsertObj.setDeliveryTime(cursor.getString(9));
					podInsertObj.setPaymentType(cursor.getString(10));
					podInsertObj.setLatitude(cursor.getString(11));
					podInsertObj.setLongitude(cursor.getString(12));
					podInsertObj.setRCPhone(cursor.getString(13));
					podInsertObj.setRCRemarks(cursor.getString(14));
					podInsertObj.setIMEINo(cursor.getString(15));
					podInsertObj.setIDProofType(cursor.getString(16));
					podInsertObj.setIDProofNo(cursor.getString(17));
					podInsertObj.setPODRemarks(cursor.getString(18));
					podInsertObj.setUDRemarks(cursor.getString(19));
					podInsertedList.add(podInsertObj);
				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return podInsertedList;
	}

	public List<PodGetterSetter> getPodInsertedResultForUpdateInLive() {
		List<PodGetterSetter> podInsertedList = new ArrayList<PodGetterSetter>();
		SQLiteDatabase db = null;
		try {
			String SelectQuery = " SELECT _id, IFNULL(signature,'') AS signature,IFNULL(podpic,'') AS podpic "
					+ ",awbno,IFNULL(pktstatus,'') AS pktstatus ,IFNULL(codamount,'0') AS codamount,ecode,IFNULL(rcname,'') AS rcname"
					+ ",IFNULL(rcrelation,'') AS rcrelation,IfNull(deliverytime,'') As deliveryTime,IfNull(paymenttype,'') As Paymenttype,"
					+ "IfNull(latitude,'') as latitude,IfNull(longitude,'') As longitude,IFNULL(rcphone,'') AS rcphone,IFNULL(rcremarks,'') rcremarks,"
					+ "IFNULL(imeino,'') As imeino,IFNULL(IDProofType,'') AS IDProofType ,IFNULL(IDProofTypeNo,'' ) AS IDProofTypeNo,"
					+ " IFNULL(PodRemarks,'') As PodRemarks , IFNULL(RcUDremarks,'') As RcUDremarks "
					+ " FROM PODDetail Where IfNull(IsUpdate,'0')=1 And IfNull(isUpdatedOnLive,'0')='0' and ServerDate='"
					+ getCurrentServerDate()
					+ "' And runsheetdate='"
					+ getCurrentServerDate() + "'";
			db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					PodGetterSetter podInsertObj = new PodGetterSetter();
					podInsertObj.setId(Integer.parseInt(cursor.getString(0)));
					podInsertObj.setSignature(cursor.getString(1));
					podInsertObj.setPODPic(cursor.getString(2));
					podInsertObj.setAwbNo(cursor.getString(3));
					podInsertObj.setPktStatus(cursor.getString(4));
					podInsertObj.setCODAmount(cursor.getString(5));
					podInsertObj.setECode(cursor.getString(6));
					podInsertObj.setRCName(cursor.getString(7));
					podInsertObj.setRCRelation(cursor.getString(8));
					podInsertObj.setDeliveryTime(cursor.getString(9));
					podInsertObj.setPaymentType(cursor.getString(10));
					podInsertObj.setLatitude(cursor.getString(11));
					podInsertObj.setLongitude(cursor.getString(12));
					podInsertObj.setRCPhone(cursor.getString(13));
					podInsertObj.setRCRemarks(cursor.getString(14));
					podInsertObj.setIMEINo(cursor.getString(15));
					podInsertObj.setIDProofType(cursor.getString(16));
					podInsertObj.setIDProofNo(cursor.getString(17));
					podInsertObj.setPODRemarks(cursor.getString(18));
					podInsertObj.setUDRemarks(cursor.getString(19));
					podInsertedList.add(podInsertObj);
				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return podInsertedList;
	}

	// function to check table record
	public int getRecordCount(String TABLE_NAME) {
		int cnt = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String countQuery = "SELECT  * FROM " + TABLE_NAME;
			Cursor cursor = db.rawQuery(countQuery, null);
			cnt = cursor.getCount();
			cursor.close();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return cnt;
	}

	// function to get all awbno's
	public String getIBCODE() {
		String TempAwbNo = "", AwbNo = "";
		try {
			String SqlSelectPodAwbNo = "SELECT " + Key_Awbno + " FROM "
					+ Table_POD + "";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(SqlSelectPodAwbNo, null);
			if (cursor.moveToFirst()) {
				do {
					TempAwbNo = cursor.getString(0);
					if (AwbNo.equals("")) {
						AwbNo = "'" + TempAwbNo + "'";
					} else {
						AwbNo += ",'" + TempAwbNo + "'";
					}
				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return AwbNo;
	}

	// function to get PODList pending
	public List<PodGetterSetter> GetPODList() {
		List<PodGetterSetter> podlist = new ArrayList<PodGetterSetter>();
		String Qstr = "";
		Qstr = "SELECT consignee,awbno,address,phone,invoiceamount,IsUpdatedOnLive,RunsheetNo,Runsheetdate ,IfNull(rcname,'') As RCName FROM "
				+ Table_POD + " WHERE isupdate=0 Order by awbno";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(Qstr, null);
		if (cursor.moveToFirst()) {
			do {
				PodGetterSetter pod = new PodGetterSetter();
				pod.setConsignee(cursor.getString(0));
				pod.setAwbNo(cursor.getString(1));
				pod.setAddress(cursor.getString(2));
				pod.setPhone(cursor.getString(3));
				pod.setInvoiceAmount(cursor.getString(4));
				pod.setIsUpdatedOnLive(cursor.getString(5));
				pod.setRunsheetNo(cursor.getString(6));
				pod.setRunsheetDate(cursor.getString(7));
				pod.setRCName(cursor.getString(8));
				podlist.add(pod);
			} while (cursor.moveToNext());
			db.close();
		}
		return podlist;
	}
	
	// function to get PODList pending on behalf of AwbNo
	public List<PodGetterSetter> GetPODListAwbNo(String AwbNo) {
		List<PodGetterSetter> podlist = new ArrayList<PodGetterSetter>();
		String Qstr = "";
		Qstr = "SELECT consignee,awbno,address,phone,invoiceamount,IsUpdatedOnLive,RunsheetNo,Runsheetdate ,IfNull(rcname,'') As RCName FROM "
				+ Table_POD + " WHERE IsUpdate=0 And awbno='"+AwbNo+"' Order by awbno";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(Qstr, null);
		if (cursor.moveToFirst()) {
			do {
				PodGetterSetter pod = new PodGetterSetter();
				pod.setConsignee(cursor.getString(0));
				pod.setAwbNo(cursor.getString(1));
				pod.setAddress(cursor.getString(2));
				pod.setPhone(cursor.getString(3));
				pod.setInvoiceAmount(cursor.getString(4));
				pod.setIsUpdatedOnLive(cursor.getString(5));
				pod.setRunsheetNo(cursor.getString(6));
				pod.setRunsheetDate(cursor.getString(7));
				pod.setRCName(cursor.getString(8));
				podlist.add(pod);
			} while (cursor.moveToNext());
			db.close();
		}
		return podlist;
	}

	// function to get PODList Delivered
	public List<PodGetterSetter> GetPODListDelivered(String PktStatus) {
		List<PodGetterSetter> podlist = new ArrayList<PodGetterSetter>();
		String Qstr = "";
		Qstr = "SELECT Consignee,IfNull(Awbno,'') As Awbno, IfNull(Address,'') As Address,IfNull(Phone,'') As Phone,"
				+ "IfNull(CodAmount,'') As CodAmount,IfNull(IsUpdatedOnLive,'') As IsUpdatedOnLive,IfNull(RunsheetNo,'') as RunsheetNo"
				+ ",IfNull(Runsheetdate,'') As Runsheetdate,IfNull(InvoiceAmount,'') As InvoiceAmount,IfNull(CreatedDate,'') As CreatedDate,IfNull(ServerDate,'') As ServerDate"
				+ " ,IfNull(rcname,'') As RcName , IfNull(RcUDremarks,'') As UnDeliveredRemarks FROM "
				+ Table_POD
				+ " WHERE IsUpdate=1 And pktstatus='"+ PktStatus + "' Order By IsUpdatedOnLive";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(Qstr, null);
		if (cursor.moveToFirst()) {
			do {
				PodGetterSetter pod = new PodGetterSetter();
				pod.setConsignee(cursor.getString(0));
				pod.setAwbNo(cursor.getString(1));
				pod.setAddress(cursor.getString(2));
				pod.setPhone(cursor.getString(3));
				pod.setCODAmount(cursor.getString(4));
				pod.setIsUpdatedOnLive(cursor.getString(5));
				pod.setRunsheetNo(cursor.getString(6));
				pod.setRunsheetDate(cursor.getString(7));
				pod.setInvoiceAmount(cursor.getString(8));
				pod.setCreatedDate(cursor.getString(9));
				pod.setServerdate(cursor.getString(10));
				pod.setRCName(cursor.getString(11));
				pod.setPODRemarks(cursor.getString(12));
				podlist.add(pod);
			} while (cursor.moveToNext());
			db.close();
		}
		return podlist;
	}
	
	// function to get PODList Delivered on behalf of awbno
		public List<PodGetterSetter> GetPODListDeliveredOnBehalfOfAwbNo(String awbNo) {
			List<PodGetterSetter> podlist = new ArrayList<PodGetterSetter>();
			String Qstr = "";
			Qstr = "SELECT Consignee,IfNull(Awbno,'') As Awbno, IfNull(Address,'') As Address,IfNull(Phone,'') As Phone,"
					+ "IfNull(CodAmount,'') As CodAmount,IfNull(IsUpdatedOnLive,'') As IsUpdatedOnLive,IfNull(RunsheetNo,'') as RunsheetNo"
					+ ",IfNull(Runsheetdate,'') As Runsheetdate,IfNull(InvoiceAmount,'') As InvoiceAmount,IfNull(CreatedDate,'') As CreatedDate,IfNull(ServerDate,'') As ServerDate"
					+ " , ifNull(rcname,'') As RcName FROM "
					+ Table_POD
					+ " WHERE IsUpdate=1 And Awbno='"+ awbNo + "' Order By IsUpdatedOnLive";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(Qstr, null);
			if (cursor.moveToFirst()) {
				do {
					PodGetterSetter pod = new PodGetterSetter();
					pod.setConsignee(cursor.getString(0));
					pod.setAwbNo(cursor.getString(1));
					pod.setAddress(cursor.getString(2));
					pod.setPhone(cursor.getString(3));
					pod.setCODAmount(cursor.getString(4));
					pod.setIsUpdatedOnLive(cursor.getString(5));
					pod.setRunsheetNo(cursor.getString(6));
					pod.setRunsheetDate(cursor.getString(7));
					pod.setInvoiceAmount(cursor.getString(8));
					pod.setCreatedDate(cursor.getString(9));
					pod.setServerdate(cursor.getString(10));
					pod.setRCName(cursor.getString(11));
					podlist.add(pod);
				} while (cursor.moveToNext());
				db.close();
			}
			return podlist;
		}

	// function to validate user
	public List<PODUser> ValidateUser(String UserId, String Password) {
		List<PODUser> userlist = new ArrayList<PODUser>();
		String Qstr = "Select  UserID,Password From " + Table_Login;
		Qstr += " where UserID='" + UserId + "' and Password='" + Password
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(Qstr, null);
		if (cursor.moveToFirst()) {
			do {
				PODUser user = new PODUser();
				user.setUserID(cursor.getString(0));
				user.setPassword(cursor.getString(1));
				userlist.add(user);
			} while (cursor.moveToNext());
			db.close();
		}
		return userlist;
	}

	// For Getting Total/Delivered Packets

	public String getDeliveredPacketStatus() {
		String tot = "", deli = "";
		try {
			deli = "SELECT " + Key_Ins_ID + " FROM " + Table_POD + ""
					+ " WHERE " + Key_IsUpdate + "=1 And "
					+Key_RunsheetDate+"='"+getCurrentServerDate()+"'" ;
			tot = "SELECT " + Key_Ins_ID + " FROM " + Table_POD + " Where "
					+Key_RunsheetDate+"='"+getCurrentServerDate()+"'";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor curtot = db.rawQuery(tot, null);
			Cursor curdeli = db.rawQuery(deli, null);
			tot = Integer.toString(curtot.getCount());
			deli = Integer.toString(curdeli.getCount());
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
			Log.e("Error ",e.getMessage());
		}
		return deli + "/" + tot;
	}

	// Function To Get PacketStatus On Entry Form
	public List<String> GetPacketStatus() {
		List<String> pktStatusList = new ArrayList<String>();
		String pktStatus = "";
		String Qstr = "SELECT " + KEY_PKT_STATUS + " FROM "+ TABLE_PACKET_STATUS + "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(Qstr, null);
		//pktStatusList.add("--Select--");
		if (cursor.moveToFirst()) {
			do {
				pktStatus = cursor.getString(0);
				pktStatusList.add(pktStatus);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return pktStatusList;
	}

	// Function To Get RCRelation On Entry Form
	public List<String> GetRCRelation() {
		List<String> rcRelationList = new ArrayList<String>();
		String rcRelation = "";
		String Qstr = "SELECT " + KEY_RC_RELATION + " FROM "+ TABLE_GET_RC_RELATION + "";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(Qstr, null);
		if (cursor.moveToFirst()) {
			do {
				rcRelation = cursor.getString(0);
				rcRelationList.add(rcRelation);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return rcRelationList;
	}

	// Function To Get RCRemarks On Entry Form
	public List<String> GetRCRemarks() {
		List<String> rcRemarksList = new ArrayList<String>();
		String rcRemarksCode = "", rcRemarks = "";
		String Qstr = "SELECT " + KEY_RC_REMARKS_CODE + "," + KEY_RC_REMARKS
				+ " FROM " + TABLE_GET_REMARKS + " Order by " + KEY_RC_REMARKS
				+ " ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(Qstr, null);
		rcRemarksList.add("Select Receiver Remarks");
		if (cursor.moveToFirst()) {
			do {
				rcRemarksCode = cursor.getString(0);
				rcRemarks = cursor.getString(1);
				rcRemarksList.add(rcRemarks + " : " + rcRemarksCode);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return rcRemarksList;
	}
	
	
	// Function To Get RCRemarks On Entry Form
		public List<String> GetPODRemarksMaster() {
			List<String> PODRemarksList = new ArrayList<String>();
			String Qstr = "SELECT " + Key_PR_PODRemarks 
					+ " FROM " + Table_PODRemarksMaster 
					+ " ";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(Qstr, null);
			PODRemarksList.add("Select Negative Remarks");
			if (cursor.moveToFirst()) {
				do {
					PODRemarksList.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return PODRemarksList;
		}

	// function to check if PODDetail is empty
	public boolean IsRecordExistsInPodTable() {
		DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String SelectQuery = "SELECT " + Key_Id + " FROM " + Table_POD
					+ " WHERE " + Key_RunsheetDate + "='"
					+ dateformat.format(date) + "'";

			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.getCount() > 0) {
				flag = true;
			} else {
				flag = false;
			}
			db.close();
			cursor.close();
		} catch (Exception e) {
			db.close();
			flag = false;
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return flag;
	}

	// this function will return true or false based on device id whether device
	// is authenticated or not
	public boolean GetStatusForDeviceID() {
		DateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		boolean flag = false;
		String Result;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String SelectQuery = "SELECT " + KEY_DEVICE_STATUS + " FROM "
					+ TABLE_DEVICE_VERIFICATION + " WHERE "
					+ KEY_DEVICE_CREATED_DATE + "='" + dateformat.format(date)
					+ "'";
			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Result = cursor.getString(0);
					if (Result.equalsIgnoreCase("False")) {
						flag = false;
					} else {
						flag = true;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			flag = false;
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return flag;
	}

	// function to get Ecode
	public String GetEcode() {
		String Ecode = "";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String SelectQuery = "SELECT " + Key_Log_UserID + " FROM "
					+ Table_Login + ";";
			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Ecode = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return Ecode;
	}
	public String GetUserName() {
		String Ecode = "";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			String SelectQuery = "SELECT " + Key_Log_UserName + " FROM "
					+ Table_Login + ";";
			Cursor cursor = db.rawQuery(SelectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Ecode = cursor.getString(0);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return Ecode;
	}

	// function to check logintable to Check whether proceed
	// further if there is no internet connection
	public boolean funCheckLoginTable(String CurrentDate) {
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor;
			String Query = "";
			Query = "SELECT COUNT (_id) FROM LoginDetail WHERE"
					+ " ServerDate= '" + CurrentDate + "' ";
			cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					if (Integer.parseInt(cursor.getString(0)) > 0) {
						flag = true;
					} else {
						flag = false;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				e.printStackTrace();
		}
		return flag;
	}

	public boolean CheckAwbStatus(String awbNo2) {
		boolean flag = false;
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "select " + Key_Awbno + " from  " + Table_POD
				+ " Where " + Key_Awbno + "='" + awbNo2.trim() + "'";
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.getCount() > 0)
				flag = true;
			else
				flag = false;
			db.close();
			cursor.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				e.printStackTrace();
			db.close();
		}
		return flag;
	}

	// get all android Log
	public List<AndroidLog> getAndroidLog() {
		List<AndroidLog> list = new ArrayList<AndroidLog>();
		AndroidLog info;
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "Select LogText,ServiceRequestTime,ServiceResponseTime,LogType,ConnectionTypeUsed,CreatedDate"
				+ " From AndroidLog Where CreatedDate=strftime('%d-%m-%Y',date('now')) Order by _id desc";
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					info = new AndroidLog();
					info.setLogText(cursor.getString(0));
					info.setServiceRequestTime(cursor.getString(1));
					info.setServiceResponseTime(cursor.getString(2));
					info.setLogType(cursor.getString(3));
					info.setConnectionUsed(cursor.getString(4));
					info.setCreatedDate(cursor.getString(5));
					list.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
			db.close();
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return list;
	}

	// get total android Log
	public int getLogCount() {
		int CountLog = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		String QueryLog = "Select * From AndroidLog";
		try {
			Cursor cursor = db.rawQuery(QueryLog, null);
			CountLog = cursor.getCount();
			cursor.close();
			db.close();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return CountLog;
	}

	// get all android Log to update on live
	public List<AndroidLog> getAndroidLogToUpdate() {
		List<AndroidLog> list = new ArrayList<AndroidLog>();
		AndroidLog info;
		String Query = "Select Ecode, LogText,ServiceRequestTime,ServiceResponseTime,LogType,ConnectionTypeUsed,CreatedDate"
				+ " From AndroidLog Where CreatedDate=strftime('%d-%m-%Y',date('now')) And IfNull(isUpdatedOnLive,0)=0";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					info = new AndroidLog();
					info.setEcode(cursor.getString(0));
					info.setLogText(cursor.getString(1));
					info.setServiceRequestTime(cursor.getString(2));
					info.setServiceResponseTime(cursor.getString(3));
					info.setLogType(cursor.getString(4));
					info.setConnectionUsed(cursor.getString(5));
					info.setCreatedDate(cursor.getString(6));
					list.add(info);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return list;
	}
	
	// get all android Log to update on live
	public List<MeterReadingInfo> getMeterReadingList() {
		List<MeterReadingInfo> list = new ArrayList<MeterReadingInfo>();
		MeterReadingInfo info;
		String Query = "Select _id,MeterReadingText,MeterReadingImage" +
				",IfNull(IsUpdatedOnLive,'0') As IsUpdatedOnLive,IfNull(latitude,'') As Latitude," +
				" IfNull(longitude,'') As Longitude From MeterReading Order by IfNull(IsUpdatedOnLive,0)";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					info = new MeterReadingInfo();
					info.setId(cursor.getString(0));
					info.setMeterReadingText(cursor.getString(1));
					info.setMeterReadingImage(cursor.getString(2));
					info.setIsUpdatedOnLive(cursor.getString(3));
					info.setLatitude(cursor.getString(4));
					info.setLongitude(cursor.getString(5));
					list.add(info);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return list;
	}
	
	//get particular image on behalf of meter id
	public String getMeterImage(String meterID) {
		String meterImage="";
		String Query = "Select MeterReadingImage From MeterReading Where _id="+meterID;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(Query, null);
			if (cursor.moveToFirst()) {
				do {
					meterImage=cursor.getString(0);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
		return meterImage;
	}

	public boolean checkForValidation(String columnName){
		String query="Select IsValidationRequired From ValidationMaster Where FieldName='"+columnName+"'";
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					if(cursor.getInt(0)==1){
						return true;
					}else {
						return false;
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
			return false;
		}
		return  false;
	}

}

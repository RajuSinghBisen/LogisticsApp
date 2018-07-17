package sipl.bombino.commonfillfunction;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.configuration.ApplicationConfiguration;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.databseOperation.DataBaseHandlerUpdate;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.GetRcRelationGetterSetter;
import sipl.bombino.properties.GetRemarksGetterSetter;
import sipl.bombino.properties.PODRemarksMasterInfo;
import sipl.bombino.properties.PacketStatusGetterSetter;
import sipl.bombino.properties.PodGetterSetter;

public class FillRecordsInLocalDatabase {

	DataBaseHandlerInsert DBObjIns;
	DataBaseHandlerSelect DBObjSel;
	DataBaseHandlerUpdate DBObjUpd;
	DataBaseHandlerDelete DBObjDel;
	String JSONResponse = "";
	Context context;
	ApplicationConfiguration appConfig;

	public FillRecordsInLocalDatabase() {
	}

	public FillRecordsInLocalDatabase(Context context) {
		this.context = context;
		DBObjIns = new DataBaseHandlerInsert(context);
		DBObjSel = new DataBaseHandlerSelect(context);
		DBObjUpd=new DataBaseHandlerUpdate(context);
		DBObjDel=new DataBaseHandlerDelete(context);
		appConfig = new ApplicationConfiguration();
	}

	// getting all pod details on behalf of IMEIno whose
	// isdownonpda=0
	public void funDownPodList() {
		JSONResponse="";
		JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_GetPacketsOnIEMI", new String[] { "@IEMINo" }, new String[] { getDeviceId() }, null, null, null, "Request", "", null,context);
		funcGetPod(JSONResponse);
		funUpdateIsDownPDA();
	}

	// function to down Packet Status
	public void funDownPacketStatus(boolean deleteOldRecord) {
		JSONResponse="";
		JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_GetPktStatus", null, null, null, null, null, "Request", "", null,context);
		funcGetPacketStatus(JSONResponse, deleteOldRecord);
	}

	// function to down RcRelation
	public void funDownRcRelation(boolean deleteOldRecord) {
		JSONResponse="";
		JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_GetRCrelation", null, null, null, null, null, "Request", "", null,context);
		funcGetRelation(JSONResponse, deleteOldRecord);
	}

	// function to down RcRemarks
	public void funDownRcRemarks(boolean deleteOldRecord) {
		JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_GetRCRemarks", null, null, null, null, null, "Request", "", null,context);
		funcGetRemarks(JSONResponse, deleteOldRecord);
	}
	
	// function to down RcRemarks
	public void funDownPODRemarksMaster(boolean deleteOldRecord) {
		JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_GetPODRemarksMaster", null, null, null, null, null, "Request", "", null,context);
		funcGetPODRemarksMaster(JSONResponse,deleteOldRecord);
	}

	// function to update Table IbRunsheetPacket's Column isDownPDA to 1 in Live
	// Database
	public void funUpdateIsDownPDA() {
		String IBCODEList = DBObjSel.getIBCODE();
		if (IBCODEList != "") {
			ServiceRequestResponse.getJSONString("Sp_Android_UpdateRunsheetToOne", new String[] { "@Awbno", "@Ecode" }, new String[] {IBCODEList, DBObjSel.GetEcode() }, null, null, null, "Request", "", null,context);
		}
	}

	// function to update logintable
	public void funUpdateloginTable() {
		JSONResponse = ServiceRequestResponse.getJSONString("Sp_Android_GetServerDate", null, null, null, null, null, "Request", "", null,context);
		funcUpdateServerDate(JSONResponse);
		DBObjDel.deleteOldPodDetailTable(DBObjSel.getCurrentServerDate());
	}
	

	// function to get Login Details Through IMEI no
	public void funcUpdateServerDate(String jsonData) {
		if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonData);
			if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
				return;
			}
			JSONObject jsonNode=jsonArr.getJSONObject(0);
			DBObjUpd.updateLoginServerDate(jsonNode.getString("ServerDate"));		
		} catch (JSONException e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// function to get all pod data on behalf to IMEI no
	public void funcGetPod(String jsonData) {
		if(null==jsonData){
			return;
		}
		if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonData);
			if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
				return;
			}
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonNode = jsonArr.getJSONObject(i);
				if (DBObjSel.checkAwbNoForDuplication(jsonNode.getString("AwbNo")) == false) {
					DBObjIns.addRecordIntoPODdetail(new PodGetterSetter(
							jsonNode.getString("Consignee")
							,jsonNode.getString("AwbNo")
							,jsonNode.getString("Invoice_Value")
							,jsonNode.getString("Address")
							,jsonNode.getString("Phone")
							,jsonNode.getString("RunsheetDate")
							,jsonNode.getString("RunSheet")
							,jsonNode.getString("IsDownonPDA")
							,DBObjSel.getCurrentDate()
							,jsonNode.getString("ServerDate")));
				}
			}
			
		} catch (JSONException e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}	
	}

	// function to get all packet status like DELIVERED,LOST etc
	public void funcGetPacketStatus(String jsonData,boolean deleteOldRecord) {
		List<PacketStatusGetterSetter> list=new ArrayList<PacketStatusGetterSetter>();
		if(null==jsonData){
			return;
		}
		if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonData);
			if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
				return;
			}
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonNode = jsonArr.getJSONObject(i);
				list.add(new PacketStatusGetterSetter(jsonNode.getString("PacketStatusCode"),jsonNode.getString("PacketStatus")));
			}
			if(list.size()>0){
				if(deleteOldRecord)
					DBObjDel.deleteTableRecord("PacketStatus");
				DBObjIns.addRecordIntoPacketStatusTable(list);
			}
			
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}

	// function to get all relation
	public void funcGetRelation(String jsonData,boolean deleteOldRecord) {
		List<GetRcRelationGetterSetter> list=new ArrayList<GetRcRelationGetterSetter>();
		if(null==jsonData){
			return;
		}
		if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonData);
			if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
				return;
			}
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonNode = jsonArr.getJSONObject(i);
				list.add(new GetRcRelationGetterSetter(jsonNode.getString("RcRelationCode"), jsonNode.getString("RcRelation")));
			}
			if(list.size()>0){
				if(deleteOldRecord)
					DBObjDel.deleteTableRecord("RcRelation");
				DBObjIns.addRecordIntoRcRelationTable(list);
			}
		} catch (Exception e) {
			if (CommonFunction.LOG)
				Log.e("Error ", e.getMessage());
		}
	}

	// function to get all remarks
	public void funcGetRemarks(String jsonData,boolean deleteOldRecord) {
		List<GetRemarksGetterSetter> list=new ArrayList<GetRemarksGetterSetter>();
		if(null==jsonData){
			return;
		}
		if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
			return;
		}
		try {
			JSONArray jsonArr = new JSONArray(jsonData);
			if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
				return;
			}
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonNode = jsonArr.getJSONObject(i);
				list.add(new GetRemarksGetterSetter(jsonNode.getString("RcRemarksCode"), jsonNode.getString("RcRemarks")));
			}
			if(list.size()>0){
				if(deleteOldRecord)
					DBObjDel.deleteTableRecord("RcRemarks");
				DBObjIns.addRecordIntoRemarksTable(list);
			}
			
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
	}
	
	// function to get all PODRemarksMaster
		public void funcGetPODRemarksMaster(String jsonData,boolean deleteOldRecord) {
			List<PODRemarksMasterInfo> list=new ArrayList<PODRemarksMasterInfo>();
			if(null==jsonData){
				return;
			}
			if (jsonData.equals("")||!CommonFunction.IsJSONValid(jsonData)) {
				return;
			}
			try {
				JSONArray jsonArr = new JSONArray(jsonData);
				if (jsonArr.length() == 0||jsonArr.getJSONObject(0).has("Error")) {
					return;
				}
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject jsonNode = jsonArr.getJSONObject(i);
					list.add(new PODRemarksMasterInfo(jsonNode.getString("PODRemarks")));
				}
				if(list.size()>0){
					if(deleteOldRecord)
						DBObjDel.deleteTableRecord("PODRemarksMaster");
					DBObjIns.addRecordIntoPODRemarksMaster(list);
				}
				
			} catch (Exception e) {
				if(CommonFunction.LOG)
					Log.e("Error ",e.getMessage());
			}
		}

	// function to get Current Version From Live Server
	public String getCurrentRunningVersionFromLive() {
		String JsonStr = "", CurrentLiveVersion = "";
		try {
			JsonStr = ServiceRequestResponse.getJSONString("Sp_Android_GetCurrentAndroidVersion", null, null, null, null, null, "Request", "", null,context);
			if(null!=JsonStr)
			if (!JsonStr.equals("")) {
				JSONArray jsonArr;
				try {
					jsonArr = new JSONArray(JsonStr);
					if (jsonArr.length() != 0) {
						JSONObject jsonNode = jsonArr.getJSONObject(0);
						if (!jsonNode.has("Error")) {
							CurrentLiveVersion = jsonNode.getString("AndroidVersion");
						}
					}
				} catch (JSONException e) {
					if(CommonFunction.LOG)
						Log.e("Error ",e.getMessage());
				}
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return CurrentLiveVersion;
	}

	// function to get DeviceID
	public String getDeviceId() {
		String IMEINo = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			IMEINo = telephonyManager.getDeviceId();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.e("Error ",e.getMessage());
		}
		return IMEINo;
	}
}

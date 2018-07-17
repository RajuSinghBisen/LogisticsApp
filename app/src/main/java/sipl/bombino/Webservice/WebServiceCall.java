package sipl.bombino.Webservice;

import android.content.Context;
import android.util.Log;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import sipl.bombino.configuration.ApplicationConfiguration;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.Connectivity;
import sipl.bombino.properties.AndroidLog;

public class WebServiceCall {

	private static String 
	NAMESPACE = ApplicationConfiguration.GetNameSpace()
	,URL = ApplicationConfiguration.GetURL()
	,SOAP_ACTION =ApplicationConfiguration.GetNameSpace()
	,JSONRESPONSE = "";
	
	static long StartTime = 0, EndTime = 0;
	static String RequestTime="",ResponseTime="";
	AndroidLog logInfo;

	// Dynamic WebMethod Without ParameterKey and ParameterValue
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName,String logType,Context context) {
		try {
			JSONRESPONSE = WebServiceOperation(MethodName, Token, CCode,ProcedureName, new String[] {}
			, new String[] {},new String[] {}, new String[] {}, "", "", "", "", "",logType,"",context);
		} catch (Exception e) {
			Log.i("DynamicWebMethod", e.getMessage());
		}
		return JSONRESPONSE;
	}

	// Dynamic WebMethod with ParameterKey and ParameteValue
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName, String[] ParamKey,
			String[] ParamValue,String logType,Context context) {
		try {
			JSONRESPONSE = WebServiceOperation(MethodName, Token, CCode,ProcedureName
					, ParamKey, ParamValue, new String[] {},new String[] {}, "", "", "", "", "",logType,"",context);
		} catch (Exception e) {
			Log.i("DynamicWebMethod", e.getMessage());
		}
		return JSONRESPONSE;
	}

	// Dynamic WebMethod with ParameterKey and ParameteValue and MobileNo and
	// Message
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName, String[] ParamKey,
			String[] ParamValue, String MobileNo, String Msg,String logType,Context context) {
		try {
			JSONRESPONSE = WebServiceOperation(MethodName, Token,CCode,ProcedureName, ParamKey, ParamValue, 
					new String[] {},new String[] {}, MobileNo, Msg, "", "", "",logType,"", context);
		} catch (Exception e) {
			Log.i("DynamicWebMethod", e.getMessage());
		}
		return JSONRESPONSE;
	}

	// Dynamic WebMethod with UserID,UserPass and IMEINO
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName, String[] ParamKey,
			String[] ParamValue, String UserID, String UserPass, String IMEINo,String logType,String Appversion,Context context) {
		try {
			JSONRESPONSE = WebServiceOperation(MethodName, Token, CCode,ProcedureName,
					new String[] {}, new String[] {},new String[] {}, new String[] {}, "", "", UserID, UserPass,IMEINo,logType,Appversion,context);
		} catch (Exception e) {
			Log.i("DynamicWebMethod", e.getMessage());
		}
		return JSONRESPONSE;
	}

	// Dynamic WebMethod with
	// ParameterKey,ParameterKeyValue,ParameterImageKey,ParameterImageKeyValue
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName, String[] ParamKey,
			String[] ParamValue, String[] ParamImageKey,
			String[] ParamImageValue,String logType,Context context) {
		JSONRESPONSE = WebServiceOperation(MethodName, Token, CCode,
				ProcedureName, ParamKey, ParamValue, ParamImageKey,
				ParamImageValue, "", "", "", "", "",logType,"", context);

		return JSONRESPONSE;
	}
	// Dynamic webmethod for password change
	public static String DynamicWebMethod(String MethodName, String Token,
			String CCode, String ProcedureName,String IMEINo,String CurrentPassword,String logType,Context context){
		JSONRESPONSE=WebServiceOperation(MethodName, Token, CCode, ProcedureName, 
				new String[] {}, new String[] {}, new String[] {}, new String[] {}, "", "", "",
				CurrentPassword, IMEINo, logType, "", context);
		return JSONRESPONSE;
	}

	// Common Operation
	private static String WebServiceOperation( String MethodName, String Token,
			String CCode, String ProcedureName, String[] ParamKey,
			String[] ParamValue, String[] ParamImageKey,
			String[] ParamImageValue, String MobileNo, String Msg,
			String UserID, String UserPass, String IMEINo,String logType,String AppVersion, Context context) {
		try {
			SoapObject request = new SoapObject(NAMESPACE, MethodName);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;

			// Adding Token Value
			PropertyInfo info = new PropertyInfo();
			info.setName("Token");
			info.setValue(Token);
			info.setType(String.class);
			request.addProperty(info);

			// Adding CCode Value
			info = new PropertyInfo();
			info.setName("CCode");
			info.setValue(CCode);
			info.setType(String.class);
			request.addProperty(info);

			// Adding Procedure Name
			info = new PropertyInfo();
			info.setName("ProcedureName");
			info.setValue(ProcedureName);
			info.setType(String.class);
			request.addProperty(info);

			if (ParamKey.length != 0) {
				// Adding parameters Key Name
				// e.g:- EmpID,EmpName,Phone Etc.
				SoapObject soapParameterKeyName = new SoapObject(NAMESPACE,
						"ParamKey");
				for (String i : ParamKey) {
					info = new PropertyInfo();
					info.setName("string");
					info.setValue(i);
					info.setType(String.class);
					soapParameterKeyName.addProperty(info);
				}
				request.addSoapObject(soapParameterKeyName);
				info = new PropertyInfo();
				info.setName("ParamKey");
				info.setValue(soapParameterKeyName);
				info.setType(String.class);
				request.addProperty(info);
			}
			if (ParamValue.length != 0) {
				// Adding parameters Key Value
				// e.g:- E1001,Saveen,9971236862 Etc.
				SoapObject soapParameterKeyValue = new SoapObject(NAMESPACE,
						"ParamKValue");
				for (String i : ParamValue) {
					info = new PropertyInfo();
					info.setName("string");
					info.setValue(i);
					info.setType(String.class);
					soapParameterKeyValue.addProperty(info);
				}
				request.addSoapObject(soapParameterKeyValue);
				info = new PropertyInfo();
				info.setName("ParamKValue");
				info.setValue(soapParameterKeyValue);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (ParamImageKey.length != 0) {
				// Adding Image parameter Key name
				// e.g :- EmployeePic,Signature etc
				SoapObject soapImageParameterKey = new SoapObject(NAMESPACE,
						"ParamImageKey");
				for (String i : ParamImageKey) {
					info = new PropertyInfo();
					info.setName("string");
					info.setValue(i);
					info.setType(String.class);
					soapImageParameterKey.addProperty(info);
				}
				request.addSoapObject(soapImageParameterKey);
				info = new PropertyInfo();
				info.setName("ParamImageKey");
				info.setValue(soapImageParameterKey);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (ParamImageValue.length != 0) {
				// Adding Image Parameter Key Value
				// e.g :- base64Image
				SoapObject soapImagePrameterKeyValue = new SoapObject(
						NAMESPACE, "ParamImageKeyValue");
				for (String i : ParamImageValue) {
					info = new PropertyInfo();
					info.setName("string");
					info.setValue(i);
					info.setType(String.class);
					soapImagePrameterKeyValue.addProperty(info);
				}
				request.addSoapObject(soapImagePrameterKeyValue);
				info = new PropertyInfo();
				info.setName("ParamImageKeyValue");
				info.setValue(soapImagePrameterKeyValue);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (!MobileNo.equalsIgnoreCase("")) {
				// Adding MobileNo
				info = new PropertyInfo();
				info.setName("MobNo");
				info.setValue(MobileNo);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (!Msg.equalsIgnoreCase("")) {
				// Adding Message
				info = new PropertyInfo();
				info.setName("Msg");
				info.setValue(Msg);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (!UserID.equalsIgnoreCase("")) {
				// Adding UserID
				info = new PropertyInfo();
				info.setName("UserID");
				info.setValue(UserID);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (!UserPass.equalsIgnoreCase("")) {
				// Adding UserPass
				info = new PropertyInfo();
				info.setName("UserPass");
				info.setValue(UserPass);
				info.setType(String.class);
				request.addProperty(info);
			}

			if (!IMEINo.equalsIgnoreCase("")) {
				// Adding IMEINo
				info = new PropertyInfo();
				info.setName("IMEINo");
				info.setValue(IMEINo);
				info.setType(String.class);
				request.addProperty(info);
			}
			if (!AppVersion.equalsIgnoreCase("")) {
				// Adding AppVersion
				info = new PropertyInfo();
				info.setName("Appversion");
				info.setValue(AppVersion);
				info.setType(String.class);
				request.addProperty(info);
			}
			//Adding requested Info
			info=new PropertyInfo();
			info.setName("RequestID");
			info.setValue(new DataBaseHandlerSelect(context).getUserID());
			info.setType(String.class);
			request.addProperty(info);

			new MarshalBase64().register(envelope);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidTransportCall = new HttpTransportSE(URL);
			try {
				RequestTime=CommonFunction.GetCurrentDateTime();
				StartTime = System.currentTimeMillis();
				androidTransportCall.call(SOAP_ACTION + MethodName, envelope);
				SoapPrimitive responsePrimitive = (SoapPrimitive) envelope.getResponse();
				JSONRESPONSE = responsePrimitive.toString();
				EndTime = System.currentTimeMillis();
				ResponseTime=CommonFunction.GetCurrentDateTime();
				insertRequesResponseLog(MethodName,(EndTime-StartTime),RequestTime,ResponseTime,logType,JSONRESPONSE,context);
				
			} catch (Exception e) {
				EndTime = System.currentTimeMillis();
				ResponseTime=CommonFunction.GetCurrentDateTime();
				JSONRESPONSE = e.getMessage();
				insertRequesResponseLog(MethodName,(EndTime-StartTime),RequestTime,ResponseTime,"Error"+e.getMessage(),"",context);
				Log.i("WebServiceOperation", e.getMessage());
			}
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.i("WebServiceOperation", e.getMessage());
		}
		return JSONRESPONSE;
	}

	public static void insertRequesResponseLog(String methodeName,long timeTook,String RequestTime,String ResponseTime,String Type,String JsonStr,Context context) {
		AndroidLog logInfo=new AndroidLog();
		logInfo.setEcode(new DataBaseHandlerSelect(context).GetEcode());
		logInfo.setLogText("Process took "+timeTook+" milliseconds to call Method :"+methodeName+" and get response :-"+JsonStr);
		logInfo.setServiceRequestTime(RequestTime);
		logInfo.setServiceResponseTime(ResponseTime);
		logInfo.setLogType(Type);
		logInfo.setConnectionUsed(Connectivity.getConnectionType(context));
		logInfo.setCreatedDate(new DataBaseHandlerSelect(context).getCurrentDate());
		new DataBaseHandlerInsert(context).addRecordInAndroidLog(logInfo);
	}
}

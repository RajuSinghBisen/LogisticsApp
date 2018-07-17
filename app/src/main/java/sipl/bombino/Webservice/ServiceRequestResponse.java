package sipl.bombino.Webservice;


import android.content.Context;
import android.util.Log;
import sipl.bombino.configuration.ApplicationConfiguration;
import sipl.bombino.helper.CommonFunction;

public class ServiceRequestResponse {
	
	// common Function to get request and response from WebService
		// @formatter:off
		public static String getJSONString(String ProcedureName, String[] ParamKey,
				String[] ParamValue, String[] ParamImgKey, String[] ParamImgValue,String IMEINo,String logType,String Appversion,String CurrentPassword,Context context) {
			String JSONString = "";
			try {
				if (ParamKey != null && ParamValue!=null && ParamImgKey ==null && ParamImgValue == null && IMEINo==null) {
					JSONString = WebServiceCall.DynamicWebMethod("GetTableWithParam",ApplicationConfiguration.GetToken(),ApplicationConfiguration.GetCCode(), ProcedureName, ParamKey,ParamValue,logType,context);
				} else if (ParamImgKey != null && ParamImgValue != null &&IMEINo==null) {
					JSONString = WebServiceCall.DynamicWebMethod("GetTableWithImageParam",ApplicationConfiguration.GetToken(),ApplicationConfiguration.GetCCode(), ProcedureName, ParamKey,ParamValue, ParamImgKey, ParamImgValue,logType,context);
				} else if(IMEINo==null&&CurrentPassword==null) {
					JSONString = WebServiceCall.DynamicWebMethod("GetTableWithoutParam",ApplicationConfiguration.GetToken(),ApplicationConfiguration.GetCCode(), ProcedureName,logType,context);
				} else if(IMEINo!=null&&CurrentPassword==null){
					JSONString = WebServiceCall.DynamicWebMethod("GetEncryptedUserCredentials", ApplicationConfiguration.GetToken(), ApplicationConfiguration.GetCCode(),
							ProcedureName, ParamKey, ParamValue, "", "", IMEINo,logType,Appversion,context);
				}
				else if(IMEINo!=null&&CurrentPassword!=null){
					JSONString=WebServiceCall.DynamicWebMethod("CheckForPasswordChange", ApplicationConfiguration.GetToken(),ApplicationConfiguration.GetCCode(), ProcedureName, IMEINo, CurrentPassword, logType,context);
				}
				
			} catch (Exception e) {
				if(CommonFunction.LOG)
					Log.i("Page: CommonDBFunction Method: getJSONString",e.getMessage());
			}
			return JSONString;
		}

}

package sipl.bombino.backgroundservices;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.CommonFunction;

public class CheckPacketStatus extends Activity {

	Context context;
	String JsonResponse = "", Ecode = "";
	DataBaseHandlerSelect DBObjSel;
	DataBaseHandlerDelete DBObjDel;

	public CheckPacketStatus(Context context) {
		this.context = context;
		DBObjSel = new DataBaseHandlerSelect(context);
		DBObjDel =new DataBaseHandlerDelete(context);
		new WebAsyncTask().execute();
	}

	public class WebAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			Ecode = DBObjSel.GetEcode();
		}

		@Override
		protected Void doInBackground(Void... params) {
			funCheckSystemUpdatedAwbNo(Ecode);
			return null;
		}

		@Override
		protected void onPostExecute(Void Result) {
		}
	}

	// This function will down all awbno's which are
	// Updated Today From System
	public void funCheckSystemUpdatedAwbNo(String Ecode) {
		ServiceRequestResponse.getJSONString("Sp_Android_CheckPacketStatus", new String[] {"Ecode"}, new String[] {Ecode}, null, null, null,"Request","",null,context);
		if (JsonResponse != "") {
			try {
				JSONArray jsonArr = new JSONArray(JsonResponse);
				if (jsonArr.length() > 0) {
					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonNode = jsonArr.getJSONObject(i);
						if (!jsonNode.has("Error")) {
							DBObjDel.funDeleteSystemUpdatedRecord(jsonNode.getString("AwbNo"));
						}
					}
				}
			} catch (JSONException e) {
				if(CommonFunction.LOG)
					Log.e("Error ",e.getMessage());
			}
		}
	}
}

package sipl.bombino.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sipl.bombino.R;

import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.dataadapter.ViewLogDataAdapter;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.AndroidLog;

public class ViewListActivity extends Activity implements OnClickListener {

	ViewLogDataAdapter adapterViewLog;
	List<AndroidLog> listViewLog;
	DataBaseHandlerSelect DbObjSel;
	TableLayout tblBackViewList, tblBackViewListUpdate;
	String Userid = "";
	int TotalAndroidLog = 0;
	ListView ViewList;
	TextView txtTotalLog;
	ConnectionDetector cd;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_list);
		getControls();
		DbObjSel = new DataBaseHandlerSelect(ViewListActivity.this);
		cd = new ConnectionDetector(ViewListActivity.this);
		listViewLog = DbObjSel.getAndroidLog();
		TotalAndroidLog = DbObjSel.getLogCount();
		txtTotalLog.setText("Total Log: " + TotalAndroidLog);
		adapterViewLog = new ViewLogDataAdapter(ViewListActivity.this,listViewLog);
		pDialog=new ProgressDialog(ViewListActivity.this);
		ViewList.setAdapter(adapterViewLog);
		Intent intnt = getIntent();
		Userid = intnt.getSerializableExtra("UserID").toString();
		tblBackViewList.setOnClickListener(this);
		tblBackViewListUpdate.setOnClickListener(this);
	}

	public void getControls() {
		txtTotalLog = (TextView) findViewById(R.id.txtTotalLog);
		tblBackViewList = (TableLayout) findViewById(R.id.tblBackViewList);
		tblBackViewListUpdate = (TableLayout) findViewById(R.id.tblBackViewListUpdate);
		ViewList = (ListView) findViewById(R.id.ViewList);
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	// function to get DeviceID
	public String getDeviceId() {
		String IMEINo = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			IMEINo = telephonyManager.getDeviceId();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				Log.i("Page: CaseListActivity Method: getDeviceId ", e.getMessage());
		}
		return IMEINo;
	}

	public void ShowMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tblBackViewList:
			goBack();
			break;

		case R.id.tblBackViewListUpdate:
			if (cd.isConnectingToInternet()) {
				final List<AndroidLog> listUpdate = DbObjSel.getAndroidLogToUpdate();
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected void onPreExecute() {
						pDialog = new ProgressDialog(ViewListActivity.this);
						pDialog.setMessage("Please wait...");
						pDialog.setCancelable(false);
						pDialog.show();
					}
					@Override
					protected Void doInBackground(Void... params) {
						for (int i = 0; i < listUpdate.size(); i++) {
							AndroidLog infoLog = listUpdate.get(i);
							ServiceRequestResponse.getJSONString(
									"Sp_AndroidLogFromDevice",
									new String[] { "@RequestCode", "@LogText",
											"@ServiceRequestTime",
											"@ServiceResponseTime", "@LogType",
											"@ConnectionUsed" },
									new String[] { infoLog.getEcode(),
											infoLog.getLogText(),
											infoLog.getServiceRequestTime(),
											infoLog.getServiceResponseTime(),
											infoLog.getLogType(),
											infoLog.getConnectionUsed() },
									null, null, null, "Request","",null,ViewListActivity.this);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void Result) {
						if (pDialog.isShowing())
							pDialog.dismiss();
					}
				}.execute();
			} else {
				showMessage("No Internet Connection.");
			}
			break;
		}
	}

	public void showMessage(String Msg) {
		Toast.makeText(ViewListActivity.this, Msg, Toast.LENGTH_SHORT).show();
	}

	public void goBack() {
		Intent i = new Intent(ViewListActivity.this, CaseListActivity.class);
		i.putExtra("UserID", Userid);
		startActivity(i);
		onStop();
		finish();
		onDestroy();
	}
}

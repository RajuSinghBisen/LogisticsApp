package sipl.bombino.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sipl.bombino.R;
import java.util.List;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.AlertDialogManager;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.PODUser;

public class MainActivity extends Activity {
	Button btnSubmit = null;
	EditText txtUserid, txtPassword;
	TextView tvVersion;
	String Userid = "";
	String Password = "";
	private ProgressDialog pDialog;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	DataBaseHandlerSelect DBObjSel = new DataBaseHandlerSelect(MainActivity.this);
	List<PODUser> userCredentials;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		cd = new ConnectionDetector(getApplicationContext());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		txtUserid = (EditText) findViewById(R.id.txtUserId);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		tvVersion = (TextView) findViewById(R.id.tvVersion);
		txtUserid.clearFocus();
		txtUserid.setText(DBObjSel.GetEcode());
		txtPassword.requestFocus();
		btnSubmit = (Button) findViewById(R.id.btnLogin);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateUser()) {
					AsyncWebServiceCall task = new AsyncWebServiceCall();
					task.execute();
				}
			}
		});
		tvVersion.setText("Version:-" + getVersionName());
	}

	public boolean validateUser() {
		if (txtUserid.getText().toString().equalsIgnoreCase("")) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Please Enter UserID.", Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER_HORIZONTAL, 10, 10);
			t.show();
			txtUserid.requestFocus();
			return false;
		}
		if (txtPassword.getText().toString().equalsIgnoreCase("")) {
			Toast t = Toast.makeText(getApplicationContext(),"Please Enter Password.", Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER_HORIZONTAL, 10, 10);
			t.show();
			txtPassword.requestFocus();
			return false;
		}
		return true;
	}
	public class AsyncWebServiceCall extends AsyncTask<Void, Void, Void> {
		String UserId ="",Password="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			UserId = txtUserid.getText().toString().trim();
			Password=txtPassword.getText().toString().trim();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			userCredentials = DBObjSel.ValidateUser(UserId, Password);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (userCredentials.size() != 0) {
				for (PODUser pn : userCredentials) {
					Userid = pn.getUserID();
					Password = pn.getPassword();
				}
			} else {
				alert.showAlertDialog(MainActivity.this, "Login Failed.","Invalid UserID or Password.", false);
				txtPassword.setText("");
			}
			if (txtUserid.getText().toString().equalsIgnoreCase((Userid.toString())) && txtPassword.getText().toString().equalsIgnoreCase((Password.toString()))) {
				Intent i = new Intent(MainActivity.this, OptionActivity.class);
				i.putExtra("UserID", Userid);
				startActivity(i);
				onStop();
				finish();
				onDestroy();
			}
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Exit Confirmation");
		dialog.setMessage("Exit From Application ?");
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		dialog.setNegativeButton("Cancel", null);
		dialog.show();
		return;
	}

	// function to get DeviceID
	public String getDeviceId() {
		String IMEINo = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			IMEINo = telephonyManager.getDeviceId();
		} catch (Exception e) {
			if(CommonFunction.LOG)
				e.printStackTrace();
		}
		return IMEINo;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (pDialog != null) {
			pDialog.dismiss();
		}
		finish();
	}

	public String getVersionName() {
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Not Able To Get Version Name", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return versionName;
	}
}

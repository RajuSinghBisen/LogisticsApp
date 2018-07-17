package sipl.bombino.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sipl.bombino.R;

import sipl.bombino.commonfillfunction.FillRecordsInLocalDatabase;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;

public class OptionActivity extends Activity implements OnClickListener{
	
	Button btnCaseList,btnMeaterReading,btnRefreshMaster;
	TableLayout tblLogOut;
	String UserID ="";
	TextView txtUserID;
	ProgressDialog pDialog;
	boolean isActivityOnFront = false;
	DataBaseHandlerSelect dbSel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_option);
		isActivityOnFront = true;
		dbSel = new DataBaseHandlerSelect(OptionActivity.this);
		getControls();
	}
	
	public void getControls(){
		btnCaseList=(Button)findViewById(R.id.btnCaseList);
		btnMeaterReading=(Button)findViewById(R.id.btnMeaterReading);
		btnRefreshMaster=(Button)findViewById(R.id.btnRefreshMaster);
		tblLogOut=(TableLayout)findViewById(R.id.tblLogOut);
		txtUserID=(TextView)findViewById(R.id.txtUserID);
		Intent intnt = getIntent();
		UserID= intnt.getSerializableExtra("UserID").toString();
		txtUserID.setText(dbSel.GetUserName() + " - " + UserID);
		//txtUserID.setText("UserID ["+UserID+"]");
		btnCaseList.setOnClickListener(this);
		btnMeaterReading.setOnClickListener(this);
		btnRefreshMaster.setOnClickListener(this);
		tblLogOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCaseList:
			Intent intentCaseList = new Intent(OptionActivity.this, CaseListActivity.class);
			intentCaseList.putExtra("UserID", UserID);
			startActivity(intentCaseList);
			onStop();
			finish();
			onDestroy();
			break;
		case R.id.btnMeaterReading:
			Intent intentMeterReading = new Intent(OptionActivity.this, MeterReadingActivity.class);
			intentMeterReading.putExtra("UserID", UserID);
			startActivity(intentMeterReading);
			onStop();
			finish();
			onDestroy();
			break;
		case R.id.btnRefreshMaster:
			AlertDialog.Builder dialog=new AlertDialog.Builder(OptionActivity.this);
			dialog.setTitle("Delete Confirmation");
			dialog.setIcon(R.drawable.fail);
			dialog.setMessage("This will delete all master data from local and " +
					"download new data from server. Are you sure to continue?");
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new AsyncTask<Void, Void, Void>() {
						
						@Override
						protected void onPreExecute(){
							pDialog=new ProgressDialog(OptionActivity.this);
							pDialog.setMessage("Please wait...");
							pDialog.setCancelable(false);
							pDialog.show();
						}

						@Override
						protected Void doInBackground(Void... params) {
							new FillRecordsInLocalDatabase(OptionActivity.this).funDownPacketStatus(true);
							new FillRecordsInLocalDatabase(OptionActivity.this).funDownRcRelation(true);
							new FillRecordsInLocalDatabase(OptionActivity.this).funDownRcRemarks(true);
							new FillRecordsInLocalDatabase(OptionActivity.this).funDownPODRemarksMaster(true);
							return null;
						}
						
						@Override
						protected void onPostExecute(Void result) {
							if (isActivityOnFront) {
								if (pDialog.isShowing())
									pDialog.dismiss();
								ShowMessage("New master records are available.");
							}
						}
						
					}.execute();
				}
			});
			dialog.setNegativeButton("Cancel", null);
			dialog.show();
			
			break;
		case R.id.tblLogOut:
			Logout();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		isActivityOnFront=true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityOnFront=true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		isActivityOnFront=false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isActivityOnFront=false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isActivityOnFront=false;
	}

	public void ShowMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void Logout() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(OptionActivity.this);
		dialog.setTitle("Logout Confirmation.");
		dialog.setIcon(R.drawable.fail);
		dialog.setMessage("Logout From Application ?");
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
				onDestroy();
			}
		});
		dialog.setNegativeButton("Cancel", null);
		dialog.show();
		return;
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(OptionActivity.this);
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
}

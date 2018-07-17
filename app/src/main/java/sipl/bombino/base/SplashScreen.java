package sipl.bombino.base;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.sipl.bombino.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.backgroundservices.BackgroundService;
import sipl.bombino.commonfillfunction.FillRecordsInLocalDatabase;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.databseOperation.DataBaseHandlerUpdate;
import sipl.bombino.gpstracker.GPSTracker;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.DeviceVerificationGetterSetter;
import sipl.bombino.properties.PODUser;
import sipl.bombino.properties.ValidationInfo;

public class SplashScreen extends Activity {
    DataBaseHandlerInsert DBObjIns;
    DataBaseHandlerSelect DBObjSel;
    DataBaseHandlerDelete DBObjDel;
    DataBaseHandlerUpdate DBObjUpd;
    ConnectionDetector cd;
    String JSONResponse = "";
    boolean DeviceAuthorized = false, AppversionFlag = false, isActivityOnFront = false;
    GPSTracker gps;
    boolean isConnectedToInternet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        cd = new ConnectionDetector(getApplicationContext());
        DBObjIns = new DataBaseHandlerInsert(SplashScreen.this);
        DBObjSel = new DataBaseHandlerSelect(SplashScreen.this);
        DBObjDel = new DataBaseHandlerDelete(SplashScreen.this);
        DBObjUpd = new DataBaseHandlerUpdate(SplashScreen.this);
        if (!CheckGPS()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
            dialog.setTitle("GPS Error.");
            dialog.setMessage("Sorry GPS not enabled,Please enable and Try Again.");
            dialog.setIcon(R.drawable.fail);
            dialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            Dialog alert = dialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            return;
        }
        if (cd.isConnectingToInternet() == false) {
            if (DBObjSel.funCheckLoginTable(DBObjSel.getCurrentDate())) {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
                dialog.setTitle("Connection Error.");
                dialog.setMessage("Sorry,No Internet Connection!!\n Enable Internet and Try Again.");
                dialog.setIcon(R.drawable.fail);
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                Dialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
                return;
            }
        } else if (cd.isConnectingToInternet()) {
            isActivityOnFront = true;
            AsyncWebserviceCall task = new AsyncWebserviceCall();
            task.execute();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isActivityOnFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityOnFront = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActivityOnFront = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityOnFront = false;
    }

    public class AsyncWebserviceCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /* ============Deleting previous day records=================== */
            // DBObjDel.deleteOldPodInsertTable(DBObjSel.getCurrentDate());
            DBObjDel.deleteOldPodDetailTable(DBObjSel.getCurrentServerDate());
            DBObjDel.deleteLoginDetail(DBObjSel.getCurrentDate());
            DBObjDel.deleteFromDeviceVerify(DBObjSel.getCurrentDate());
            /* ================================ */
            isConnectedToInternet = cd.isConnectingToInternet();
            Log.d("","");

            if (isConnectedToInternet) {
                if (!DBObjSel.funCheckLoginTable(DBObjSel.getCurrentDate())) {
                    // For Checking Device is Authorized or Not.
                    JSONResponse = ServiceRequestResponse.getJSONString("SP_AndroidGetCredenOnBehalfOfIEMINo", null, null, null, null, getDeviceId(), "Request", getVersionName(), null, SplashScreen.this);
                    DeviceAuthorized = funCheckVerification(JSONResponse);
                } else {
                    DeviceAuthorized = true;
                }
                String JsonChangePass = ServiceRequestResponse.getJSONString("Sp_Android_CheckForPasswordChange", null, null, null, null, getDeviceId(), "Request", "", DBObjSel.getUserPassword(), SplashScreen.this);
                funcChangePassword(JsonChangePass);
            }

            if (DeviceAuthorized) {
                funcGetLoginDetail(JSONResponse);
                String liveDatabaseAndroidVersion = new FillRecordsInLocalDatabase(SplashScreen.this).getCurrentRunningVersionFromLive();
                if (liveDatabaseAndroidVersion.equals(getVersionName())) {
                    // This block will get Android Version from Server Database
                    // and check Whether Server Database Version and
                    // manifest Version are Same? then update local table
                    DBObjUpd.updateAndroidVersion(liveDatabaseAndroidVersion);
                }
                AppversionFlag = DBObjSel.CheckVersion(getVersionName());
                if (AppversionFlag == true) {
                    // getting packet status
                    if (DBObjSel.getRecordCount("PacketStatus") <= 0) {
                        new FillRecordsInLocalDatabase(SplashScreen.this).funDownPacketStatus(false);
                    }
                    // getting all Relation -SP_Android_GetRCrelation
                    if (DBObjSel.getRecordCount("RcRelation") <= 0) {
                        new FillRecordsInLocalDatabase(SplashScreen.this).funDownRcRelation(false);
                    }
                    // getting all remarks -SP_Android_GetRCRemarks
                    if (DBObjSel.getRecordCount("RcRemarks") <= 0) {
                        new FillRecordsInLocalDatabase(SplashScreen.this).funDownRcRemarks(false);
                    }
                    // getting all remarks -SP_Android_GetRCRemarks
                    if (DBObjSel.getRecordCount("PODRemarksMaster") <= 0) {
                        new FillRecordsInLocalDatabase(SplashScreen.this).funDownPODRemarksMaster(false);
                    }
                    String validationFields = ServiceRequestResponse.getJSONString("SP_Android_GetAndValidationMasterField", null, null, null, null, null, "Request", "", null, SplashScreen.this);
                    insertIntoValidationField(validationFields);
                    // run background service;
                    funToRunBackgroundService();
                    // this function will copy database to SDCard.
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            if (DeviceAuthorized == false) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
                dialog.setTitle("\tAuthentication Error.");
                dialog.setMessage("Your Device is not authorized.\nPlease contact your administrator..!!");
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                Dialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                if (isActivityOnFront)
                    alert.show();
                return;
            } else if (AppversionFlag == false) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SplashScreen.this);
                dialog.setTitle("\tOld Version Error.");
                dialog.setMessage("You are using old version of an application. Please update latest version of an application");
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                Dialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                if (isActivityOnFront)
                    alert.show();
                return;
            } else {
                /***checking whether downloading packet data or not
                 * if 1  means download data otherwise don't ***/
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        JSONResponse = "";
                        JSONResponse = ServiceRequestResponse.getJSONString("SP_Android_IsRunsheetClose"
                                , new String[]{"@ECode"}
                                , new String[]{new DataBaseHandlerSelect(SplashScreen.this).getUserID()}, null, null, null, "Request", "", null, SplashScreen.this);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (null == JSONResponse) {
                            return;
                        }
                        if (!JSONResponse.equals("")) {
                            if (CommonFunction.IsJSONValid(JSONResponse)) {
                                try {
                                    JSONArray jsonArr = new JSONArray(JSONResponse);
                                    if (jsonArr.length() != 0) {
                                        if (!jsonArr.getJSONObject(0).has("Error")) {
                                            if (jsonArr.getJSONObject(0).getInt("IsRunSheetClosed")==1) {
                                                new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {
                                                        new FillRecordsInLocalDatabase(SplashScreen.this).funDownPodList();
                                                        return null;
                                                    }
                                                }.execute();

                                            } else if (jsonArr.getJSONObject(0).getInt("IsRunSheetClosed")==0) {
                                                showToastMessage("Your previous runsheet not closed unable to fetch data");
                                            } else {
                                               // showToastMessage(JSONResponse);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (CommonFunction.LOG)
                                        e.printStackTrace();
                                }
                            }
                        }
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }.execute();
            }
        }
    }

    // function to get DeviceID
    public String getDeviceId() {
        String IMEINo = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEINo = telephonyManager.getDeviceId();
        } catch (Exception e) {
            if (CommonFunction.LOG)
                Log.e("Error ", e.getMessage());
        }

        return IMEINo;
    }

    // function to get Login Details Through IMEI no
    public void funcGetLoginDetail(String jsonData) {
        if (null == jsonData) {
            return;
        }
        if (!jsonData.equals("")) {
            try {
                JSONArray jsonArr = new JSONArray(jsonData);
                if (jsonArr.length() != 0) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonNode = jsonArr.getJSONObject(i);
                        DBObjIns.addRecordIntoLogin(new PODUser(jsonNode.getString("ECode"),jsonNode.getString("EName"),jsonNode.getString("UserPass")
                                , jsonNode.getString("IMEINo"), jsonNode.getString("AndroidVersion"), jsonNode.getString("ServerCurrentDate")));
                    }
                }
            } catch (JSONException e) {
                if (CommonFunction.LOG)
                    Log.e("Error ", e.getMessage());
            }
        }
    }

    // function will change password if there is any changes
    public void funcChangePassword(String jsonData) {
        if (null == jsonData) {
            return;
        }
        if (!jsonData.equals("")) {
            try {
                JSONArray jsonArr = new JSONArray(jsonData);
                if (jsonArr.length() != 0) {
                    JSONObject jsonNode = jsonArr.getJSONObject(0);
                    if (!jsonNode.has("Error")) {
                        if (jsonNode.getString("PasswordStatus").equalsIgnoreCase("Change")) {
                            String NewPassword = jsonNode.getString("NewPassword");
                            DBObjUpd.upDateLoginTableForChangePassword(NewPassword);
                        }
                    }
                }
            } catch (JSONException e) {
                if (CommonFunction.LOG)
                    Log.e("Error ", e.getMessage());
            }
        }
    }

    // function to check device is verified or not.
    private boolean funCheckVerification(String jsonData) {
        boolean flag = false;
        if (null == jsonData) {
            return false;
        }
        if (!jsonData.equals("")) {
            try {
                JSONArray jsonArry = new JSONArray(jsonData);
                if (jsonArry.length() != 0) {
                    for (int i = 0; i < jsonArry.length(); i++) {
                        JSONObject jsonNode = jsonArry.getJSONObject(i);
                        if (jsonNode.has("Error")) {
                            flag = false;
                        } else if (jsonNode.has("Result")) {
                            if (jsonNode.getString("Result").equalsIgnoreCase("Success")) {
                                flag = true;
                                DBObjIns.addRecordIntoDeviceVeri(new DeviceVerificationGetterSetter("true"));
                            } else {
                                flag = false;
                                DBObjIns.addRecordIntoDeviceVeri(new DeviceVerificationGetterSetter("false"));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                flag = false;
                DBObjIns.addRecordIntoDeviceVeri(new DeviceVerificationGetterSetter("false"));
                if (CommonFunction.LOG)
                    Log.e("Error ", "" + e.getMessage());
            }
        }
        return flag;
    }

    public void insertIntoValidationField(String validationJson) {
        try {
            if (null != validationJson) {
                if (!validationJson.equals("")) {
                    JSONArray jsonArry = new JSONArray(validationJson);
                    if (jsonArry.length() != 0 && !jsonArry.getJSONObject(0).has("Error")) {
                        List<ValidationInfo> listVali = new ArrayList<ValidationInfo>();
                        ValidationInfo info = null;
                        for (int i = 0; i < jsonArry.length(); i++) {
                            info = new ValidationInfo();
                            info.setValidationField(jsonArry.getJSONObject(i).getString("ValidationField"));
                            info.setIsValidationRequired(jsonArry.getJSONObject(i).getBoolean("IsValidationRequired") ? 1 : 0);
                            listVali.add(info);
                        }
                        new DataBaseHandlerDelete(SplashScreen.this).deleteTableRecord("ValidationMaster");
                        new DataBaseHandlerInsert(SplashScreen.this).insertIntoAndroid(listVali);
                    }
                }
            }
        } catch (Exception e) {
            if (CommonFunction.LOG)
                Log.e("Error ", "" + e.getMessage());
        }
    }

    // this function will run background service to update records
    public void funToRunBackgroundService() {
        startService(new Intent(this, BackgroundService.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every hour
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), ((10 * 60 * 1000)), pintent);
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

    public void showToastMessage(String msg) {
        if (isActivityOnFront)
            Toast.makeText(SplashScreen.this, msg, Toast.LENGTH_SHORT);
    }

    private boolean CheckGPS() {
        boolean isGPSEnabled = false;
        gps = new GPSTracker(SplashScreen.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            isGPSEnabled = true;
        } else {
            gps.showSettingsAlert();
            isGPSEnabled = false;
        }
        return isGPSEnabled;
    }
}


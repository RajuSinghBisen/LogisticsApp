package sipl.bombino.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sipl.bombino.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.commonfillfunction.FillRecordsInLocalDatabase;
import sipl.bombino.dataadapter.PodListDataAdapter;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.PodGetterSetter;

public class CaseListActivity extends Activity implements OnClickListener {

    // Strings
    String UserID = "", PodCount = "", JsonResponse = "", tabStatus = "P";
    // boolean
    boolean IsrunsheetClosed = true;
    // Buttons
    Button btnPending = null, btnDelivered = null, btnRto = null, btnUnDelivered = null;
    // Image Buttons
    ImageButton btnRefreshCaseList, btnUpdateList, btnViewLogList,
            btnDeleteOldRecord, btnSearchAwbNo;
    // btnUploadLiveCaseList
    ImageView ImgNodata;
    TableLayout tblBack;
    // List
    List<PodGetterSetter> list = null;
    // ProgressDialog
    private ProgressDialog pDialog;
    // ListView
    ListView listView;

    TextView txtpodCount, txtCaseTitle, tvEmptyMessage;
    DataBaseHandlerSelect DBObjSel;
    DataBaseHandlerDelete DBObjDel;
    public static CaseListActivity instance = null;
    boolean isActivityOnFront = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityOnFront = true;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_case_list);
        DBObjSel = new DataBaseHandlerSelect(CaseListActivity.this);
        DBObjDel = new DataBaseHandlerDelete(CaseListActivity.this);
        Intent intnt = getIntent();
        UserID = intnt.getSerializableExtra("UserID").toString();
        funcGetControls();
        txtCaseTitle.setText("Delivery List");
        btnDelivered.setOnClickListener(this);
        btnPending.setOnClickListener(this);
        btnRto.setOnClickListener(this);
        btnUnDelivered.setOnClickListener(this);
        btnRefreshCaseList.setOnClickListener(this);
        btnViewLogList.setOnClickListener(this);
        btnDeleteOldRecord.setOnClickListener(this);
        tblBack.setOnClickListener(this);
        btnSearchAwbNo.setOnClickListener(this);
        btnPending.setBackgroundResource(R.drawable.tabborder);
        btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
        ImgNodata.setVisibility(View.GONE);
        tvEmptyMessage.setVisibility(View.GONE);
        ToggleList("P", "");
    }

    // function to initialize controls before they can be accessed
    public void funcGetControls() {
        // ListView
        listView = (ListView) findViewById(R.id.CaseList);
        // Buttons
        btnPending = (Button) findViewById(R.id.btnPending);
        btnDelivered = (Button) findViewById(R.id.btnDelivered);
        btnRto = (Button) findViewById(R.id.btnRto);
        btnUnDelivered = (Button) findViewById(R.id.btnUnDelivered);

        // Image Buttons
        btnRefreshCaseList = (ImageButton) findViewById(R.id.btnRefreshCaseList);
        btnUpdateList = (ImageButton) findViewById(R.id.btnUpdateList);
        ImgNodata = (ImageView) findViewById(R.id.ImgNodata);
        btnViewLogList = (ImageButton) findViewById(R.id.btnViewLogList);
        btnDeleteOldRecord = (ImageButton) findViewById(R.id.btnDeleteOldRecord);
        btnSearchAwbNo = (ImageButton) findViewById(R.id.btnSearchAwbNo);

        // TextViews
        txtpodCount = (TextView) findViewById(R.id.txtpodCount);
        txtCaseTitle = (TextView) findViewById(R.id.txtCaseTitle);
        tvEmptyMessage = (TextView) findViewById(R.id.tvEmptyMessage);

        // Tables
        tblBack = (TableLayout) findViewById(R.id.tblBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelivered:
                btnPending.setBackgroundResource(R.drawable.tab1_gredient);
                btnDelivered.setBackgroundResource(R.drawable.tabborder);
                btnRto.setBackgroundResource(R.drawable.tab1_gredient);
                btnUnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                tabStatus = "D";
                ToggleList(tabStatus, "");
                break;
            case R.id.btnPending:
                btnPending.setBackgroundResource(R.drawable.tabborder);
                btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                btnRto.setBackgroundResource(R.drawable.tab1_gredient);
                btnUnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                tabStatus = "P";
                ToggleList(tabStatus, "");
                break;
            case R.id.btnRto:
                btnRto.setBackgroundResource(R.drawable.tabborder);
                btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                btnPending.setBackgroundResource(R.drawable.tab1_gredient);
                btnUnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                tabStatus = "R";
                ToggleList(tabStatus, "");
                break;
            case R.id.btnUnDelivered:
                btnRto.setBackgroundResource(R.drawable.tab1_gredient);
                btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                btnPending.setBackgroundResource(R.drawable.tab1_gredient);
                btnUnDelivered.setBackgroundResource(R.drawable.tabborder);
                tabStatus = "UD";
                ToggleList(tabStatus, "");
                break;
            case R.id.btnRefreshCaseList:
                /***checking availability of data
                 * if returns true  means download data otherwise don't ***/
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        pDialog = new ProgressDialog(CaseListActivity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        new FillRecordsInLocalDatabase(CaseListActivity.this).funUpdateloginTable();
                        JsonResponse = "";
                        JsonResponse = ServiceRequestResponse.getJSONString("SP_Android_IsRunsheetClose"
                                , new String[]{"@ECode"}
                                , new String[]{new DataBaseHandlerSelect(CaseListActivity.this).getUserID()}, null, null, null, "Request", "", null, CaseListActivity.this);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        btnPending.setBackgroundResource(R.drawable.tabborder);
                        btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                        btnRto.setBackgroundResource(R.drawable.tab1_gredient);
                        btnUnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                        ToggleList("P", "");
                        tabStatus = "P";
                        if (!JsonResponse.equals("")) {
                            if (CommonFunction.IsJSONValid(JsonResponse)) {
                                try {
                                    JSONArray jsonArr = new JSONArray(JsonResponse);
                                    if (jsonArr.length() != 0) {
                                        if (!jsonArr.getJSONObject(0).has("Error")) {
                                            if (jsonArr.getJSONObject(0).getInt("IsRunSheetClosed")==1) {
                                                new AsyncTask<Void, Void, Void>() {
                                                    @Override
                                                    protected Void doInBackground(Void... params) {
                                                        new FillRecordsInLocalDatabase(CaseListActivity.this).funDownPodList();
                                                        return null;
                                                    }
                                                }.execute();
                                            } else if (jsonArr.getJSONObject(0).getInt("IsRunSheetClosed")==0) {
                                                IsrunsheetClosed = false;
                                            } else {
                                                ShowMessage(JsonResponse);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (!IsrunsheetClosed) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(CaseListActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle("Runsheet Information");
                            dialog.setMessage("Your previous runsheet not closed unable to fetch data");
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setNegativeButton("Cancel", null);
                            dialog.show();
                        }
                    }
                }.execute();
                break;
            case R.id.btnViewLogList:
                Intent intLog = new Intent(CaseListActivity.this, ViewListActivity.class);
                intLog.putExtra("UserID", UserID);
                startActivity(intLog);
                break;
            case R.id.btnDeleteOldRecord:
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        CaseListActivity.this);
                dialog.setTitle("\tDelete Confirmation.");
                dialog.setMessage("This option will clear all your previous RTO, Un-Delivered and Delivered Data.\n Are you sure to proceed ?");
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (DBObjDel.deleteOldPodDetailTable(DBObjSel.getCurrentServerDate()) > 0
                                        || DBObjDel.DeleteFromInsertPODEntry(DBObjSel.getCurrentServerDate()) > 0) {
                                    ShowMessage("Record Deleted Successfully.");
                                    btnPending.setBackgroundResource(R.drawable.tabborder);
                                    btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                                    btnRto.setBackgroundResource(R.drawable.tab1_gredient);
                                    btnUnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
                                    ToggleList("P", "");
                                    tabStatus = "P";
                                } else {
                                    ShowMessage("Nothing to Delete.");
                                }
                            }
                        });
                dialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                Dialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
                break;
            case R.id.tblBack:
                Intent i = new Intent(CaseListActivity.this, OptionActivity.class);
                i.putExtra("UserID", UserID);
                startActivity(i);
                finish();
                //Logout();
                break;
            case R.id.btnSearchAwbNo:
                showAwbNoSearch();
                break;
            default:
                break;
        }
    }

    public void ToggleList(String Type, String awbNo) {
        PodCount = DBObjSel.getDeliveredPacketStatus();
        txtpodCount.setText("Packets : [" + PodCount + "]");
        if (Type.equals("P")) {
            btnPending.setBackgroundResource(R.drawable.tabborder);
            btnDelivered.setBackgroundResource(R.drawable.tab1_gredient);
            btnRto.setBackgroundResource(R.drawable.tab1_gredient);
            if (awbNo.equals("")) {
                list = DBObjSel.GetPODList();
            } else {
                list = DBObjSel.GetPODListAwbNo(awbNo);
            }
            if (list.isEmpty()) {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, true,false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.VISIBLE);
                tvEmptyMessage.setVisibility(View.VISIBLE);
                if (awbNo.equals("")) {
                    tvEmptyMessage.setText("No Packets For Delivery.");
                } else {
                    tvEmptyMessage.setText("Awbno not found.");
                }
            } else {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, true,false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.GONE);
                tvEmptyMessage.setVisibility(View.GONE);
                tvEmptyMessage.setText("");
            }

        } else if (Type.equals("D")) {
            if (awbNo.equals("")) {
                list = DBObjSel.GetPODListDelivered("DELIVERED");
            } else {
                list = DBObjSel.GetPODListDeliveredOnBehalfOfAwbNo(awbNo);
            }
            if (list.isEmpty()) {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false,false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.VISIBLE);
                tvEmptyMessage.setVisibility(View.VISIBLE);
                if (awbNo.equals("")) {
                    tvEmptyMessage.setText("No Packets Delivered Today.!!");
                } else {
                    tvEmptyMessage.setText("Awbno not found.");
                }
            } else {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false,false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.GONE);
                tvEmptyMessage.setVisibility(View.GONE);
                tvEmptyMessage.setText("");
            }
        }/* else if (Type.equals("R")) {
            list = DBObjSel.GetPODListDelivered("RTO");
            if (list.isEmpty()) {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.VISIBLE);
                tvEmptyMessage.setVisibility(View.VISIBLE);
                tvEmptyMessage.setText("No Packets RTO Today.!!");
            } else {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.GONE);
                tvEmptyMessage.setVisibility(View.GONE);
                tvEmptyMessage.setText("");
            }
        }*/ else if (Type.equals("UD")) {
            if (awbNo.equals("")) {
                list = DBObjSel.GetPODListDelivered("UN-DELIVERED");
            } else {
                list = DBObjSel.GetPODListDeliveredOnBehalfOfAwbNo(awbNo);
            }
            if (list.isEmpty()) {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false,true);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.VISIBLE);
                tvEmptyMessage.setVisibility(View.VISIBLE);
                if (awbNo.equals("")) {
                    tvEmptyMessage.setText("No Packets Un-Delivered Today.!!");
                } else {
                    tvEmptyMessage.setText("Awbno not found.");
                }
            } else {
                PodListDataAdapter cusAdapter = new PodListDataAdapter(CaseListActivity.this, list, false,true);
                listView.setAdapter(cusAdapter);
                ImgNodata.setVisibility(View.GONE);
                tvEmptyMessage.setVisibility(View.GONE);
                tvEmptyMessage.setText("");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        instance = this;
    }

    public static CaseListActivity getInstance() {
        return instance;
    }

    public void refreshPackets() {
        ToggleList(tabStatus, "");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CaseListActivity.this, OptionActivity.class);
        i.putExtra("UserID", UserID);
        startActivity(i);
        finish();
    }

    public void ShowMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showAwbNoSearch() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("AwbNo Search");
        alert.setMessage("Enter AwbNo to search.");
        final EditText input = new EditText(this);
        input.setHeight(120);
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        //Overriding above set positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!input.getEditableText().toString().trim().equals("")) {
                    ToggleList(tabStatus, input.getEditableText().toString().trim());
                    alertDialog.dismiss();
                } else {
                    ShowMessage("Please enter awbno to search.");
                    return;
                }
            }
        });
    }

    public void showDialog() {
        if (isActivityOnFront) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    public void dismissDialog() {
        if (isActivityOnFront) {
            if (pDialog != null)
                if (pDialog.isShowing())
                    pDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityOnFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
        }
        isActivityOnFront = false;
        // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityOnFront = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOnFront = false;
    }
}

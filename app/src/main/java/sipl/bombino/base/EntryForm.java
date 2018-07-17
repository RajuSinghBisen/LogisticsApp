package sipl.bombino.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sipl.bombino.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.commonfillfunction.FillRecordsInLocalDatabase;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.databseOperation.DataBaseHandlerUpdate;
import sipl.bombino.gpstracker.GPSTracker;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.helper.DBCopierToSD;
import sipl.bombino.helper.ImageCaptureClass;
import sipl.bombino.helper.SignatureGesture;
import sipl.bombino.properties.PodGetterSetter;
import sipl.bombino.properties.PodInsertGetterSetter;

public class EntryForm extends Activity implements OnItemSelectedListener, OnClickListener {

    String AwbNo = "", Consignee = "", UserId = "", base64img = "",
            base64signature = "", latitude = "", longitude = "",
            RunsheetNo = "", Address = "", Phone = "", RunsheetDate = "", Status = "";

    // declaring controls variable
    TableLayout tblBackCEF, tblSaveRecordCEF, tblRTO;
    TableRow tblrowRcRemarks, tblrowNegRemarks;
    TextView tvUserID, lblCodamount;
    Spinner spnpktStatus, spnRCRelation, spnRcRemarks, spnPaymentType, spnIdProof, spnNegRemarks;
    EditText txtConsignee, txtRcName, txtRcPhone, txtAwbNo, txtCODAmount, txtIDProof, txtPosRemarks;
    Button btnCapturePhoto, btnCaptureSignature;
    ImageView imgphoto, ImgSignature;
    TextView tvRecCodAmt, tvCodamount, tvPaymentType;
    private ProgressDialog pDialog;
    LinearLayout tblrowRcPhone, tblrowCODAmt, tblrowCODRcAmount, tblrowIDProofNo, tblPosRemarks, tblrowIDProff,
            tblrowRcName, tblrowRcRelation, tblrowPaymentType;
    List<String> listpktstatus = new ArrayList<String>();
    List<String> listRcRelation = new ArrayList<String>();
    List<String> listRcRemarks = new ArrayList<String>();
    List<String> listPaymentType = new ArrayList<String>();
    List<String> listIDProof = new ArrayList<String>();
    List<String> listPODRemarksMaster = new ArrayList<String>();
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    Bitmap bitmapImg = null, bitmapSignature = null;
    boolean isUpdateButtonPressed = false;
    boolean isRecordSaved = false;
    public static final int SIGNATURE_ACTIVITY = 1;
    DataBaseHandlerSelect DBObjSel;
    DataBaseHandlerInsert DBObjIns;
    DataBaseHandlerDelete DBObjDel;
    DataBaseHandlerUpdate DBObjUpd;
    DBCopierToSD dbcopy = new DBCopierToSD();
    GPSTracker gps;
    double cod = 0.0;
    boolean isActivityOnFront = false;
    int TotalUpdated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_entry_form);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DBObjSel = new DataBaseHandlerSelect(EntryForm.this);
        DBObjIns = new DataBaseHandlerInsert(EntryForm.this);
        DBObjDel = new DataBaseHandlerDelete(EntryForm.this);
        DBObjUpd = new DataBaseHandlerUpdate(EntryForm.this);
        isActivityOnFront = true;
        GetControls();
        CheckGPS();
        Intent i = getIntent();
        UserId = i.getSerializableExtra("UserId").toString();
        tvUserID.setText(DBObjSel.GetUserName() + " - " + UserId);
        txtAwbNo.setText(i.getSerializableExtra("AwbNo").toString());
        AwbNo = i.getSerializableExtra("AwbNo").toString();
        txtCODAmount.setText(i.getSerializableExtra("CodAmount").toString());
        lblCodamount.setText(i.getSerializableExtra("CodAmount").toString());
        txtConsignee.setText(i.getSerializableExtra("ConsigneeName").toString());
        RunsheetNo = i.getSerializableExtra("RunsheetNo").toString();
        RunsheetDate = i.getSerializableExtra("RunSheetDate").toString();
        Address = i.getSerializableExtra("Address").toString();
        Phone = i.getSerializableExtra("Phone").toString();

        cod = Double.parseDouble(i.getSerializableExtra("CodAmount").toString());
        if (cod == 0.0) {
            lblCodamount.setVisibility(View.GONE);
            txtCODAmount.setVisibility(View.GONE);
            tvRecCodAmt.setVisibility(View.GONE);
            tvCodamount.setVisibility(View.GONE);
            tvPaymentType.setVisibility(View.GONE);
            spnPaymentType.setVisibility(View.GONE);
        }
        txtCODAmount.setEnabled(false);
        spnPaymentType.setEnabled(false);
        tblSaveRecordCEF.setOnClickListener(this);
        tblBackCEF.setOnClickListener(this);
        btnCapturePhoto.setOnClickListener(this);
        btnCaptureSignature.setOnClickListener(this);
        spnpktStatus.setOnItemSelectedListener(this);
        spnRCRelation.setOnItemSelectedListener(this);
        tblrowRcName.setVisibility(View.GONE);
        CheckSystemUpdatedAwbNo(AwbNo);
        new AsyncWebServiceCall().execute();
    }

    /**
     * Checking whether spinner has data if not go to case list activity
     */
    public void checkAllSpinner() {
        if (listpktstatus.size() <= 0 || listRcRelation.size() <= 0 || listRcRemarks.size() <= 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EntryForm.this);
            dialog.setTitle("Message");
            dialog.setMessage("Some data are not loaded to perform operation.Please check your network connection and try again.");
            dialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    // getting packet status
                                    if (DBObjSel.getRecordCount("PacketStatus") <= 0) {
                                        new FillRecordsInLocalDatabase(EntryForm.this).funDownPacketStatus(false);
                                    }
                                    // getting all Relation
                                    // -SP_Android_GetRCrelation
                                    if (DBObjSel.getRecordCount("RcRelation") <= 0) {
                                        new FillRecordsInLocalDatabase(EntryForm.this).funDownRcRelation(false);
                                    }
                                    // getting all remarks
                                    // -SP_Android_GetRCRemarks
                                    if (DBObjSel.getRecordCount("RcRemarks") <= 0) {
                                        new FillRecordsInLocalDatabase(EntryForm.this).funDownRcRemarks(false);
                                    }
                                    return null;
                                }
                            }.execute();
                            Intent i = new Intent(EntryForm.this, CaseListActivity.class);
                            i.putExtra("UserID", UserId);
                            onStop();
                            finish();
                            onDestroy();
                            startActivity(i);
                        }
                    });
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            // dialog.setNegativeButton("Cancel", null);
            Dialog alert = dialog.create();
            alert.setCanceledOnTouchOutside(false);
            if (isActivityOnFront)
                alert.show();
        }
    }

    private void CheckSystemUpdatedAwbNo(String awbNo2) {
        // Function To Check System Updated AwbNos
        if (!DBObjSel.CheckAwbStatus(awbNo2)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EntryForm.this);
            dialog.setTitle("Update Info.");
            dialog.setMessage("Already Updated from WebApp.!!");
            dialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(EntryForm.this, CaseListActivity.class);
                            i.putExtra("UserID", UserId);
                            FinishActivity();
                            startActivity(i);
                        }
                    });
            Dialog alert = dialog.create();
            alert.setCanceledOnTouchOutside(false);
            if (isActivityOnFront)
                alert.show();
            return;
        }
    }

    public void GetControls() {
        lblCodamount = (TextView) findViewById(R.id.lblCodamount);
        tvUserID = (TextView) findViewById(R.id.tvUserID);
        tblSaveRecordCEF = (TableLayout) findViewById(R.id.tblSaveRecordCEF);
        tblBackCEF = (TableLayout) findViewById(R.id.tblBackCEF);
        txtRcName = (EditText) findViewById(R.id.txtRCName);
        txtRcPhone = (EditText) findViewById(R.id.txtRCPhone);
        txtAwbNo = (EditText) findViewById(R.id.txtAwbNo);
        txtCODAmount = (EditText) findViewById(R.id.txtCODamount);
        //txtNegRemarks = (EditText) findViewById(R.id.txtNegRemarks);
        txtConsignee = (EditText) findViewById(R.id.txtConsignee);
        txtIDProof = (EditText) findViewById(R.id.txtIDProof);
        txtPosRemarks = (EditText) findViewById(R.id.txtPosRemarks);

        btnCapturePhoto = (Button) findViewById(R.id.btnCapturePhoto);
        btnCaptureSignature = (Button) findViewById(R.id.btnCaptureSignature);

        imgphoto = (ImageView) findViewById(R.id.ImgView);
        ImgSignature = (ImageView) findViewById(R.id.ImgSignature);
        imgphoto = (ImageView) findViewById(R.id.ImgView);

        spnpktStatus = (Spinner) findViewById(R.id.spnPktStatus);
        spnRCRelation = (Spinner) findViewById(R.id.spnRCRelation);
        spnRcRemarks = (Spinner) findViewById(R.id.spnRCReamrks);
        spnPaymentType = (Spinner) findViewById(R.id.spnPaymentType);
        spnIdProof = (Spinner) findViewById(R.id.spnIdProof);
        spnNegRemarks = (Spinner) findViewById(R.id.spnNegRemarks);

        tblRTO = (TableLayout) findViewById(R.id.tblRTO);
        tblrowRcRemarks = (TableRow) findViewById(R.id.tblrowRcRemarks);
        tblrowRcRelation = (LinearLayout) findViewById(R.id.tblrowRcRelation);
        tblrowRcName = (LinearLayout) findViewById(R.id.tblrowRcName);
        tblrowRcPhone = (LinearLayout) findViewById(R.id.tblrowRcPhone);
        tblrowPaymentType = (LinearLayout) findViewById(R.id.tblrowPaymentType);
        tblrowCODAmt = (LinearLayout) findViewById(R.id.tblrowCODAmt);
        tblrowCODRcAmount = (LinearLayout) findViewById(R.id.tblrowCODRcAmount);
        tblrowIDProff = (LinearLayout) findViewById(R.id.tblrowIDProff);
        tblrowIDProofNo = (LinearLayout) findViewById(R.id.tblrowIDProofNo);
        tblrowNegRemarks = (TableRow) findViewById(R.id.tblrowNegRemarks);
        tblPosRemarks = (LinearLayout) findViewById(R.id.tblPosRemarks);

        tvRecCodAmt = (TextView) findViewById(R.id.tvRecCodAmt);
        tvCodamount = (TextView) findViewById(R.id.tvCodamount);
        tvPaymentType = (TextView) findViewById(R.id.tvPaymentType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tblSaveRecordCEF:
                try {
                    if (validateCODForm()) {
                        isUpdateButtonPressed = true;
                        new AsyncWebServiceCall().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.tblBackCEF:
                Intent i = new Intent(EntryForm.this, CaseListActivity.class);
                i.putExtra("UserID", UserId);
                startActivity(i);
                finish();
                break;

            case R.id.btnCapturePhoto:
                captureImage();
                break;

            case R.id.btnCaptureSignature:
                Intent intent = new Intent(EntryForm.this, SignatureGesture.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
                break;
            default:
                break;
        }
    }

    private void CheckGPS() {
        // create class object
        gps = new sipl.bombino.gpstracker.GPSTracker(EntryForm.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());
        } else {
            // can't get location GPS or Network is not enabled Ask user to
            // enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private boolean IsGPSEnabled() {
        boolean isGPSEnabled = false;
        gps = new GPSTracker(EntryForm.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            isGPSEnabled = true;
        } else {
            gps.showSettingsAlert();
            isGPSEnabled = false;
        }
        return isGPSEnabled;
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = new ImageCaptureClass().getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file URL in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");
                    if (status.equalsIgnoreCase("done")) {
                        ImageView image = (ImageView) findViewById(R.id.ImgSignature);
                        ByteArrayInputStream imageStreamClient = new ByteArrayInputStream(data.getExtras().getByteArray("draw"));
                        image.setImageBitmap(BitmapFactory.decodeStream(imageStreamClient));
                        image.setBackgroundResource(R.drawable.imageborder);
                        byte[] b1 = data.getExtras().getByteArray("draw");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b1, 0, b1.length);
                        bitmapSignature = bitmap;
                    }
                }
                break;
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                ShowMessage("User cancelled image capture");
            } else {
                ShowMessage("User cancelled image capture");
            }
        }
    }

    private void previewCapturedImage() {
        try {
            imgphoto.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmapImg = BitmapFactory.decodeFile(fileUri.getPath(), options);
            imgphoto.setImageBitmap(bitmapImg);
            imgphoto.setBackgroundResource(R.drawable.imageborder);
        } catch (NullPointerException e) {
            if (CommonFunction.LOG)
                e.printStackTrace();
        }
    }

    public class AsyncWebServiceCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isUpdateButtonPressed == true) {
                SavePodEntry();
            } else {
                listpktstatus = DBObjSel.GetPacketStatus();
                listRcRelation = DBObjSel.GetRCRelation();
                listRcRemarks = DBObjSel.GetRCRemarks();
                listPODRemarksMaster = DBObjSel.GetPODRemarksMaster();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dismissDialog();
            doTask();
            if (isRecordSaved) {
                dbcopy.CopyDBToSDCard();
                AlertDialog.Builder dialog = new AlertDialog.Builder(EntryForm.this);
                dialog.setTitle("Message");
                dialog.setMessage("Record Saved Successfully.");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(EntryForm.this, CaseListActivity.class);
                        i.putExtra("UserID", UserId);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK;
                    }
                });
                Dialog alert = dialog.create();
                alert.setCanceledOnTouchOutside(false);
                if (isActivityOnFront) {
                    alert.show();
                }
                return;
            }
            if (!isUpdateButtonPressed)
                checkAllSpinner();
        }

        private void doTask() {
            listPaymentType.add("CASH");
            listPaymentType.add("CREDIT");
            BindSpinner(listPaymentType, spnPaymentType);
            listIDProof.add("Select Receiver ID Proof");
            listIDProof.add("Voter ID");
            listIDProof.add("Driving License");
            listIDProof.add("Pan Card");
            listIDProof.add("Adhar Card");
            listIDProof.add("Other");
            BindSpinner(listIDProof, spnIdProof);
            BindSpinner(listpktstatus, spnpktStatus);
            BindSpinner(listRcRemarks, spnRcRemarks);
            BindSpinner(listRcRelation, spnRCRelation);
            BindSpinner(listPODRemarksMaster, spnNegRemarks);
            if (listRcRelation.size() > 0)
                spnRCRelation.setSelection(getIndex(spnRCRelation, "SELF"));
        }
    }

    @SuppressWarnings("unused")
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.spnPktStatus:
                if (listpktstatus.size() > 0)
                    if (spnpktStatus.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        tblRTO.setVisibility(View.GONE);
                        tblrowRcRelation.setVisibility(View.GONE);
                        tblrowRcName.setVisibility(View.GONE);
                        tblrowRcRemarks.setVisibility(View.GONE);
                        tblrowNegRemarks.setVisibility(View.GONE);
                        tblPosRemarks.setVisibility(View.GONE);
                        imgphoto.setVisibility(View.GONE);
                        btnCapturePhoto.setVisibility(View.GONE);
                        hide_UnhideControls(false);
                    } else if (!spnpktStatus.getSelectedItem().toString().equalsIgnoreCase("DELIVERED")) {
                        tblRTO.setVisibility(View.VISIBLE);
                        tblrowRcRelation.setVisibility(View.GONE);
                        tblrowRcName.setVisibility(View.GONE);
                        tblrowRcRemarks.setVisibility(View.VISIBLE);
                        tblrowNegRemarks.setVisibility(View.VISIBLE);
                        tblPosRemarks.setVisibility(View.VISIBLE);
                        imgphoto.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 4);
                        param.setMargins(4, 18, 4, 4);
                        btnCapturePhoto.setLayoutParams(param);
                        btnCapturePhoto.setVisibility(View.VISIBLE);
                        hide_UnhideControls(false);
                    } else {
                        tblRTO.setVisibility(View.GONE);
                        tblrowRcRelation.setVisibility(View.VISIBLE);
                        tblrowRcRemarks.setVisibility(View.GONE);
                        tblrowNegRemarks.setVisibility(View.GONE);
                        if (spnRCRelation.getSelectedItem().toString().equalsIgnoreCase("SELF"))
                            tblrowRcName.setVisibility(View.GONE);
                        else
                            tblrowRcName.setVisibility(View.VISIBLE);
                        tblPosRemarks.setVisibility(View.VISIBLE);
                        imgphoto.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 2);
                        param.setMargins(4, 18, 4, 4);
                        btnCapturePhoto.setLayoutParams(param);
                        btnCapturePhoto.setVisibility(View.VISIBLE);
                        hide_UnhideControls(true);
                    }
                break;

            case R.id.spnRCRelation:
                if (listpktstatus.size() > 0 && listRcRelation.size() > 0)
                    if (spnRCRelation.getSelectedItem().toString().equalsIgnoreCase("SELF") && spnpktStatus.getSelectedItem().toString().equalsIgnoreCase("DELIVERED")) {
                        tblrowRcName.setVisibility(View.GONE);
                        txtRcName.setText(Consignee);
                    } else {
                        if (spnpktStatus.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                            tblrowRcName.setVisibility(View.GONE);
                        } else {
                            tblrowRcName.setVisibility(View.VISIBLE);
                            txtRcName.setText("");
                        }
                    }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public void hide_UnhideControls(boolean flag) {
        int val = View.VISIBLE;
        if (flag == false) {
            val = View.GONE;
        } else {
            val = View.VISIBLE;
        }
        tblrowCODAmt.setVisibility(val);
        tblrowCODRcAmount.setVisibility(val);
        tblrowPaymentType.setVisibility(val);
        tblrowRcPhone.setVisibility(val);
        tblrowIDProff.setVisibility(val);
        tblrowIDProofNo.setVisibility(val);
        ImgSignature.setVisibility(val);
        btnCaptureSignature.setVisibility(val);
    }

    public String BitmapToBase64StringConvertion(Bitmap myBitmap) {
        String base64Result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        base64Result = Base64.encodeToString(data, 0);
        return base64Result;
    }

    public boolean validateCODForm() {
        if (spnpktStatus.getSelectedItemPosition() == 0) {
            if (spnRCRelation.getSelectedItemPosition() == 0) {
                ShowMessage("Please Select Receiver Relation.");
                spnRCRelation.performClick();
                return false;
            }

            if (!spnRCRelation.getSelectedItem().toString().trim().equals("SELF")) {
                if (txtRcName.getText().toString().trim().equalsIgnoreCase("")) {
                    txtRcName.requestFocus();
                    ShowMessage("Please Enter Receiver Name.");
                    return false;
                }
            }
            if (spnRCRelation.getSelectedItem().toString().trim().equals("SELF")) {
                txtRcName.setText(txtConsignee.getText());
            }
            if (new DataBaseHandlerSelect(EntryForm.this).checkForValidation("PODPIC")) {
                if (bitmapImg == null) {
                    ShowMessage("Please Take Image.");
                    return false;
                }
            }
            if (new DataBaseHandlerSelect(EntryForm.this).checkForValidation("SIGNATURE")) {
                if (bitmapSignature == null) {
                    ShowMessage("Please Take Signature.");
                    return false;
                }
            }
        }
        if (spnpktStatus.getSelectedItemPosition() == 1) {
            if (spnRcRemarks.getSelectedItem().toString().equalsIgnoreCase("Select Receiver Remarks")) {
                ShowMessage("Please Select Receiver Remarks.");
                spnRcRemarks.performClick();
                return false;
            }
            /*if (spnNegRemarks.getSelectedItem().toString().equalsIgnoreCase("Select Negative Remarks")) {
                ShowMessage("Please Select Negative Remarks.");
                spnNegRemarks.performClick();
                return false;
            }*/
        }
        return true;
    }

    public boolean SavePodEntry() {
        try {
            String packetStatus, rcRelation, idProofType, rtoReason;
            packetStatus = spnpktStatus.getSelectedItem().toString().trim();
            rcRelation = spnRCRelation.getSelectedItem().toString().trim().equalsIgnoreCase("--Select--") ? "" : spnRCRelation.getSelectedItem().toString().trim();
            idProofType = spnIdProof.getSelectedItem().toString().trim().equalsIgnoreCase("Select Receiver ID Proof") ? "" : spnIdProof.getSelectedItem().toString().trim();
            rtoReason = spnRcRemarks.getSelectedItem().toString().equalsIgnoreCase("Select Receiver Remarks") ? "" : spnRcRemarks.getSelectedItem().toString().trim();
            if (bitmapImg != null) {
                base64img = BitmapToBase64StringConvertion(bitmapImg);
            } else {
                base64img = "";
            }
            if (bitmapSignature != null) {
                base64signature = BitmapToBase64StringConvertion(bitmapSignature);
            } else {
                base64signature = "";
            }
            if (packetStatus.equals("")) {
                isRecordSaved = false;
                return isRecordSaved;
            }
            if (spnpktStatus.getSelectedItemPosition() == 0) {
                final PodGetterSetter info = new PodGetterSetter();
                info.setSignature(base64signature);
                info.setPODPic(base64img);
                info.setAwbNo(txtAwbNo.getText().toString());
                info.setRCName(txtRcName.getText().toString().trim());
                info.setRCPhone(txtRcPhone.getText().toString().trim());
                info.setRCRelation(rcRelation);
                info.setPktStatus(packetStatus);
                info.setIMEINo(getDeviceId());
                info.setECode(UserId);
                info.setLatitude(latitude);
                info.setLongitude(longitude);
                info.setPaymentType(spnPaymentType.getSelectedItem().toString());
                info.setCODAmount(txtCODAmount.getText().toString().trim());
                info.setIDProofType(idProofType);
                info.setIDProofNo(txtIDProof.getText().toString());
                info.setPODRemarks(txtPosRemarks.getText().toString());
                info.setRunsheetNo(RunsheetNo);
                long result = DBObjUpd.updatePODDetailTable(info);
                if (result > 0) {
                    isRecordSaved = true;
                    if (DBObjUpd.upDatePodDetailTable(txtAwbNo.getText().toString(), packetStatus) > 0) {
                        addRecordInPodInsert(new PodInsertGetterSetter(
                                txtAwbNo.getText().toString().trim(),
                                txtConsignee.getText().toString().trim(),
                                tvCodamount.getText().toString().trim(),
                                txtCODAmount.getText().toString().trim(),
                                Address, Phone, packetStatus,
                                RunsheetDate, RunsheetNo,
                                DBObjSel.getCurrentDate(),
                                DBObjSel.getCurrentServerDate()));
                    }
                    if (new ConnectionDetector(EntryForm.this).isConnectingToInternet()) {
                        saveRecordOnLive(info);
                    } else
                        ShowMessage("Internet connection available right now,You may upload record later when connection available");
                }
            } else if (spnpktStatus.getSelectedItemPosition() == 1) {
                final PodGetterSetter info = new PodGetterSetter();
                info.setPODPic(base64img);
                info.setAwbNo(txtAwbNo.getText().toString());
                info.setPktStatus(packetStatus);
                info.setRCRemarks(rtoReason);
                info.setIMEINo(getDeviceId());
                info.setECode(UserId);
                info.setLatitude(latitude);
                info.setLongitude(longitude);
                info.setRunsheetNo(RunsheetNo);
                info.setUDRemarks(spnRcRemarks.getSelectedItem().toString().trim());//txtPosRemarks.getText().toString());
                info.setPODRemarks(spnNegRemarks.getSelectedItem().toString().equalsIgnoreCase("Select Negative Remarks") ? "" : spnNegRemarks.getSelectedItem().toString());
                long result = DBObjUpd.updatePODDetailTable(info);
                if (result > 0) {
                    isRecordSaved = true;
                    if (DBObjUpd.upDatePodDetailTable(txtAwbNo.getText().toString(), packetStatus) > 0) {
                        addRecordInPodInsert(new PodInsertGetterSetter(txtAwbNo.getText().toString().trim(), txtConsignee.getText().toString().trim(),
                                tvCodamount.getText().toString().trim(),
                                txtCODAmount.getText().toString().trim(), Address, Phone, packetStatus,
                                RunsheetDate, RunsheetNo,
                                DBObjSel.getCurrentDate(),
                                DBObjSel.getCurrentServerDate()));

                    }
                    if (new ConnectionDetector(EntryForm.this).isConnectingToInternet()) {
                        saveRecordOnLive(info);
                    } else
                        ShowMessage("Internet connection available right now,You may upload record later when connection available");
                }
            }
        } catch (Exception e) {
            isRecordSaved = false;
            if (CommonFunction.LOG)
                e.printStackTrace();
        }
        return isRecordSaved;
    }

    public void saveRecordOnLive(PodGetterSetter info) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        String JsonResult = ServiceRequestResponse.getJSONString(
                "Sp_Android_UpdatePacket",
                new String[]{
                        "@AwbNo", "@Delivery_Status", "@CodAmount",
                        "@DeliveryBoy", "@RCName", "@RcRelation", "@RcTime",
                        "@PaymentReceiveMode", "@Rto_Reason", "@Latitude",
                        "@Longitude", "@RcPhoneNo", "@IDProof",
                        "@IDProofNo", "@PodRemarks", "@UnDeliveredRemarks"
                },
                new String[]{
                        info.getAwbNo(), info.getPktStatus(), info.getCODAmount() == null ? "0" : info.getCODAmount(), info.getECode() == null ? "" : info.getECode(),
                        info.getRCName() == null ? "" : info.getRCName(), info.getRCRelation() == null ? "" : info.getRCRelation(), sdf.format(cal.getTime()),
                        info.getPaymentType() == null ? "" : info.getPaymentType(), info.getRCRemarks() == null ? "" : info.getRCRemarks(),
                        info.getLatitude() == null ? "" : info.getLatitude(), info.getLongitude() == null ? "" : info.getLongitude(),
                        info.getRCPhone() == null ? "" : info.getRCPhone(), info.getIDProofType() == null ? "" : info.getIDProofType(),
                        info.getIDProofNo() == null ? "" : info.getIDProofNo(), info.getPODRemarks() == null ? "" : info.getPODRemarks(), info.getUDRemarks() == null ? "" : info.getUDRemarks()},
                new String[]{"@PodImage", "@Signature"},
                new String[]{info.getPODPic() == null ? "" : info.getPODPic(), info.getSignature() == null ? "" : info.getSignature()}, null, "Request", "", null, EntryForm.this);
        if (null != JsonResult) {
            if (JsonResult != "") {
                try {
                    JSONArray jsonArr = new JSONArray(JsonResult);
                    if (jsonArr.length() != 0) {
                        for (int j = 0; j < jsonArr.length(); j++) {
                            JSONObject jsonNode = jsonArr.getJSONObject(j);
                            if (!jsonNode.has("Error")) {
                                if (jsonNode.getString("feed").equalsIgnoreCase("SUCCESS")) {
                                    DBObjUpd.funToUpdatePODDetail(info.getAwbNo());
                                    Status = "Success";
                                    // counting Total Updated
                                    TotalUpdated++;
                                    if (DBObjUpd.updatePODInsertTable(info.getAwbNo()) > 0) {
                                        //TODO
                                    }
                                    if (TotalUpdated > 0) {
                                        Notification();
                                    }
                                   /* new DataBaseHandlerUpdate(EntryForm.this).funToUpdatePODDetail(info.getAwbNo());
                                    if (new DataBaseHandlerUpdate(EntryForm.this).updatePODInsertTable(info.getAwbNo()) > 0) {
                                        //TODO
                                    }*/
                                } else if (jsonNode.getString("feed").equalsIgnoreCase("FAILED")) {
                                    Status = "Failed";
                                    //ShowMessage("Failed to update record on Live.");
                                    //new DataBaseHandlerUpdate(EntryForm.this).funToUpdatePODDetail(info.getAwbNo());
                                    //new DataBaseHandlerUpdate(EntryForm.this).updatePODInsertTable(info.getAwbNo());
                                }
                            } else {
                                Status = jsonNode.toString();
                                //ShowMessage(jsonNode.toString());
                            }
                        }
                    }
                } catch (JSONException e) {
                    if (CommonFunction.LOG)
                        e.printStackTrace();
                }
            }
        }
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

    // adding record in podInsertTable
    public long addRecordInPodInsert(PodInsertGetterSetter podInsInfo) {
        return DBObjIns.addRecordIntoPodInsertTable(podInsInfo);
    }

    public String getDeviceId() {
        String IMEINo = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEINo = telephonyManager.getDeviceId();
        } catch (Exception e) {
            if (CommonFunction.LOG)
                e.printStackTrace();
        }
        return IMEINo;
    }

    protected void BindSpinner(List<String> SpinnerList, Spinner spn) {
        ArrayAdapter<String> commonAdp = new ArrayAdapter<String>(EntryForm.this, android.R.layout.simple_spinner_item, SpinnerList);
        commonAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(commonAdp);
    }

    public void ShowMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EntryForm.this, CaseListActivity.class);
        i.putExtra("UserID", UserId);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityOnFront = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityOnFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityOnFront = false;
        dismissDialog();
        if (isRecordSaved) {
            FinishActivity();
        }
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
        //isRecordSaved=false;
    }

    public void FinishActivity() {
        onStop();
        finish();
        onDestroy();
    }

    public void Notification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(EntryForm.this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(EntryForm.this.getString(R.string.notificationticker))
                .setContentTitle(EntryForm.this.getString(R.string.notificationtitle))
                .setContentText((TotalUpdated) + " Packet(s) Updated On Live.")
                .setSound(alarmSound)
                .setAutoCancel(true);
        NotificationManager notificationmanager = (NotificationManager) EntryForm.this.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }
}

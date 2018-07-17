package sipl.bombino.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sipl.bombino.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sipl.bombino.dataadapter.MeterReaderAdapter;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.gpstracker.GPSTracker;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ImageCaptureClass;
import sipl.bombino.properties.MeterReadingInfo;

public class MeterReadingActivity extends Activity implements OnClickListener {
    String UserId = "", latitude = "", longitude = "";
    TextView tvUserID;
    EditText txtMeterReading;
    Button btnCapturePhoto;
    ImageView imgphoto;
    TableLayout tblSaveRecord, tblBack;
    private ProgressDialog pDialog;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    Bitmap bitmapImg = null;
    ListView MeterReadingList;
    List<MeterReadingInfo> mList = new ArrayList<MeterReadingInfo>();
    GPSTracker gps;
    static MeterReadingActivity instance;
    LinearLayout llNoMeterDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meter_reading);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        GetControls();
        Intent i = getIntent();
        UserId = i.getSerializableExtra("UserID").toString();
        tvUserID.setText("UserID [" + UserId + "]");

        //new AsyncWebServiceCall().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    public static MeterReadingActivity getInstance() {
        return instance;
    }

    public void GetControls() {
        tvUserID = (TextView) findViewById(R.id.tvUserID);
        btnCapturePhoto = (Button) findViewById(R.id.btnCapturePhoto);
        imgphoto = (ImageView) findViewById(R.id.ImgViewMeterReading);
        txtMeterReading = (EditText) findViewById(R.id.txtMeterReading);
        tblSaveRecord = (TableLayout) findViewById(R.id.tblSaveRecord);
        tblBack = (TableLayout) findViewById(R.id.tblBack);
        MeterReadingList = (ListView) findViewById(R.id.MeterReadingList);
        llNoMeterDetails = (LinearLayout) findViewById(R.id.llNoMeterDetails);
        btnCapturePhoto.setOnClickListener(this);
        tblBack.setOnClickListener(this);
        tblSaveRecord.setOnClickListener(this);
        refreshList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tblSaveRecord:
                if (CheckGPS()) {
                    if (ValidateEntryForm()) {
                        SavePodEntry();
                    }
                } else {
                    ShowMessage("can't get location,Please enable locataion and try again.");
                }
                break;
            case R.id.tblBack:
                Intent i = new Intent(MeterReadingActivity.this, OptionActivity.class);
                i.putExtra("UserID", UserId);
                startActivity(i);
                finish();
                break;
            case R.id.btnCapturePhoto:
                captureImage();
                break;
            default:
                break;
        }
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
        // save file url in bundle as it will be null on screen orientation
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
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        previewCapturedImage();
                    } else if (resultCode == RESULT_CANCELED) {
                        ShowMessage("User cancelled image capture");
                    } else {
                        ShowMessage("User cancelled image capture");
                    }
                }
                break;
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
            File _file = new File(fileUri.getPath());
            boolean r = _file.delete();
            Log.d("CODApp", "File Deleted=" + r);
        } catch (NullPointerException e) {
            if (CommonFunction.LOG)
                e.printStackTrace();
        }
    }

    public String BitmapToBase64StringConvertion(Bitmap myBitmap) {
        String base64Result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        base64Result = Base64.encodeToString(data, 0);
        return base64Result;
    }

    public boolean ValidateEntryForm() {
        if (bitmapImg == null) {
            ShowMessage("Please take meter reader image.");
            return false;
        }
        if (txtMeterReading.getText().toString().trim().equals("")) {
            txtMeterReading.requestFocus();
            ShowMessage("Please enter meter reader value");
            return false;
        }
        return true;
    }

    public void SavePodEntry() {
        if (new DataBaseHandlerInsert(MeterReadingActivity.this)
                .insertIntoMeterReading(new MeterReadingInfo(txtMeterReading.getText().toString().trim()
                        , BitmapToBase64StringConvertion(bitmapImg)
                        , new DataBaseHandlerSelect(MeterReadingActivity.this).getCurrentDate(), latitude, longitude)) > 0) {
            ShowMessage("Record Saved Successfully");
            ResetFrom();
            refreshList();
        }
    }

    public void refreshList() {
        mList = new DataBaseHandlerSelect(MeterReadingActivity.this).getMeterReadingList();
        MeterReaderAdapter adapter = new MeterReaderAdapter(MeterReadingActivity.this, mList);
        MeterReadingList.setAdapter(adapter);
        if (MeterReadingList.getCount() > 0) {
            MeterReadingList.setVisibility(View.VISIBLE);
            llNoMeterDetails.setVisibility(View.GONE);
        } else {
            MeterReadingList.setVisibility(View.GONE);
            llNoMeterDetails.setVisibility(View.VISIBLE);
        }
    }

    public void ResetFrom() {
        txtMeterReading.getText().clear();
        bitmapImg = null;
        imgphoto.setImageResource(0);
        imgphoto.setImageResource(android.R.color.transparent);
    }

    public void ShowMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MeterReadingActivity.this, CaseListActivity.class);
        i.putExtra("UserID", UserId);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private boolean CheckGPS() {
        gps = new sipl.bombino.gpstracker.GPSTracker(MeterReadingActivity.this);
        if (gps.canGetLocation()) {
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());
            return true;
        } else {
            gps.showSettingsAlert();
            return false;
        }
    }
}

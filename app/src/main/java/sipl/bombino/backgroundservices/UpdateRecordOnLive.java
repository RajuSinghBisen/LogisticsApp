package sipl.bombino.backgroundservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sipl.bombino.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.base.CaseListActivity;
import sipl.bombino.configuration.ApplicationConfiguration;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerInsert;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.databseOperation.DataBaseHandlerUpdate;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.properties.PodGetterSetter;

public class UpdateRecordOnLive extends Activity {
    ApplicationConfiguration appConfigObj;
    DataBaseHandlerUpdate DbObjUpd;
    DataBaseHandlerInsert DbObjIns;
    DataBaseHandlerSelect DbObjSel;
    DataBaseHandlerDelete DbObjDel;
    Context context;
    List<PodGetterSetter> list;
    int TotalUpdated = 0;
    String Awbno = "";
    boolean isActivityOnFront = false;
    private String Status = "";

    public UpdateRecordOnLive(Context context, String _awbNo) {
        this.context = context;
        Awbno = _awbNo;
        DbObjUpd = new DataBaseHandlerUpdate(this.context);
        DbObjIns = new DataBaseHandlerInsert(this.context);
        DbObjSel = new DataBaseHandlerSelect(this.context);
        DbObjDel = new DataBaseHandlerDelete(this.context);
        appConfigObj = new ApplicationConfiguration();
        isActivityOnFront = true;
        if (!Awbno.equals("")) {
            if (DbObjSel.getPodInsertedResultForUpdateInLive(Awbno).size() != 0) {
                new AsyncWebService().execute();
            } else {
                ToastMessage("Packet already updated.");
            }
        } else {
            new AsyncWebService().execute();
        }
    }

    public class AsyncWebService extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            if (Awbno.equals("")) {
                list = DbObjSel.getPodInsertedResultForUpdateInLive();
            } else {
                CaseListActivity.getInstance().showDialog();
                list = DbObjSel.getPodInsertedResultForUpdateInLive(Awbno);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (list.size() > 0) {
                funUpdateRecordOnLive();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            if (Status.equals("Failed")) {
                if (!Awbno.equals("")) {
                    CaseListActivity.getInstance().dismissDialog();
                }
                //ToastMessage("Failed to update record ");
                if (CaseListActivity.getInstance() != null && isActivityOnFront) {
                    CaseListActivity.getInstance().refreshPackets();
                }
                Awbno = "";
            }else if(Status.equalsIgnoreCase("Success")){
                if (!Awbno.equals("")) {
                    CaseListActivity.getInstance().dismissDialog();
                }
                if (TotalUpdated > 0)
                    Notification();
                if (CaseListActivity.getInstance() != null && isActivityOnFront) {
                    CaseListActivity.getInstance().refreshPackets();
                }
                Awbno = "";
            }else{
                if (!Awbno.equals("")) {
                    CaseListActivity.getInstance().dismissDialog();
                }
               // ToastMessage(Status);
                if (CaseListActivity.getInstance() != null && isActivityOnFront) {
                    CaseListActivity.getInstance().refreshPackets();
                }
                Awbno = "";
            }
        }
    }

    public String funUpdateRecordOnLive() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        Status = "";
        for (int i = 0; i < list.size(); i++) {
            final PodGetterSetter pod = list.get(i);
            String JsonResult = ServiceRequestResponse.getJSONString(
                    "Sp_Android_UpdatePacket",
                    new String[]{"@AwbNo", "@Delivery_Status", "@CodAmount",
                            "@DeliveryBoy", "@RCName", "@RcRelation", "@RcTime",
                            "@PaymentReceiveMode", "@Rto_Reason", "@Latitude",
                            "@Longitude", "@RcPhoneNo", "@IDProof",
                            "@IDProofNo", "@PodRemarks", "@UnDeliveredRemarks"},
                    new String[]{
                            pod.getAwbNo(), pod.getPktStatus(),
                            pod.getCODAmount(), pod.getECode(),
                            pod.getRCName(), pod.getRCRelation(),
                            sdf.format(cal.getTime()),pod.getPaymentType(),
                            pod.getRCRemarks(),pod.getLatitude(), pod.getLongitude(),
                            pod.getRCPhone(), pod.getIDProofType(),pod.getIDProofNo(),
                            pod.getPODRemarks(), pod.getUDRemarks()},
                    new String[]{"@PodImage", "@Signature"}, new String[]{
                            pod.getPODPic()== null ? "" : pod.getPODPic(), pod.getSignature()== null ? "" : pod.getSignature()}, null, "Request", "", null, context);
            if (null == JsonResult) {
                return "";
            }
            if (JsonResult != "") {
                try {
                    JSONArray jsonArr = new JSONArray(JsonResult);
                    if (jsonArr.length() != 0) {
                        for (int j = 0; j < jsonArr.length(); j++) {
                            JSONObject jsonNode = jsonArr.getJSONObject(j);
                            if (!jsonNode.has("Error")) {
                                if (jsonNode.getString("feed").equalsIgnoreCase("SUCCESS")) {
                                    DbObjUpd.funToUpdatePODDetail(pod.getAwbNo());
                                    Status = "Success";
                                    // counting Total Updated
                                    TotalUpdated++;

                                    if (DbObjUpd.updatePODInsertTable(pod.getAwbNo()) > 0) {
                                        //TODO
                                    }
                                } else if (jsonNode.getString("feed").equalsIgnoreCase("FAILED")) {
                                    Status = "Failed";
                                }
                            } else {
                               Status = jsonNode.toString();
                            }
                        }
                    }
                } catch (JSONException e) {
                    if (CommonFunction.LOG)
                        e.printStackTrace();
                }
            } else {
                if (!Awbno.equals(""))
                    ToastMessage("Not able to upload data" + "\n Please Try again later");
            }
        }
        return Status;
    }

    public void ToastMessage(String Msg) {
        Toast.makeText(context, Msg, Toast.LENGTH_LONG).show();
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
    protected void onPause() {
        super.onPause();
        isActivityOnFront = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityOnFront = false;
    }

    @Override
    protected void onDestroy() {
        isActivityOnFront = false;
        super.onDestroy();

    }

    public void Notification() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(context.getString(R.string.notificationticker))
                .setContentTitle(context.getString(R.string.notificationtitle))
                .setContentText((TotalUpdated) + " Packet(s) Updated On Live.")
                .setSound(alarmSound)
                .setAutoCancel(true);
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }
}

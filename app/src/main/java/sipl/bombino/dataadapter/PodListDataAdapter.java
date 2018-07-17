package sipl.bombino.dataadapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sipl.bombino.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import sipl.bombino.backgroundservices.UpdateRecordOnLive;
import sipl.bombino.base.EntryForm;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.PodGetterSetter;

public class PodListDataAdapter extends BaseAdapter {

    Context context;
    List<PodGetterSetter> list;
    boolean showControlOnPending, isFromUnDelivered;
    ConnectionDetector cd;
    Calendar ServerCurrentdate = null, MobileCurrentDate = Calendar.getInstance();
    String ServerCurrentDate = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public PodListDataAdapter(Context context, List<PodGetterSetter> list, boolean showControlOnPending, boolean isFromUnDelivered) {
        this.context = context;
        this.list = list;
        this.showControlOnPending = showControlOnPending;
        this.isFromUnDelivered = isFromUnDelivered;
        cd = new ConnectionDetector(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView Consignee;
        TextView AwBno;
        TextView RcName;
        TextView InvoiceAmount;
        TextView CodAmount;
        TextView Address;
        TextView Phone;
        TextView UpdatedOnLive;
        TextView RunSheetNo;
        TextView RunSheetDate;
        TextView CreatedDate;
        TextView ServerDate;
        TextView txtUndeliveredReason;
        ImageButton btnCall;
        ImageButton btnUpdateList;
        ImageButton btnupload;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.podtextview, parent, false);
            holder = new ViewHolder();
            holder.Consignee = (TextView) convertView.findViewById(R.id.txtconsignee);
            holder.AwBno = (TextView) convertView.findViewById(R.id.txtAwbno);
            holder.RcName = (TextView)convertView.findViewById(R.id.txtRcName);
            holder.InvoiceAmount = (TextView) convertView.findViewById(R.id.txtInvoiceAmount);
            holder.CodAmount = (TextView) convertView.findViewById(R.id.txtCodAmount);
            holder.Address = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.Phone = (TextView) convertView.findViewById(R.id.txtPhone);
            holder.UpdatedOnLive = (TextView) convertView.findViewById(R.id.txtUpdatedOnLive);
            holder.RunSheetNo = (TextView) convertView.findViewById(R.id.txtRunSheetNo);
            holder.RunSheetDate = (TextView) convertView.findViewById(R.id.txtRunSheetDate);
            holder.CreatedDate = (TextView) convertView.findViewById(R.id.txtCreatedDate);
            holder.ServerDate = (TextView) convertView.findViewById(R.id.txtServerDate);
            holder.txtUndeliveredReason = (TextView)convertView.findViewById(R.id.txtUndeliveredReason);
            holder.btnCall = (ImageButton) convertView.findViewById(R.id.btnCall);
            holder.btnUpdateList = (ImageButton) convertView.findViewById(R.id.btnUpdateList);
            holder.btnupload = (ImageButton) convertView.findViewById(R.id.btnupload);
            convertView.setTag(holder);

            holder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton tb = (ImageButton) v;
                    PodGetterSetter pod = (PodGetterSetter) tb.getTag();
                    if (pod.getPhone().toString().equalsIgnoreCase("Phone:")) {
                        Toast.makeText(context, "Phone No Does Not Exists...", Toast.LENGTH_SHORT).show();
                    } else if (pod.getPhone().toString().length() < 10) {
                        Toast.makeText(context, "Invalid Phone No To Call...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Calling..." + pod.getPhone(), Toast.LENGTH_SHORT).show();
                        String number = pod.getPhone().toString();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        callIntent.setData(Uri.parse("tel:" + number));
                        context.startActivity(callIntent);
                    }
                }
            });

            holder.btnUpdateList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton imagebtn = (ImageButton) v;
                    PodGetterSetter pod = (PodGetterSetter) imagebtn.getTag();
                    Intent i = new Intent(context, EntryForm.class);
                    i.putExtra("ConsigneeName", pod.getConsignee());
                    i.putExtra("AwbNo", pod.getAwbNo());
                    i.putExtra("CodAmount", pod.getInvoiceAmount());
                    i.putExtra("UserId", new DataBaseHandlerSelect(context).GetEcode());
                    i.putExtra("RunsheetNo", pod.getRunsheetNo());
                    i.putExtra("RunSheetDate", pod.getRunsheetDate());
                    i.putExtra("Address", pod.getAddress());
                    i.putExtra("Phone", pod.getPhone());
                    context.startActivity(i);
                    //((Activity) context).finish();
                }
            });

            holder.btnupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton tb = (ImageButton) v;
                    final PodGetterSetter pod = (PodGetterSetter) tb.getTag();
                    if (cd.isConnectingToInternet()) {
                        new UpdateRecordOnLive(context, pod.getAwbNo());
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Connection Error.");
                        dialog.setMessage("Sorry,No Internet Connection !\n Enable Internet and Try Again.");
                        dialog.setIcon(R.drawable.fail);
                        dialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        Dialog alert = dialog.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();
                    }
                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PodGetterSetter poditem = list.get(position);
        holder.Consignee.setText("Consignee: " + poditem.getConsignee());
        holder.AwBno.setText("AwbNo: " + poditem.getAwbNo());
        if (poditem.getInvoiceAmount() != null && (!poditem.getInvoiceAmount().equals("") && !poditem.getInvoiceAmount().equals("0.0"))) {
            holder.InvoiceAmount.setText("COD Amount: " + poditem.getInvoiceAmount());
        }
        holder.Address.setText("Address: " + poditem.getAddress());
        holder.Phone.setText("Phone: " + poditem.getPhone());
        holder.RunSheetNo.setText("Runsheet No: " + poditem.getRunsheetNo());
        if (showControlOnPending) {
            holder.btnCall.setVisibility(View.VISIBLE);
            holder.btnUpdateList.setVisibility(View.VISIBLE);
            holder.btnupload.setVisibility(View.GONE);
            holder.UpdatedOnLive.setVisibility(View.GONE);
            if (poditem.getInvoiceAmount() != null && (!poditem.getInvoiceAmount().equals("") && !poditem.getInvoiceAmount().equals("0.0"))) {
                holder.InvoiceAmount.setVisibility(View.VISIBLE);
                holder.InvoiceAmount.setText("COD Amount: " + poditem.getInvoiceAmount());
            }else{
                holder.InvoiceAmount.setVisibility(View.GONE);
            }
            holder.RunSheetNo.setVisibility(View.GONE);
            holder.RunSheetDate.setVisibility(View.GONE);
            holder.CreatedDate.setVisibility(View.GONE);
            holder.ServerDate.setVisibility(View.GONE);
            holder.CodAmount.setVisibility(View.GONE);
            holder.btnCall.setTag(poditem);
            holder.btnUpdateList.setTag(poditem);
        } else {
            if(!poditem.getPODRemarks().equals("")){
                holder.txtUndeliveredReason.setVisibility(View.VISIBLE);
                holder.txtUndeliveredReason.setText("Reason For Un-Delivery: "+poditem.getPODRemarks());
            }else{
                holder.txtUndeliveredReason.setVisibility(View.GONE);
            }

            if(!poditem.getRCName().equals("")){
                holder.RcName.setVisibility(View.VISIBLE);
                holder.RcName.setText("Receiver: "+poditem.getRCName());
            }else{
                holder.RcName.setVisibility(View.GONE);
            }
            holder.RunSheetDate.setVisibility(View.GONE);
            holder.CreatedDate.setVisibility(View.GONE);
            holder.ServerDate.setVisibility(View.GONE);
            if (isFromUnDelivered) {
                holder.InvoiceAmount.setVisibility(View.GONE);
            } else {
                holder.InvoiceAmount.setVisibility(View.VISIBLE);
            }
            holder.InvoiceAmount.setText("CodAmount: " + poditem.getCODAmount());
            holder.RunSheetDate.setText("Runsheet Date: " + poditem.getRunsheetDate());
            holder.CreatedDate.setText("Delivered Date: " + poditem.getCreatedDate());
            holder.ServerDate.setText("Server Fetch Date: " + poditem.getServerdate());
            holder.btnCall.setVisibility(View.GONE);
            holder.btnUpdateList.setVisibility(View.GONE);
            holder.UpdatedOnLive.setVisibility(View.VISIBLE);
            holder.CodAmount.setVisibility(View.GONE);
            holder.RunSheetNo.setVisibility(View.VISIBLE);
            if (poditem.getIsUpdatedOnLive().equals("0")) {
                holder.UpdatedOnLive.setText("IsUpdateOnLive: No");
                holder.btnupload.setVisibility(View.VISIBLE);
                holder.btnupload.setBackgroundResource(R.drawable.upload);
                holder.btnupload.setTag(poditem);
            } else {
                holder.UpdatedOnLive.setText("IsUpdateOnLive: Yes");
                holder.btnupload.setVisibility(View.VISIBLE);
                holder.btnupload.setBackgroundResource(R.drawable.uploaded_red);
                holder.btnupload.setTag(poditem);
            }
        }
        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.drawable.alternate_background);
        } else {
            convertView.setBackgroundResource(R.drawable.alternate_backgroundtwo);
        }
        return convertView;
    }
}

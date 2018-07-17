package sipl.bombino.dataadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sipl.bombino.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import sipl.bombino.Webservice.ServiceRequestResponse;
import sipl.bombino.base.MeterReadingActivity;
import sipl.bombino.databseOperation.DataBaseHandlerDelete;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import sipl.bombino.databseOperation.DataBaseHandlerUpdate;
import sipl.bombino.helper.CommonFunction;
import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.MeterReadingInfo;

public class MeterReaderAdapter extends BaseAdapter {

	Context context;
	List<MeterReadingInfo> list;
	ConnectionDetector cd;
	String jsonResult="";
	ProgressDialog pDialog=null;

	public MeterReaderAdapter(Context context, List<MeterReadingInfo> list) {
		this.context = context;
		this.list = list;
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
		TextView tvMeterReadingID;
		TextView txtMeterText;
		ImageView imgViewMeterImage;
		ImageView imgDeleteMeterImage;
		ImageView imgUploadMeterImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.meter_reader_templete,parent, false);
			holder = new ViewHolder();
			holder.tvMeterReadingID = (TextView) convertView.findViewById(R.id.tvMeterReadingID);
			holder.txtMeterText = (TextView) convertView.findViewById(R.id.txtMeterText);
			holder.imgViewMeterImage = (ImageView) convertView.findViewById(R.id.imgViewMeterImage);
			holder.imgDeleteMeterImage = (ImageView) convertView.findViewById(R.id.imgDeleteMeterImage);
			holder.imgUploadMeterImage = (ImageView) convertView.findViewById(R.id.imgUploadMeterImage);
			
			holder.imgViewMeterImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView imgtag = (ImageView) v;
					final MeterReadingInfo infoTag = (MeterReadingInfo) imgtag.getTag();
					loadPhoto(funConvertBase64ToImage(new DataBaseHandlerSelect(context).getMeterImage(infoTag.getId())));
				}
			});
			
			holder.imgDeleteMeterImage.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					ImageView imgtag = (ImageView) v;	
					final MeterReadingInfo infoTag = (MeterReadingInfo) imgtag.getTag();
					AlertDialog.Builder dialog=new AlertDialog.Builder(context);
					dialog.setCancelable(false);
					dialog.setTitle("Delete Confirmation");
					dialog.setMessage("Are you sure to delete this item?");
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(new DataBaseHandlerDelete(context).deleteMeterReaderValue(infoTag.getId())>0){
								showToastMessage("Item deleted Successfully");
								MeterReadingActivity.getInstance().refreshList();
							}else {
								showToastMessage("Item not deleted.Please try again");
							}
						}
					});
					dialog.setNegativeButton("Cancel", null);
					dialog.show();
				}
			});
			
			holder.imgUploadMeterImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView imgtag = (ImageView) v;
					final MeterReadingInfo infoTag = (MeterReadingInfo) imgtag.getTag();
					
					AlertDialog.Builder dialog=new AlertDialog.Builder(context);
					dialog.setCancelable(false);
					dialog.setTitle("Update Confirmation");
					dialog.setMessage("Are you sure to upload this item?");
					dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							if(!cd.isConnectingToInternet()){
								showToastMessage("No internet connection. Please enable your internet connection and try again.");
								return;
							}
							new AsyncTask<Void,Void,Void>() {
								
								@Override
								protected void onPreExecute() {
									pDialog=new ProgressDialog(context);
									pDialog.setMessage("Please wait...");
									pDialog.setCancelable(false);
									pDialog.show();
								}

								@Override
								protected Void doInBackground(Void... params) {
									jsonResult="";
									jsonResult= ServiceRequestResponse.getJSONString("SP_Android_InsertIntoMeterReading"
											, new String[] {"@MeterValue","@EmpCode","@Latitude","@Longitude"}
											, new String[] {infoTag.getMeterReadingText(),new DataBaseHandlerSelect(context).getUserID(),
													infoTag.getLatitude(),infoTag.getLongitude()}
											, new String[] {"@MeterImage"}
											, new String[] {infoTag.getMeterReadingImage()}
											, null
											, "Request"
											, null
											, null,context);
									return null;
								}
								
								@Override
								protected void onPostExecute(Void result) {
									if(pDialog.isShowing())
										pDialog.dismiss();
									
									if(jsonResult.equals("")){
										showToastMessage("Data not uploaded.Please try again later.");
										return;
									}
									
									if(!CommonFunction.IsJSONValid(jsonResult)){
										showToastMessage(jsonResult);
										return;
									}
									
									try {
										JSONArray jsonArr=new JSONArray(jsonResult);
										if(jsonArr.length()==0){
											showToastMessage("Error in establishing network .Please try again later.");
											return;
										}
										
										if(jsonArr.getJSONObject(0).has("Error")){
											showToastMessage(jsonArr.getJSONObject(0).getString("Error"));
											return;
										}
										
										if(jsonArr.getJSONObject(0).getString("Res").equalsIgnoreCase("Ins")){
											showToastMessage("Record uploaded successfully");
											new DataBaseHandlerUpdate(context).updateMeterReaderToOne(infoTag.getId());
											MeterReadingActivity.getInstance().refreshList();
										}else if(jsonArr.getJSONObject(0).getString("Res").equalsIgnoreCase("Fail")){
											showToastMessage("Record not uploaded.Please try again later");
										}else if(jsonArr.getJSONObject(0).getString("Res").equalsIgnoreCase("Dup")){
											showToastMessage("Record already uploaded on server.");
										}else {
											showToastMessage(jsonArr.getJSONObject(0).getString("Res"));
										}
											
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}.execute();
						}
					});
					dialog.setNegativeButton("Cancel", null);
					dialog.show();
					
				}
			});
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MeterReadingInfo mList = list.get(position);
		holder.tvMeterReadingID.setText(mList.getId());
		holder.txtMeterText.setText(mList.getMeterReadingText());
		if(mList.getIsUpdatedOnLive().equalsIgnoreCase("1")){
			holder.imgUploadMeterImage.setImageResource(R.drawable.ic_uploadmeterdatared);
		}else{
			holder.imgUploadMeterImage.setImageResource(R.drawable.ic_uploadmeterdata);
		}
		holder.imgViewMeterImage.setTag(mList);
		holder.imgDeleteMeterImage.setTag(mList);
		holder.imgUploadMeterImage.setTag(mList);
		
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.alternate_background);
		} else {
			convertView.setBackgroundResource(R.drawable.alternate_backgroundtwo);
		}
		return convertView;
	}
	
	@SuppressWarnings("static-access")
	private void loadPhoto(Bitmap bitmapImg) {
        
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,(ViewGroup)((Activity) context).findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageBitmap(bitmapImg);
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        imageDialog.create();
        imageDialog.show();     
    }
	
	public void showToastMessage(String msg){
		if(context!=null)
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
// converting String to Bitmap
	public Bitmap funConvertBase64ToImage(String base64String) {
		byte[] imageBytes = Base64.decode(base64String, Base64.NO_WRAP);
		InputStream in = new ByteArrayInputStream(imageBytes);
		Bitmap bimap = BitmapFactory.decodeStream(in);
		return bimap;
	}
}

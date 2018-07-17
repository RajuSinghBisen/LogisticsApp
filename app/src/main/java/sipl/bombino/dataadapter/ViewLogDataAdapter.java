package sipl.bombino.dataadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sipl.bombino.R;

import java.util.List;

import sipl.bombino.helper.ConnectionDetector;
import sipl.bombino.properties.AndroidLog;

public class ViewLogDataAdapter extends BaseAdapter {

	Context context;
	List<AndroidLog> list;
	ConnectionDetector cd;

	public ViewLogDataAdapter(Context context, List<AndroidLog> list) {
		this.context = context;
		this.list = list;
		cd=new ConnectionDetector(context);
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
		TextView txtLogTextLog;
		TextView txtRequestTimeLog;
		TextView txtResponseTimeLog;
		TextView txtTypeLog;
		TextView txtCreatedDateLog;
		TextView txtConnectionUsedLog;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.viewloglist_templete, parent, false);
			holder = new ViewHolder();
			holder.txtLogTextLog = (TextView) convertView.findViewById(R.id.txtLogTextLog);
			holder.txtRequestTimeLog = (TextView) convertView.findViewById(R.id.txtRequestTimeLog);
			holder.txtResponseTimeLog = (TextView) convertView.findViewById(R.id.txtResponseTimeLog);
			holder.txtTypeLog = (TextView) convertView.findViewById(R.id.txtTypeLog);
			holder.txtCreatedDateLog = (TextView) convertView.findViewById(R.id.txtCreatedDateLog);
			holder.txtConnectionUsedLog=(TextView) convertView.findViewById(R.id.txtConnectionUsedLog);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AndroidLog LogList = list.get(position);
		holder.txtLogTextLog.setText("Log:" + LogList.getLogText());
		holder.txtRequestTimeLog.setText("RequesTime:" + LogList.getServiceRequestTime());
		holder.txtResponseTimeLog.setText("ResponseTime:" + LogList.getServiceResponseTime());
		holder.txtTypeLog.setText("TypeLog:" + LogList.getLogType());
		holder.txtCreatedDateLog.setText("CreatedDate:" + LogList.getCreatedDate());
		holder.txtConnectionUsedLog.setText("ConnectionUsed:" + LogList.getConnectionUsed());
		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.alternate_background);
		} else {
			convertView.setBackgroundResource(R.drawable.alternate_backgroundtwo);
		}
		return convertView;
	}
}

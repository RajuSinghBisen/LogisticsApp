package sipl.bombino.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import sipl.bombino.helper.ConnectionDetector;

public class BackgroundService extends Service {

	ConnectionDetector cd;
	UpdateRecordOnLive updObj;
	CheckPacketStatus pktStatusObj;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onStart(Intent intent, int startId) {
		cd = new ConnectionDetector(BackgroundService.this);
		if (cd.isConnectingToInternet()) {
			updObj = new UpdateRecordOnLive(BackgroundService.this,"");
			pktStatusObj = new CheckPacketStatus(BackgroundService.this);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}

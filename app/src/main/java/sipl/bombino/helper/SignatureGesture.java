package sipl.bombino.helper;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.sipl.bombino.R;

import java.io.ByteArrayOutputStream;

public class SignatureGesture extends Activity {

	private Button button_save, button_cancel;
	private GestureOverlayView gesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.signature_gestureoverlaypopup);
		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int Width = display.getWidth();
		@SuppressWarnings("deprecation")
		int Height = display.getHeight();

		WindowManager.LayoutParams params = getWindow().getAttributes();
		// params.x = -100;
		params.height = ((Height / 2) + (Height / 4));
		params.width = ((Width / 2) + (Width / 3));
		// params.y = -50;
		this.getWindow().setAttributes(params);
		this.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

		gesture = (GestureOverlayView) findViewById(R.id.gestures);
		button_save = (Button) findViewById(R.id.getsign);
		button_cancel = (Button) findViewById(R.id.cancel);
		button_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					Bitmap gestureImg = gesture.getGesture().toBitmap(100, 100,
							8, Color.BLACK);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					gestureImg.compress(Bitmap.CompressFormat.PNG, 100, bos);
					byte[] bArray = bos.toByteArray();
					Bundle b = new Bundle();
					b.putString("status", "done");
					Intent intent = new Intent();
					intent.putExtra("draw", bArray);
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(SignatureGesture.this,
							"Signature Gesture Exception", 3000).show();
				}
			}
		});
		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("status", "Cancel");
				Intent intent = new Intent();
				// intent.putExtra("draw", bArray);
				intent.putExtras(b);
				setResult(RESULT_CANCELED, intent);
				finish();

			}
		});

	}

}

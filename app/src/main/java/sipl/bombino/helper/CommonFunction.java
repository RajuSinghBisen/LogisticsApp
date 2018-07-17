package sipl.bombino.helper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sipl.bombino.databseOperation.DataBaseHandlerSelect;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class CommonFunction {

	Calendar ServerCurrentdate = null;
	static Calendar MobileCurrentDate = Calendar.getInstance();
	private static String ServerCurrentDate = "";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	public static final boolean LOG=false;

	public enum Month {
		Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
	};

	// this Function will accept date in 1-1-2015 format and return in
	// 1-Jan-2015 Format
	public String getDateFormatddMMMYYYY(String DateStr) {
		String dateFormat = "";
		String[] DateStrArr = DateStr.split("-");
		String mon = Month.values()[Integer.parseInt(DateStrArr[1]) - 1].name();
		dateFormat = DateStrArr[0] + "-" + mon + "-" + DateStrArr[2];
		return dateFormat;
	}

	// this function will return random number of six digit
	public String getRandomOTPNumber() {
		long number = (long) Math.floor(Math.random() * 900000L) + 100000L;
		return (number + "").trim();
	}

	// this function will return Mobile model
	public String getDeviceName() {
		String Manufacturer = Build.MANUFACTURER;
		String Model = Build.MODEL;
		if (Model.startsWith(Manufacturer)) {
			return capitalize(Model);
		} else {
			return capitalize(Manufacturer) + " " + Model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	// this function will return available internal memory
	@SuppressWarnings("deprecation")
	public String getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return formatSize(availableBlocks * blockSize);
	}

	// this function will return Total Internal memory
	@SuppressWarnings("deprecation")
	public String getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return formatSize(totalBlocks * blockSize);
	}

	// this function will return Available External memory
	@SuppressWarnings("deprecation")
	public String getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return formatSize(availableBlocks * blockSize);
		} else {
			return "No External Memory";
		}
	}

	// this function will return Total External memory
	@SuppressWarnings("deprecation")
	public String getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return formatSize(totalBlocks * blockSize);
		} else {
			return "No External Memory";
		}
	}

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String formatSize(long size) {
		String suffix = null;
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	public static String GetCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime()); // 14/09/2015 16:00:22
	}

	/**
	 * this function will check mobile date and Server date if both are same
	 * then return true otherwise false
	 */
	public static boolean checkMobileAndServerDate(Context context) {
		boolean flag = false;
		DataBaseHandlerSelect dbObjSel = new DataBaseHandlerSelect(context);
		ServerCurrentDate = dbObjSel.getCurrentServerDate();
		if (sdf.format(MobileCurrentDate.getTime()).equals(ServerCurrentDate)) {
			flag=true;
		}
		else{
			flag=false;
		}
		return flag;
	}
	
	public static boolean IsJSONValid(String strFormat) {
		try {
			new JSONObject(strFormat);
		} catch (JSONException e) {
			try {
				new JSONArray(strFormat);
			} catch (JSONException e1) {
				return false;
			}
		}
		return true;
	}
	
	
}

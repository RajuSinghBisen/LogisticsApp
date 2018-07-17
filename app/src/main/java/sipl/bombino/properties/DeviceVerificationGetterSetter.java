package sipl.bombino.properties;

public class DeviceVerificationGetterSetter {

	int _id;
	String _Status;
	String Serverdate;

	public DeviceVerificationGetterSetter() {

	}

	public DeviceVerificationGetterSetter(int id, String Status) {
		this._id = id;
		this._Status = Status;
	}

	public DeviceVerificationGetterSetter(String Status) {
		this._Status = Status;
	}

	public void setDeviceID(int id) {
		this._id = id;
	}

	public int getDeviceID() {
		return this._id;
	}

	public void setDeviceStatus(String Status) {
		this._Status = Status;
	}

	public String getDeviceStatus() {
		return this._Status;
	}

	public String getServerdate() {
		return Serverdate;
	}

	public void setServerdate(String serverdate) {
		Serverdate = serverdate;
	}
}

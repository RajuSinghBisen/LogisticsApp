package sipl.bombino.properties;

public class DeviceInfo {
	
	int id;
	String Ecode;
	String IMEINo;
	String DeviceName;
	String InternalStorage;
	String AvailableInternaleStorage;
	String ExternalStorage;
	String AvailableExternalStorage;
	String CreatedDate;
	String ServerDate;

	public DeviceInfo() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEcode() {
		return Ecode;
	}

	public void setEcode(String ecode) {
		Ecode = ecode;
	}

	public String getIMEINo() {
		return IMEINo;
	}

	public void setIMEINo(String iMEINo) {
		IMEINo = iMEINo;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getInternalStorage() {
		return InternalStorage;
	}

	public void setInternalStorage(String internalStorage) {
		InternalStorage = internalStorage;
	}

	public String getAvailableInternaleStorage() {
		return AvailableInternaleStorage;
	}

	public void setAvailableInternaleStorage(String availableInternaleStorage) {
		AvailableInternaleStorage = availableInternaleStorage;
	}

	public String getExternalStorage() {
		return ExternalStorage;
	}

	public void setExternalStorage(String externalStorage) {
		ExternalStorage = externalStorage;
	}

	public String getAvailableExternalStorage() {
		return AvailableExternalStorage;
	}

	public void setAvailableExternalStorage(String availableExternalStorage) {
		AvailableExternalStorage = availableExternalStorage;
	}

	public String getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}

	public String getServerDate() {
		return ServerDate;
	}

	public void setServerDate(String serverDate) {
		ServerDate = serverDate;
	}
}

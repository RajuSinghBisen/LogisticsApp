package sipl.bombino.properties;

public class AndroidLog {
	
	int id;
	String Ecode;
	String LogText;
	String ServiceRequestTime;
	String ServiceResponseTime;
	String LogType;
	String IsUpdatedOnLive;
	String ConnectionUsed;
	String CreatedDate;
	String ServerDate;
	
	public AndroidLog() {
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

	public String getLogText() {
		return LogText;
	}

	public void setLogText(String logText) {
		LogText = logText;
	}

	public String getServiceRequestTime() {
		return ServiceRequestTime;
	}

	public void setServiceRequestTime(String serviceRequestTime) {
		ServiceRequestTime = serviceRequestTime;
	}

	public String getServiceResponseTime() {
		return ServiceResponseTime;
	}

	public void setServiceResponseTime(String serviceResponseTime) {
		ServiceResponseTime = serviceResponseTime;
	}

	public String getLogType() {
		return LogType;
	}

	public void setLogType(String logType) {
		LogType = logType;
	}

	public String getIsUpdatedOnLive() {
		return IsUpdatedOnLive;
	}

	public void setIsUpdatedOnLive(String isUpdatedOnLive) {
		IsUpdatedOnLive = isUpdatedOnLive;
	}

	public String getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}

	public String getConnectionUsed() {
		return ConnectionUsed;
	}

	public void setConnectionUsed(String connectionUsed) {
		ConnectionUsed = connectionUsed;
	}

	public String getServerDate() {
		return ServerDate;
	}

	public void setServerDate(String serverDate) {
		ServerDate = serverDate;
	}
}

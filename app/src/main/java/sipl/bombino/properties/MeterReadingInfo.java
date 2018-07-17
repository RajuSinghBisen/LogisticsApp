package sipl.bombino.properties;

public class MeterReadingInfo {

	String id;
	String MeterReadingText;
	String MeterReadingImage;
	String CreatedDate;
	String IsUpdatedOnLive;
	String latitude;
	String longitude;
	

	public MeterReadingInfo() {
		super();
	}

	public MeterReadingInfo(String meterReadingText, String meterReadingImage
			,String createdDate,String latitude,String longitude) {
		super();
		MeterReadingText = meterReadingText;
		MeterReadingImage = meterReadingImage;
		CreatedDate = createdDate;
		this.latitude=latitude;
		this.longitude=longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}

	public String getMeterReadingText() {
		return MeterReadingText;
	}

	public void setMeterReadingText(String meterReadingText) {
		MeterReadingText = meterReadingText;
	}

	public String getMeterReadingImage() {
		return MeterReadingImage;
	}

	public void setMeterReadingImage(String meterReadingImage) {
		MeterReadingImage = meterReadingImage;
	}

	public String getIsUpdatedOnLive() {
		return IsUpdatedOnLive;
	}

	public void setIsUpdatedOnLive(String isUpdatedOnLive) {
		IsUpdatedOnLive = isUpdatedOnLive;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}

package sipl.bombino.properties;

public class PodGetterSetter {

	int id;
	String AwbNo;
	String InvoiceAmount;
	String Consignee;
	String RunsheetNo;
	String RunsheetDate;
	String IsDownonPDA;
	String Address;
	String Phone;
	String IsUpdated;
	String PktStatus;
	String IsUpdatedOnLive;
	String Signature;
	String PODPic;
	String RCName;
	String RCPhone;
	String RCRelation;
	String RCRemarks;
	String IMEINo;
	String PaymentType;
	String CODAmount;
	String Latitude;
	String Longitude;
	String DeliveryTime;
	String IDProofType;
	String IDProofNo;
	String PODRemarks;
	String ECode;
	String CreatedDate;
	String Serverdate;
	String UDRemarks;

	public PodGetterSetter() {
		super();
	}

	public PodGetterSetter(String consignee, String awbNo, String invoiceAmount,
			String address, String phone, String runsheetDate,String runsheetNo,
			String isDownOnPDA, String createdDate, String serverDate) {
		this.Consignee=consignee;
		this.AwbNo=awbNo;
		this.InvoiceAmount=invoiceAmount;
		this.Address=address;
		this.Phone=phone;
		this.RunsheetDate=runsheetDate;
		this.RunsheetNo=runsheetNo;
		this.IsDownonPDA=isDownOnPDA;
		this.CreatedDate=createdDate;
		this.Serverdate=serverDate;
		
	}

	public String getUDRemarks() {
		return UDRemarks;
	}

	public void setUDRemarks(String UDRemarks) {
		this.UDRemarks = UDRemarks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAwbNo() {
		return AwbNo;
	}

	public void setAwbNo(String awbNo) {
		AwbNo = awbNo;
	}

	public String getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		InvoiceAmount = invoiceAmount;
	}

	public String getConsignee() {
		return Consignee;
	}

	public void setConsignee(String consignee) {
		Consignee = consignee;
	}

	public String getRunsheetNo() {
		return RunsheetNo;
	}

	public void setRunsheetNo(String runsheetNo) {
		RunsheetNo = runsheetNo;
	}

	public String getRunsheetDate() {
		return RunsheetDate;
	}

	public void setRunsheetDate(String runsheetDate) {
		RunsheetDate = runsheetDate;
	}

	public String getIsDownonPDA() {
		return IsDownonPDA;
	}

	public void setIsDownonPDA(String isDownonPDA) {
		IsDownonPDA = isDownonPDA;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getIsUpdated() {
		return IsUpdated;
	}

	public void setIsUpdated(String isUpdated) {
		IsUpdated = isUpdated;
	}

	public String getPktStatus() {
		return PktStatus;
	}

	public void setPktStatus(String pktStatus) {
		PktStatus = pktStatus;
	}

	public String getIsUpdatedOnLive() {
		return IsUpdatedOnLive;
	}

	public void setIsUpdatedOnLive(String isUpdatedOnLive) {
		IsUpdatedOnLive = isUpdatedOnLive;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

	public String getPODPic() {
		return PODPic;
	}

	public void setPODPic(String pODPic) {
		PODPic = pODPic;
	}

	public String getRCName() {
		return RCName;
	}

	public void setRCName(String rCName) {
		RCName = rCName;
	}

	public String getRCPhone() {
		return RCPhone;
	}

	public void setRCPhone(String rCPhone) {
		RCPhone = rCPhone;
	}

	public String getRCRelation() {
		return RCRelation;
	}

	public void setRCRelation(String rCRelation) {
		RCRelation = rCRelation;
	}

	public String getRCRemarks() {
		return RCRemarks;
	}

	public void setRCRemarks(String rCRemarks) {
		RCRemarks = rCRemarks;
	}

	public String getIMEINo() {
		return IMEINo;
	}

	public void setIMEINo(String iMEINo) {
		IMEINo = iMEINo;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	public String getCODAmount() {
		return CODAmount;
	}

	public void setCODAmount(String cODAmount) {
		CODAmount = cODAmount;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getDeliveryTime() {
		return DeliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		DeliveryTime = deliveryTime;
	}

	public String getIDProofType() {
		return IDProofType;
	}

	public void setIDProofType(String iDProofType) {
		IDProofType = iDProofType;
	}

	public String getIDProofNo() {
		return IDProofNo;
	}

	public void setIDProofNo(String iDProofNo) {
		IDProofNo = iDProofNo;
	}

	public String getPODRemarks() {
		return PODRemarks;
	}

	public void setPODRemarks(String pODRemarks) {
		PODRemarks = pODRemarks;
	}

	public String getECode() {
		return ECode;
	}

	public void setECode(String eCode) {
		ECode = eCode;
	}

	public String getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}

	public String getServerdate() {
		return Serverdate;
	}

	public void setServerdate(String serverdate) {
		Serverdate = serverdate;
	}
}

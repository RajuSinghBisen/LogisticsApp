package sipl.bombino.properties;

public class PodInsertGetterSetter {

	int id;
	String AwbNo;
	String Consignee;
	String CODAmount;
	String InvoiceAmount;
	String Address;
	String Phone;
	String PktStatus;
	String IsUpDatedOnLive;
	String RunSheetDate;
	String RunSheetNo;
	String CreatedDate;
	String ServerDate;

	public PodInsertGetterSetter() {
		super();
	}

	public PodInsertGetterSetter(String awbNo, String consignee,String invoiceAmount,
			String cODAmount, String address, String phone, String pktStatus,
			 String runSheetDate, String runSheetNo,
			String createdDate, String serverDate) {
		super();
		AwbNo = awbNo;
		Consignee = consignee;
		InvoiceAmount=invoiceAmount;
		CODAmount = cODAmount;
		Address = address;
		Phone = phone;
		PktStatus = pktStatus;
		RunSheetDate = runSheetDate;
		RunSheetNo = runSheetNo;
		CreatedDate = createdDate;
		ServerDate = serverDate;
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

	public String getConsignee() {
		return Consignee;
	}

	public void setConsignee(String consignee) {
		Consignee = consignee;
	}

	public String getCODAmount() {
		return CODAmount;
	}

	public void setCODAmount(String cODAmount) {
		CODAmount = cODAmount;
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

	public String getPktStatus() {
		return PktStatus;
	}

	public void setPktStatus(String pktStatus) {
		PktStatus = pktStatus;
	}

	public String getIsUpDatedOnLive() {
		return IsUpDatedOnLive;
	}

	public void setIsUpDatedOnLive(String isUpDatedOnLive) {
		IsUpDatedOnLive = isUpDatedOnLive;
	}

	public String getRunSheetDate() {
		return RunSheetDate;
	}

	public void setRunSheetDate(String runSheetDate) {
		RunSheetDate = runSheetDate;
	}

	public String getRunSheetNo() {
		return RunSheetNo;
	}

	public void setRunSheetNo(String runSheetNo) {
		RunSheetNo = runSheetNo;
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

	public String getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		InvoiceAmount = invoiceAmount;
	}
}

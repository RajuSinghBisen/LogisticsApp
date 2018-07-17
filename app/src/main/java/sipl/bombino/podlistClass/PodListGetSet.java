package sipl.bombino.podlistClass;

public class PodListGetSet {

	String IBCode;
	String AWbNo;
	String Consignee;
	String DepartmentCode;
	String CodAmount;
	String Address;
	String Phone;
	String IsUpdateOnLive;
	boolean CheckBox;

	public void setIBCode(String _IBCode) {
		IBCode = _IBCode;
	}

	public String GetIBCode() {
		return IBCode;
	}

	public void setAwbNo(String _AwbNo) {
		AWbNo = _AwbNo;
	}

	public String GetAwbNo() {
		return AWbNo;
	}

	public void setConsignee(String _Consignee) {
		Consignee = _Consignee;
	}

	public String getConsignee() {
		return Consignee;
	}

	public void setDepartmentcode(String _DepartmentCode) {
		DepartmentCode = _DepartmentCode;
	}

	public String getDepartmentCode() {
		return DepartmentCode;
	}

	public String getCodAmount() {
		return CodAmount;
	}

	public void setCodAmount(String _CODAmount) {
		CodAmount = _CODAmount;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String _Address) {
		Address = _Address;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String _Phone) {
		Phone = _Phone;
	}

	public void setCheckBox(boolean _CheckBox) {
		CheckBox = _CheckBox;
	}

	public boolean getCheckBox() {
		return CheckBox;
	}
	
	public void setIsUpdateOnLive(String IsUpdateOnLive) {
		this.IsUpdateOnLive = IsUpdateOnLive;
	}

	public String  getIsUpdateOnLive() {
		return IsUpdateOnLive;
	}
	
	

}

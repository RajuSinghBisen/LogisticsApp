package sipl.bombino.properties;

public class GetRemarksGetterSetter {

	int _id;
	String _RcRemarkCode;
	String _RcRemark;

	public GetRemarksGetterSetter() {

	}

	public GetRemarksGetterSetter(int id, String RcRemarksCode, String RcRemark) {
		this._id = id;
		this._RcRemarkCode = RcRemarksCode;
		this._RcRemark = RcRemark;
	}

	public GetRemarksGetterSetter(String RcRemarksCode, String RcRemark) {
		this._RcRemarkCode = RcRemarksCode;
		this._RcRemark = RcRemark;
	}

	public String getRcRemarkCode() {
		return this._RcRemarkCode;
	}

	public void setRcRemarkCode(String RcRemarkCode) {
		this._RcRemarkCode = RcRemarkCode;
	}

	public String getRcRemark() {
		return this._RcRemark;
	}

	public void setRcRemark(String RcRemark) {
		this._RcRemark = RcRemark;
	}

}

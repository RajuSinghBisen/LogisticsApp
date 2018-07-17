package sipl.bombino.properties;

public class GetRcRelationGetterSetter {

	int _id;
	String _RcRelationCode;
	String _RcRelation;

	public GetRcRelationGetterSetter() {

	}

	public GetRcRelationGetterSetter(int id, String RcRelationCode,
			String RcRelation) {
		this._id = id;
		this._RcRelationCode = RcRelationCode;
		this._RcRelation = RcRelation;
	}

	public GetRcRelationGetterSetter(String RcRelationCode, String RcRelation) {
		this._RcRelationCode = RcRelationCode;
		this._RcRelation = RcRelation;
	}

	public String getRcRelationCode() {
		return this._RcRelationCode;
	}

	public void setRcRelationCode(String RcRelationCode) {
		this._RcRelationCode = RcRelationCode;
	}

	public String getRcRelation() {
		return this._RcRelation;
	}

	public void setRcRelation(String RcRelation) {
		this._RcRelation = RcRelation;
	}

}

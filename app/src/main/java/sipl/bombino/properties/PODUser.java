package sipl.bombino.properties;

public class PODUser {
	String _UserID;
	String _UserName;
	String _Password;
	String _iemeino;
	String _AppVersion;
	String _ServerDate;

	public PODUser(String UserID,String UserName, String Password,String IEMEINO,String appVersion,String serverDate) {
		this._UserID = UserID;
		this._UserName = UserName;
		this._Password = Password;
		this._iemeino=IEMEINO;
		this._AppVersion=appVersion;
		this._ServerDate=serverDate;
	}

	public PODUser() {
		// TODO Auto-generated constructor stub
	}

	public void setUserID(String UserID) {
		this._UserID = UserID;
	}

	public String getUserID() {
		return this._UserID;
	}

	public String get_UserName() {
		return _UserName;
	}

	public void set_UserName(String _UserName) {
		this._UserName = _UserName;
	}

	public void setPassword(String Password) {
		this._Password = Password;
	}

	public String getPassword() {
		return this._Password;
	}
	public void setIEMEINO(String IEMEINO) {
		this._iemeino = IEMEINO;
	}

	public String getIEMEINO() {
		return this._iemeino;
	}

	public String get_AppVersion() {
		return _AppVersion;
	}

	public void set_AppVersion(String _AppVersion) {
		this._AppVersion = _AppVersion;
	}

	public String get_ServerDate() {
		return _ServerDate;
	}

	public void set_ServerDate(String _ServerDate) {
		this._ServerDate = _ServerDate;
	}
}

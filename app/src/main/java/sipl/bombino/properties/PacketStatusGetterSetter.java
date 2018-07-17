package sipl.bombino.properties;

public class PacketStatusGetterSetter {

	int _id;
	String _PacketStatusCode;
	String _PacketStatus;

	public PacketStatusGetterSetter() {

	}

	public PacketStatusGetterSetter(int id, String PacketStatusCode,
			String PacketStatus) {
		this._id = id;
		this._PacketStatusCode = PacketStatusCode;
		this._PacketStatus = PacketStatus;
	}

	public PacketStatusGetterSetter(String PacketStatusCode, String PacketStatus) {
		this._PacketStatusCode = PacketStatusCode;
		this._PacketStatus = PacketStatus;
	}

	public String getPacketStatus() {
		return this._PacketStatus;
	}

	public void setPacketStatus(String PacketStatus) {
		this._PacketStatus = PacketStatus;
	}

	public String getPacketStatusCode() {
		return this._PacketStatusCode;
	}

	public void setPacketStatusCode(String PacketStatusCode) {
		this._PacketStatusCode = PacketStatusCode;
	}
}

package sipl.bombino.properties;

public class PacketStatusDetailGetterSetter {

	int _id;
	String _Packets;

	// Default constructor
	public PacketStatusDetailGetterSetter() {

	}

	// Parameterized constructor one
	public PacketStatusDetailGetterSetter(int id, String Packets) {
		this._id = id;
		this._Packets = Packets;
	}

	// Parameterized constructor two
	public PacketStatusDetailGetterSetter(String Packets) {
		this._Packets = Packets;
	}

	public String getPacket() {
		return this._Packets;
	}

	public void setPacket(String Packet) {
		this._Packets = Packet;
	}
}

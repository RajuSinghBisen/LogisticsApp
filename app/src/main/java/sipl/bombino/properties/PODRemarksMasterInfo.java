package sipl.bombino.properties;

public class PODRemarksMasterInfo {

	int _id;
	String podRemarks;
	
	public PODRemarksMasterInfo() {
	
	}
	
	public PODRemarksMasterInfo(String podRemarks) {
		super();
		this.podRemarks = podRemarks;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPodRemarks() {
		return podRemarks;
	}

	public void setPodRemarks(String podRemarks) {
		this.podRemarks = podRemarks;
	}
}

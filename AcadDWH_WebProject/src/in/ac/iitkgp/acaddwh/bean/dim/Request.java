package in.ac.iitkgp.acaddwh.bean.dim;

public class Request {
	private String requestKey;
	private String instituteKey;
	private String fileNameWithoutExtn;
	private String status;

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

	public String getInstituteKey() {
		return instituteKey;
	}

	public void setInstituteKey(String instituteKey) {
		this.instituteKey = instituteKey;
	}

	public String getFileNameWithoutExtn() {
		return fileNameWithoutExtn;
	}

	public void setFileNameWithoutExtn(String fileNameWithoutExtn) {
		this.fileNameWithoutExtn = fileNameWithoutExtn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

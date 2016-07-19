package in.ac.iitkgp.acaddwh.config;

public class ProjectInfo {
	private static String websiteName = "Academic Data Warehouse";
	private static String websiteTagLine = "Portal for managing academic data";

	private static String uploadDirPathWindows = "G:/AcadDWH/AcadDWH_Data/";
	private static String uploadDirPathLinux = "/home/sourav/AcadDWH/AcadDWH_Data/";

	public static String getWebsiteName() {
		return websiteName;
	}

	public static String getWebsiteTagLine() {
		return websiteTagLine;
	}

	public static String getUploadDirPath() {
		if (System.getProperty("os.name").contains("Windows")) {
			return uploadDirPathWindows;
		} else {
			return uploadDirPathLinux;
		}
	}

	

}

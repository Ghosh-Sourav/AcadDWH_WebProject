package in.ac.iitkgp.acaddwh.config;

public class HiveInfo {
	private static String driverClass = "org.apache.hive.jdbc.HiveDriver";
	//"org.apache.hadoop.hive.jdbc.HiveDriver";
	private static String protocol = "jdbc:hive2:";
	private static String hostIP = "10.5.30.101";
	private static long hostPort = 10000;
	private static String databaseName = "test_15cs60r16";
	private static String username = "hive";
	private static String password = "";

	private static String url = protocol + "//" + hostIP + ":" + hostPort + "/" + databaseName;
	
	public static String getDriverClass() {
		return driverClass;
	}

	public static String getProtocol() {
		return protocol;
	}

	protected static String getHostIP() {
		return hostIP;
	}

	protected static long getHostPort() {
		return hostPort;
	}

	protected static String getDatabaseName() {
		return databaseName;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

	public static String getUrl() {
		return url;
	}

}

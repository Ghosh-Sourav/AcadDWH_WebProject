package in.ac.iitkgp.acaddwh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.config.HiveInfo;

public class HiveConnection {

	public static Connection getSaveConnection() throws ClassNotFoundException, SQLException {
		Connection con = null;
		try {
			Class.forName(HiveInfo.getDriverClass());
			DriverManager.setLoginTimeout(10);
			con = DriverManager.getConnection(HiveInfo.getUrl(), HiveInfo.getUsername(), HiveInfo.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw(e);
		}
		return con;
	}
	
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
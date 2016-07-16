package in.ac.iitkgp.acaddwh.dao;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.Test;

public class TestDAO {
	public synchronized int saveDim(Connection con, Test test) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
//			ps = con.prepareStatement("insert into table test_table select ?, ? from dummy");
//			ps.setInt(1, test.getRoll());
//			ps.setString(2, test.getName());
			
			ps = con.prepareStatement("LOAD DATA LOCAL INPATH ? INTO TABLE acaddwh.test_table");
			String filePath = "G:/acaddwh/test_table_data.csv";
			ps.setString(1, "file:///"+filePath);
			System.out.println("file:///"+filePath);

			System.out.println("Name = " + test.getName() + ", Roll = " + test.getRoll());
			
			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

	public void printItems(Connection con) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("select * from acaddwh.test_table where roll<20");

			rs = ps.executeQuery();
			while (rs.next()) {

				System.out.println("Column 1 = " + rs.getString(1));
				System.out.println("Column 2 = " + rs.getString(2));

			}

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		}

	}
}

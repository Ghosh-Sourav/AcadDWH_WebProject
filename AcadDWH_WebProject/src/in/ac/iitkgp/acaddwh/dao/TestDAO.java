package in.ac.iitkgp.acaddwh.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import in.ac.iitkgp.acaddwh.bean.Test;

public class TestDAO {
	public synchronized int saveDim(Connection con, Test test) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			String localfsFilePath = "G:/AcadDWH/test_table_data.csv";
			String hdfsFilePath = "/user/15CS60R16/test_table_data.csv";

			Configuration config = new Configuration();
			FileSystem hdfs = FileSystem.get(config);
			Path srcPath = new Path(localfsFilePath);
			Path dstPath = new Path(hdfsFilePath);
			hdfs.copyFromLocalFile(srcPath, dstPath);

			// ps = con.prepareStatement("insert into table test_table select ?,
			// ? from dummy");
			// ps.setInt(1, test.getRoll());
			// ps.setString(2, test.getName());

			ps = con.prepareStatement("LOAD DATA INPATH ? INTO TABLE acaddwh.test_table");

			ps.setString(1, hdfsFilePath);

			System.out.println("Name = " + test.getName() + ", Roll = " + test.getRoll());

			returnValue = ps.executeUpdate();

		} catch (IOException e) {
			e.printStackTrace();
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

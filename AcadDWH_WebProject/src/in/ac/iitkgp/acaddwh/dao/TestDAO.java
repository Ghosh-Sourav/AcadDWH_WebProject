package in.ac.iitkgp.acaddwh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.Test;


public class TestDAO {
	public int saveDim(Connection con, Test test) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into table test_table select ?, ? from dummy");
			ps.setInt(1, test.getRoll());
			ps.setString(2, test.getName());
			
			System.out.println("Name = "+test.getName()+", Roll = "+test.getRoll());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

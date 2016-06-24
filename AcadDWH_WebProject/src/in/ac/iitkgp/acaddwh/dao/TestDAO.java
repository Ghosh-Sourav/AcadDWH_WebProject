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
			ps = con.prepareStatement("insert into table test_15cs60r16.test_students select ?, ? from dummy");
			ps.setString(1, test.getName());
			ps.setInt(2, test.getRoll());
			
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

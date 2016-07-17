package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.Regtype;

public class RegtypeDAO {
	public int addToDB(Connection con, Regtype regtype) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into acaddwh.dim_regtypes values (?,?,?)");
			ps.setString(1, regtype.getRegtypeKey());
			ps.setString(2, regtype.getRegtypeCode());
			ps.setString(3, regtype.getRegtypeDesc());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}
	
	public int addToHive(Connection con, Regtype regtype) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into table acaddwh.dim_regtypes select ?,?,? from acaddwh.dummy limit 1");
			ps.setString(1, regtype.getRegtypeKey());
			ps.setString(2, regtype.getRegtypeCode());
			ps.setString(3, regtype.getRegtypeDesc());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

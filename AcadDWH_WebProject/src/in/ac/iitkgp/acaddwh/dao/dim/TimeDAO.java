package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.Time;

public class TimeDAO {
	public int addDim(Connection con, Time time) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into acaddwh.dim_times values (?,?,?,?)");
			ps.setString(1, time.getTimeKey());
			ps.setString(2, time.getTimeCode());
			ps.setString(3, time.getAcadsemester());
			ps.setString(4, time.getAcadsession());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

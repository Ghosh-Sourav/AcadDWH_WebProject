package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.Institute;

public class InstituteDAO {
	public int addDim(Connection con, Institute institute) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into acaddwh.dim_institute values (?,?,?)");
			ps.setString(1, institute.getInstituteKey());
			ps.setString(2, institute.getInstituteName());
			ps.setString(3, institute.getInstitutePassword());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

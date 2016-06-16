package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.Teacher;

public class TeacherDAO {
	public int addDim(Connection con, Teacher teacher) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;
		
		try {
			ps = con.prepareStatement("insert into acaddwh.dim_teachers values (?,?,?,?)");
			ps.setString(1, teacher.getTeacherKey());
			ps.setString(2, teacher.getTeacherCode());
			ps.setString(3, teacher.getTeacherDept());
			ps.setString(4, teacher.getTeacherDesg());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}

		return returnValue;
	}

}

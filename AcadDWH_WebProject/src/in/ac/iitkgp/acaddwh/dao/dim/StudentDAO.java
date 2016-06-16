package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.Student;

public class StudentDAO {
	public int addDim(Connection con, Student student) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into acaddwh.dim_students values (?,?,?,?,?,?)");
			ps.setString(1, student.getStudentKey());
			ps.setString(2, student.getStudentCode());
			ps.setString(3, student.getSplKey());
			ps.setString(4, student.getStudentNoa());
			ps.setString(5, student.getStudentNoa());
			ps.setInt(6, student.getAdmissionYear());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

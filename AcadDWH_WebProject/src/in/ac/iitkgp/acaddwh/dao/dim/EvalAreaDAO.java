package in.ac.iitkgp.acaddwh.dao.dim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import in.ac.iitkgp.acaddwh.bean.dim.EvalArea;

public class EvalAreaDAO {
	public int addDim(Connection con, EvalArea evalArea) throws SQLException {
		int returnValue = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into acaddwh.dim_eval_areas values (?,?,?,?)");
			ps.setString(1, evalArea.getEvalAreaKey());
			ps.setString(2, evalArea.getEvalAreaCode());
			ps.setString(3, evalArea.getEvalArea());

			returnValue = ps.executeUpdate();

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
		return returnValue;
	}

}

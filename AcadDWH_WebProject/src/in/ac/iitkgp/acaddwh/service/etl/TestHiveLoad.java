package in.ac.iitkgp.acaddwh.service.etl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.ac.iitkgp.acaddwh.bean.Test;
import in.ac.iitkgp.acaddwh.config.HiveInfo;
import in.ac.iitkgp.acaddwh.dao.TestDAO;
import in.ac.iitkgp.acaddwh.exception.LoadException;
import in.ac.iitkgp.acaddwh.util.HiveConnection;
import in.ac.iitkgp.acaddwh.util.LogFile;

public class TestHiveLoad {

	@SuppressWarnings("unchecked")
	public int load(List<?> tests, String absoluteLogFileName) throws LoadException {
		int count = 0, processedLineCount = 0;
		StringBuffer logString = new StringBuffer();

		Connection con = null;
		
		TestDAO testDAO = new TestDAO();

		try {
			
			con = HiveConnection.getSaveConnection();
			System.out.println("Connection obtained!");
			
			for (Test test : (List<Test>) tests) {
				try {
					++processedLineCount;
					count += testDAO.saveDim(con, test);
					System.out.println("[C] Consolidated Test " + test);
				} catch (SQLException e) {
					logString.append(
							"Load," + processedLineCount + "," + test.getRoll() + "," + LogFile.getErrorMsg(e) + "\n");
					con.rollback();
				}
			}
			if (logString.length() != 0) {
				throw (new LoadException());
			}
			System.out.println("Committing updates...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(logString);
			// TODO: Throw save exception here
		} finally {
			HiveConnection.closeConnection(con);
		}

		return count;
	}

	public static void main(String[] args) {
		List<Test> tests = new ArrayList<Test>();

		Test test = new Test();
		test.setName("Inserted User");
		test.setRoll(120);

		tests.add(test);

		TestHiveLoad testHiveLoad = new TestHiveLoad();
		try {
			System.out.println("Starting...");
			System.out.println(HiveInfo.getUrl());
			testHiveLoad.load(tests, "");
			System.out.println("Finished with success!");
		} catch (LoadException e) {
			e.printStackTrace();
		}
		System.out.println();

	}

}

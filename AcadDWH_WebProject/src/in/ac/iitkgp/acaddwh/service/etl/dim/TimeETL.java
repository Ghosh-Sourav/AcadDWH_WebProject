package in.ac.iitkgp.acaddwh.service.etl.dim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.ac.iitkgp.acaddwh.bean.dim.Time;
import in.ac.iitkgp.acaddwh.dao.dim.TimeDAO;
import in.ac.iitkgp.acaddwh.exception.ExtractException;
import in.ac.iitkgp.acaddwh.exception.LoadException;
import in.ac.iitkgp.acaddwh.exception.TransformException;
import in.ac.iitkgp.acaddwh.exception.WarehouseException;
import in.ac.iitkgp.acaddwh.service.ETLService;
import in.ac.iitkgp.acaddwh.util.DBConnection;
import in.ac.iitkgp.acaddwh.util.HiveConnection;
import in.ac.iitkgp.acaddwh.util.LogFile;

public class TimeETL implements ETLService<Time> {

	public List<Time> extract(String filePath, String splitter, String absoluteLogFileName) throws ExtractException {
		List<Time> times = new ArrayList<Time>();
		StringBuffer logString = new StringBuffer();
		BufferedReader br = null;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				Time time = new Time();
				String[] values = line.split(splitter);

				time.setTimeCode(values[0]);
				time.setAcadsemester(values[1]);
				time.setAcadsession(values[2]);

				System.out.println("Extracted Time " + time);

				times.add(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logString.append(
					"Extract," + times.size() + ",Not Extracted,Data format is invalid - Further lines ignored\n");
			LogFile.writeToLogFile(absoluteLogFileName, logString);
			throw (new ExtractException());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return times;
	}

	@SuppressWarnings("unchecked")
	public int transform(List<?> times, String instituteCode, String absoluteLogFileName) throws TransformException {
		int count = 0;
		try {
			for (Time time : (List<Time>) times) {
				time.setTimeKey(instituteCode + '_' + time.getTimeCode());
				System.out.println("Transformed Time " + time);
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw (new TransformException());
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public int load(List<?> times, String absoluteLogFileName) throws LoadException {
		int count = 0, processedLineCount = 0;
		StringBuffer logString = new StringBuffer();

		Connection con = DBConnection.getWriteConnection();
		TimeDAO timeDAO = new TimeDAO();

		try {
			for (Time time : (List<Time>) times) {
				try {
					++processedLineCount;
					count += timeDAO.addToDB(con, time);
					System.out.println("[UC] Loaded Time " + time);
				} catch (SQLException e) {
					logString.append("Load," + processedLineCount + "," + time.getTimeCode() + ","
							+ LogFile.getErrorMsg(e) + "\n");
					con.rollback();
				}
			}
			if (logString.length() != 0) {
				throw (new LoadException());
			}
			System.out.println("Committing updates...");
			con.commit();
		} catch (Exception e) {
			try {
				System.out.println("Rolling back changes...");
				con.rollback();
				LogFile.writeToLogFile(absoluteLogFileName, logString);
				count = 0;
				throw (new LoadException());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBConnection.closeConnection(con);
		}

		return count;
	}

	public void warehouse(String hadoopLocalFileName, String absoluteLogFileName) throws WarehouseException {
		StringBuffer logString = new StringBuffer();

		Connection con = HiveConnection.getSaveConnection();
		TimeDAO timeDAO = new TimeDAO();

		try {
			timeDAO.addToHive(con, hadoopLocalFileName);
			System.out.println("[W] Warehoused Time file: " + hadoopLocalFileName);

		} catch (SQLException e) {
			System.out.println("WarehouseException thrown!");
			logString.append("Warehouse," + "-" + "," + "-" + "," + LogFile.getErrorMsg(e) + "\n");
			LogFile.writeToLogFile(absoluteLogFileName, logString);
			throw (new WarehouseException());
		} finally {
			HiveConnection.closeConnection(con);
		}
	}
}

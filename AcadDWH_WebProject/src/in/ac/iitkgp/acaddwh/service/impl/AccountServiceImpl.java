package in.ac.iitkgp.acaddwh.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import in.ac.iitkgp.acaddwh.bean.dim.Institute;
import in.ac.iitkgp.acaddwh.dao.dim.AccountDAO;
import in.ac.iitkgp.acaddwh.exception.ETLException;
import in.ac.iitkgp.acaddwh.service.AccountService;
import in.ac.iitkgp.acaddwh.service.ETLService;
import in.ac.iitkgp.acaddwh.service.etl.dim.InstituteETL;
import in.ac.iitkgp.acaddwh.util.Cryptography;
import in.ac.iitkgp.acaddwh.util.DBConnection;

public class AccountServiceImpl implements AccountService {

	@Override
	public boolean instituteExists(String key, String password) {
		boolean exists = false;
		password = Cryptography.encrypt(key + password);
		Institute institute = null;

		Connection con = DBConnection.getReadConnection();
		AccountDAO accountDAO = new AccountDAO();

		try {
			institute = accountDAO.getInstitute(con, key, password);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBConnection.closeConnection(con);
		}

		if (institute != null) {
			exists = true;
		}
		return exists;
	}

	@Override
	public String getInstituteName(String key) {
		String instituteName = "";

		Connection con = DBConnection.getReadConnection();
		AccountDAO accountDAO = new AccountDAO();

		try {
			instituteName = accountDAO.getInstitute(con, key).getInstituteName();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			DBConnection.closeConnection(con);
		}

		return instituteName;
	}

	@Override
	public String signUpInstitute(Institute institute) throws ETLException {

		List<Institute> institutes = new ArrayList<Institute>();
		institutes.add(institute);

		ETLService<Institute> etlService = new InstituteETL();
		try {
			etlService.transform(institutes, null, null);
			etlService.load(institutes, null);
			etlService.warehouse(institutes, null);
		} catch (ETLException e) {
			throw (e);
		}

		return institute.getInstituteKey();
	}

}

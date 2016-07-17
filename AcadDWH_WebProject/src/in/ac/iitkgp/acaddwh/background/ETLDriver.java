package in.ac.iitkgp.acaddwh.background;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.Part;

import in.ac.iitkgp.acaddwh.bean.dim.Request;
//import in.ac.iitkgp.acaddwh.dso.ItemDSO;
import in.ac.iitkgp.acaddwh.exception.ETLException;
import in.ac.iitkgp.acaddwh.exception.ExtractException;
import in.ac.iitkgp.acaddwh.exception.WarehouseException;
import in.ac.iitkgp.acaddwh.exception.LoadException;
import in.ac.iitkgp.acaddwh.exception.TransformException;
import in.ac.iitkgp.acaddwh.service.ETLService;
import in.ac.iitkgp.acaddwh.service.RequestService;
import in.ac.iitkgp.acaddwh.service.etl.dim.*;
import in.ac.iitkgp.acaddwh.service.etl.fact.*;
import in.ac.iitkgp.acaddwh.service.impl.RequestServiceImpl;

public class ETLDriver implements Runnable {

	private Request request = null;
	private String df = null;
	private String absoluteFileNameWithoutExtn = null;
	private Part part = null;

	RequestService requestService = null;

	public ETLDriver(Request request, String df, String absoluteFileNameWithoutExtn, Part part) {
		this.request = request;
		this.df = df;
		this.absoluteFileNameWithoutExtn = absoluteFileNameWithoutExtn;
		this.part = part;

		requestService = new RequestServiceImpl();
	}

	@Override
	public void run() {
		try {
			// Thread.sleep(20000);

			uploadCsvFile();
			if (new File(absoluteFileNameWithoutExtn + ".csv").exists()) {
				System.out.println("File saved as " + absoluteFileNameWithoutExtn + ".csv");
				request.setStatus("File upload completed, Extracting...");
				requestService.updateLog(request);
			}

			System.out.println("Inititating ETL [df = " + df + ", instituteKey = " + request.getInstituteKey() + "]");
			processETL();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ETLDriver thread aborted!");
		} finally {
			// deleteFile();
		}
	}

	private void uploadCsvFile() throws IOException {
		part.write(absoluteFileNameWithoutExtn + ".csv");
	}

	@SuppressWarnings("unused")
	private boolean deleteFile(String fileName) {
		return new File(fileName).delete();
	}

	private void processETL()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, ETLException {
		Class<?> etlClass = null;

		switch (df) {
		case "dim_departments":
			etlClass = DepartmentETL.class;
			break;
		case "dim_specialisations":
			etlClass = SpecialisationETL.class;
			break;
		case "dim_students":
			etlClass = StudentETL.class;
			break;
		case "dim_teachers":
			etlClass = TeacherETL.class;
			break;
		case "dim_courses":
			etlClass = CourseETL.class;
			break;
		case "dim_eval_areas":
			etlClass = EvalAreaETL.class;
			break;
		case "dim_regtypes":
			etlClass = RegtypeETL.class;
			break;
		case "dim_times":
			etlClass = TimeETL.class;
			break;

		case "fact_sem_performance":
			etlClass = SemPerformanceETL.class;
			break;
		case "fact_spl_performance":
			etlClass = SplPerformanceETL.class;
			break;
		case "fact_stu_learning":
			etlClass = StuLearningETL.class;
			break;
		case "fact_teaching_quality":
			etlClass = TeachingQualityETL.class;
			break;

		default:
			throw (new ExtractException());
		}
		drive(etlClass);
	}

	private void drive(Class<?> etlClass)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		int resultCount = 0;

		ETLService<?> etlService = (ETLService<?>) etlClass.newInstance();

		try {
			List<?> items = etlService.extract(absoluteFileNameWithoutExtn + ".csv", ",",
					absoluteFileNameWithoutExtn + "-report.txt");

			System.out.println("Extracted " + items.size() + " items");
			request.setStatus("Extraction completed, Transforming...");
			requestService.updateLog(request);

			resultCount = etlService.transform(items, request.getInstituteKey(),
					absoluteFileNameWithoutExtn + "-report.txt");
			System.out.println("Transformed " + resultCount + " items");
			request.setStatus("Transformation completed, Loading...");
			requestService.updateLog(request);

			resultCount = etlService.load(items, absoluteFileNameWithoutExtn + "-report.txt");
			System.out.println("Loaded " + resultCount + " items");
			request.setStatus("Loading completed, Warehousing...");
			requestService.updateLog(request);

			/*
			 * TO SAVE WAREHOUSED OUTPUT TO "-hive.csv" FILE, UNCOMMENT THE
			 * FOLLOWING LINE AND CORRESPONDING IMPORT STATEMENT
			 */
			// ItemDSO.writeTransformedCSV(items, absoluteFileNameWithoutExtn +
			// "-hive.csv");

			resultCount = etlService.warehouse(items, absoluteFileNameWithoutExtn + "-report.txt");
			System.out.println("Warehoused " + resultCount + " items");
			request.setStatus("ETL Process completed successfully");
			requestService.updateLog(request);

		} catch (ExtractException e) {
			System.out.println("Extraction failed!");
			request.setStatus("Extraction failed, ETL Aborted");
			requestService.updateLog(request);

		} catch (TransformException e) {
			System.out.println("Transformation failed!");
			request.setStatus("Transformation failed, ETL Aborted");
			requestService.updateLog(request);

		} catch (LoadException e) {
			System.out.println("Loading failed!");
			request.setStatus("Loading failed, ETL Aborted");
			requestService.updateLog(request);

		} catch (WarehouseException e) {
			System.out.println("Warehousing failed!");
			request.setStatus("Warehousing failed, ETL completed upto Loading phase");
			requestService.updateLog(request);

		}
	}

}

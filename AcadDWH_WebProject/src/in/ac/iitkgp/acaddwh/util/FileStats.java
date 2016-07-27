package in.ac.iitkgp.acaddwh.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import in.ac.iitkgp.acaddwh.config.ProjectInfo;

public class FileStats {
	public static long getLineCount(String fileName) throws IOException {
		String absoluteFileName = ProjectInfo.getUploadDirPath() + fileName;

		InputStream is = new BufferedInputStream(new FileInputStream(absoluteFileName));
		try {
			byte[] c = new byte[1024];
			long count = 0;
			int readChars = 0;
			boolean endsWithoutNewLine = false;
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
				endsWithoutNewLine = (c[readChars - 1] != '\n');
			}
			if (endsWithoutNewLine) {
				++count;
			}
			return count;
		} catch (Exception e) {
			return -1;
		} finally {
			is.close();
		}
	}

	public static long getSizeInBytes(String fileName) {
		String absoluteFileName = ProjectInfo.getUploadDirPath() + fileName;
		return (new File(absoluteFileName).length());
	}

}

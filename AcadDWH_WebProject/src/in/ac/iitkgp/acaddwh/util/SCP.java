package in.ac.iitkgp.acaddwh.util;

import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SCP {

	public static void main() {
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession("15CS60R16", "10.5.30.101", 22);
			session.setPassword("**********");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();

			File localFile = new File("localfilepath");
			// If you want you can change the directory using the following
			// line.
			
			channel.cd("AcadDWH");
			channel.put(new FileInputStream(localFile), localFile.getName());
			
			channel.disconnect();
			session.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

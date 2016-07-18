package in.ac.iitkgp.acaddwh.util;

import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SCP {

	public static void main(String[] args) {
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession("15CS60R16", "10.5.30.101", 22);
			session.setPassword("");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();

			File localFile = new File("/home/sourav/AcadDWH/AcadDWH_Data/20160718035530444_IITKGP_dim_departments-hive.csv");
			// If you want you can change the directory using the following
			// line.
			
			channel.cd("/home/mtech/15CS60R16/AcadDWH_Data");
			channel.put(new FileInputStream(localFile), localFile.getName());
			
			channel.disconnect();
			session.disconnect();
			System.out.println("Sent");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

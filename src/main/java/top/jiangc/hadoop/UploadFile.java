package top.jiangc.hadoop;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.jiangc.util.UUIDUtil;

@Component
public class UploadFile {
	
	private static Logger logger = LoggerFactory.getLogger(UploadFile.class);
	
	@Autowired
	private HadoopParams hp;
	
	@Autowired
	private ReportLogs reportLogs;
	
	/**
	 * 上传文件到HDFS
	 *
	 * jiangc
	 * @param fis 文件输入流
	 * @return  返回保存文件路径
	 */
	public String uploadFileToHdfs(InputStream in, String fileName){
		String uuid = UUIDUtil.getUUID();
		String fileNames[] = fileName.split("\\.");
		String fName = fileNames[0]+"_"+uuid+"."+fileNames[1];//保存到HDFS中的文件名
		String dst = hp.getHdfs()+"input/"+fName;//保存文件路径
		
		Configuration conf = new Configuration();

		try {
			FileSystem fs = FileSystem.get(URI.create(dst), conf);
			OutputStream out = fs.create(new Path(dst), new Progressable() {
				public void progress() {
//					System.out.print(".");
				}
			});
			IOUtils.copyBytes(in, out, 4096, true);
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return dst;
	}

	/**
	 * 上传文件并处理
	 *
	 * jiangc
	 * @param in
	 * @param fileName
	 * @return
	 */
	public List<String> UploadAndReport(InputStream in, String fileName){
		List<String> lst = new ArrayList<>();
		long date1 = System.currentTimeMillis();
		String dst = uploadFileToHdfs(in, fileName);
		long date2 = System.currentTimeMillis();
		logger.info("文件上传到HDFS用时："+(float)(date2-date1)/1000+"s");
		String dstOut = reportLogs.reportLogs(dst);
		long date3 = System.currentTimeMillis();
		logger.info("文件MapReduce处理用时："+(float)(date3-date2)/1000+"s");
		lst = DownFile.downFileFromHdfs(dstOut);
		long date4 = System.currentTimeMillis();
		logger.info("文件下载用时："+(float)(date4-date3)/1000+"s");
		return lst;
	}
	
	public static void main(String[] args) {

		try {
			String localSrc = "E://show.txt";
			String dst = "hdfs://centos:9000/user/root/input/show.txt";
			InputStream in = new BufferedInputStream(new FileInputStream(
					localSrc));
			Configuration conf = new Configuration();

			FileSystem fs = FileSystem.get(URI.create(dst), conf);
			OutputStream out = fs.create(new Path(dst), new Progressable() {
				public void progress() {
					System.out.print(".");
				}
			});
			IOUtils.copyBytes(in, out, 4096, true);
			System.out.println("success");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
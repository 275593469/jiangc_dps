package top.jiangc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * 文件上传
 * @author jiangc
 *
 */
public class FileUpload {
	
	private static Logger logger = LoggerFactory.getLogger(FileUpload.class);

//	@Value("${fileLog.path}")
	private static String sPath = "../userLogs/";
	
	@Value("${user.name}")
	private static String sIntervalTime;
	
	public static Boolean fileUpload(String path, File file){
		return null;
	}
	
	/**
	 * 生成用户日志文件
	 *
	 * jiangc
	 * @param path  生成文件目录
	 * @param logs
	 * @return
	 */
	public static Boolean createFileLog(String logs){
		File dir = new File(sPath);
		//判断目录是否存在
		if(!dir.exists()){
			dir.mkdir();
		}
		//每天生成一个文件
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(date);
		File logFile = new File(sPath+sDate+".text");
		
		SimpleDateFormat nowSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
		String nowTime = nowSdf.format(date);
		logs = nowTime+"  "+logs;
		
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(logFile, true));
			ps.println(logs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		} finally {
			ps.close();
		}
		
		return true;
	}
	
	public static Boolean creatFileLog(String path, Map logs){
		return null;
	}
}

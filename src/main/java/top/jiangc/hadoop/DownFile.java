package top.jiangc.hadoop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;


public class DownFile {
	
	private static Logger logger = LoggerFactory.getLogger(DownFile.class);
	
	public static List<String> downFileFromHdfs(String dstOut){
		dstOut = dstOut+"/part-r-00000";
		Configuration conf = new Configuration();
		FileSystem fs = null;
		
		List<String> lst = new ArrayList<>();
		
		try {
			fs = FileSystem.get(URI.create(dstOut), conf);
			InputStream is = fs.open(new Path(URI.create(dstOut)));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while((str=br.readLine()) != null){
				if(!StringUtils.isEmpty(str))
					lst.add(str);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return lst;
	}

	public static void main(String[] args) throws Exception {
		String dst = "hdfs://centos:9000/user/root/ouput/part-r-00000";
		
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		
		try {
//			FileInputStream fis = new FileInputStream(file);
			InputStream is = fs.open(new Path(URI.create(dst)));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while(br.readLine() != null){
				System.out.println(br.readLine());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

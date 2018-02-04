package top.jiangc.hadoop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(locations="classpath:config/hadoop.properties",prefix="hadoop")
@Component
public class HadoopParams {

	private String hdfs;

	public String getHdfs() {
		return hdfs;
	}

	public void setHdfs(String hdfs) {
		this.hdfs = hdfs;
	}
	
	
}

package top.jiangc.hadoop;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.jiangc.util.JsonUtil;
import top.jiangc.util.UUIDUtil;

import com.alibaba.druid.util.StringUtils;

@Component
public class ReportLogs {
	
	private static Logger logger = LoggerFactory.getLogger(ReportLogs.class);
	
	@Autowired
	private HadoopParams hp;
    /**

     * 四个泛型类型分别代表：

     * KeyIn        Mapper的输入数据的Key，这里是每行文字的起始位置（0,11,...）

     * ValueIn      Mapper的输入数据的Value，这里是每行文字

     * KeyOut       Mapper的输出数据的Key，这里是每行文字中的“年份”

     * ValueOut     Mapper的输出数据的Value，这里是每行文字中的“气温”

     */
    static class TempMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            // 打印样本: Before Mapper: 0, 2000010115
            String line = value.toString();
            if(StringUtils.isEmpty(line)){
            	return;
            }
            String[] logs = line.split("  ");
            Map<String, Object> mapLog = JsonUtil.parseMap(logs[1]);//将日志转换成Map
            String skey = null;
            int svalue = 1;
            if(null != mapLog.get("browseId") ){//表示浏览日志
				String title = ((String) mapLog.get("title")).trim();//浏览页面标题
				skey = "browse:"+title;
			}else{
				String buttonText = ((String) mapLog.get("buttonText")).trim();
				skey = "handle:"+buttonText;
			}
            context.write(new Text(skey), new IntWritable(svalue));
        }
    }
 
    /**
     * 四个泛型类型分别代表：
     * KeyIn        Reducer的输入数据的Key，这里是每行文字中的“年份”
     * ValueIn      Reducer的输入数据的Value，这里是每行文字中的“气温”
     * KeyOut       Reducer的输出数据的Key，这里是不重复的“年份”
     * ValueOut     Reducer的输出数据的Value，这里是这一年中的“最高气温”
     */
    static class TempReducer extends
            Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                Context context) throws IOException, InterruptedException {
            int maxValue = 0;
            //取values的最大值
            for (IntWritable value : values) {
                maxValue = maxValue+value.get();
            }
            context.write(key, new IntWritable(maxValue));
        }
    }
 
    /**
     * MapReduce处理文件
     *
     * jiangc
     * @param dst
     * @return 返回结果目录路径
     */
    public String reportLogs(String dst) {
    	String uuid = UUIDUtil.getUUID();
        //输出路径，必须是不存在的，空文件加也不行。
        String dstOut = hp.getHdfs()+"output"+uuid;
        try {
	        Configuration hadoopConfig = new Configuration();
	        hadoopConfig.setBoolean("mapreduce.app-submission.cross-platform", true);// 配置使用跨平台提交任务
	        hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	        hadoopConfig.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
	        Job job = new Job(hadoopConfig);
	        //如果需要打成jar运行，需要下面这句
	//        job.setJarByClass(NewMaxTemperature.class);
	 
	        //job执行作业时输入和输出文件的路径
	        FileInputFormat.addInputPath(job, new Path(dst));
	        FileOutputFormat.setOutputPath(job, new Path(dstOut));
	 
	        //指定自定义的Mapper和Reducer作为两个阶段的任务处理类
	        job.setMapperClass(TempMapper.class);
	        job.setReducerClass(TempReducer.class);
	         
	        //设置最后输出结果的Key和Value的类型
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
	         
	        //执行job，直到完成
	        if(!job.waitForCompletion(true)){//处理成功
	        	dstOut="";
	        }
        } catch(Exception e){
        	logger.error(e.getMessage());
        }
        
        return dstOut;
    }
}

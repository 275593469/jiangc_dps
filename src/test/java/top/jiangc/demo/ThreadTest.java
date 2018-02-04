package top.jiangc.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

import com.alibaba.druid.util.StringUtils;

public class ThreadTest {

	private static volatile int race = 20;
	private int num = 0;
	
	private void addNum(){
		num++;
	}
	
	public void addTest(){
		Thread[] threads = new Thread[this.race];
		
		for(int i=0; i<race; i++){
			threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					addNum();
					
				}
			});
			threads[i].start();
		}
		
		while(Thread.activeCount() > 1){
			Thread.yield();
			System.out.println(num);
		}
	}
	
	@Test
	public void test(){
		String s = "2017-05-20 15:39:48:869";
		Date date = new Date(s);
		System.out.println(date);
	}
	
	@Test
	public void createLog() throws Exception {
		File yuanFile = new File("E:\\java\\MyEclipse2014\\userLogs\\2017-05-20.text");
		File file = new File("E:\\logs3.text");
		
		InputStream in = new FileInputStream(yuanFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str = null;
		List<String> lst = new ArrayList<>();
		while((str=br.readLine())!=null){
			if(!StringUtils.isEmpty(str)){
				lst.add(str);
			}
		}
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(file,true));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int len = 10000*2*100*2;
		for(int i=0;i<len;i++){
			int length = lst.size();
			int n = (int) (Math.random()*length);
			ps.println(lst.get(n));
		}
		

		
		ps.close();
	}
	
}

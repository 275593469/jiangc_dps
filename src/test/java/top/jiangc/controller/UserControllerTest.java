package top.jiangc.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import top.jiangc.Application;
import top.jiangc.hadoop.HadoopParams;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserControllerTest {

	@Autowired
	private HadoopParams hp;
	
	@Test
	public void registerTest() throws ClientProtocolException, IOException{
		List<NameValuePair> list = Form.form()
		.add("userName", "jiangc_java@163.com")//
		.add("nickName", "jiangc")//
		.add("password", "123456").build();
		
		String str = Request.Post("http://127.0.0.1:8081/HelloBoot/user/register").bodyForm(list).execute().returnContent().asString();
		System.out.println(str);
	}
	
	@Test
	public void test(){
		System.out.println(hp.getHdfs());
	}
	
	
}

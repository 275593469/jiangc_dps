package top.jiangc.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import top.jiangc.Application;
import top.jiangc.entity.User;
import top.jiangc.repository.IUserRepository;
import top.jiangc.util.EncryptUtil;
import top.jiangc.util.UUIDUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)//指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class UserTest {

	@Autowired
	private IUserRepository userRepository;
	
	@Resource(name = "userDao")
	private IUserDao userDao;
	
	@Autowired
	private User user;
	
	@Test
	public void insertTest(){
		user.setUserId("123");
		user.setUserName("aaa");
		user.setNickName("haha");
		user.setEmail("1233");
		user.setPassword("111");
		user.setStatus(0);
		userRepository.save(user);
	}
	
	@Test
	public void findTest(){
		userRepository.getUserById("123");
	}
	
	@Test
	public void insertBatch(){
		for(int i=0; i<1; i++){
			user = new User(UUIDUtil.getUUID(), "test"+i, "测试"+i, EncryptUtil.MD5("123456") , "123@qq.com", "北京", 0);
			System.out.println(userRepository.save(user).toString());
		}
	}
	
	@Test
	public void MD5Test(){
		System.out.println(EncryptUtil.MD5("123456"));
	}
	
	@Test
	public void test(){
		System.out.println(userRepository.findUserByName("123"));
	}
	
}

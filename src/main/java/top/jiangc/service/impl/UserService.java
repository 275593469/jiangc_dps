package top.jiangc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.jiangc.dao.IRoleDao;
import top.jiangc.dao.IUserDao;
import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.User;
import top.jiangc.service.IUserService;
import top.jiangc.util.EncryptUtil;
import top.jiangc.util.MailUtil;

@Service("userService")
public class UserService implements IUserService {
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private User globalUser;
	
	@Resource(name = "userDao")
	private IUserDao userDao;

	@Resource(name = "roleDao")
	private IRoleDao roleDao;

	@Override
	public User findUserByName(String userName) {
		return userDao.findUserByName(userName);
	}

	@Override
	public Integer addUser(User user, String code) {
		String password = user.getPassword();
		user.setPassword(EncryptUtil.MD5(password));
		user.setAddress("");
		user.setStatus(0);
		user.setEmail(user.getUserName());
		return userDao.addUser(user, code);
	}
	
	@Override
	public void sendActiveMail(User user, String path, String email) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body><div style='margin-top:15px;margin-bottom:5px;'> 尊敬的"
				+ user.getNickName()
				+ "用户，您好：</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>恭喜您的账号："
				+ user.getUserName()
				+ " 注册成功！</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>"
				+ "请您点击下面的链接激活。</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'><a href='"
				+ path
				+ "'>"
				+ path
				+ "</a></div><br /><div style='color:red;margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>有效期一个小时</div></body></html>");
		try {
			MailUtil.sendHtmlMail("DPS账号激活", builder.toString(), email);
		} catch (EmailException e) {
			logger.error("邮件发送失败："+e.getMessage());
		}
		
	}
	
	@Override
	public void sendResetMail(User user, String path, String email) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body><div style='margin-top:15px;margin-bottom:5px;'> 尊敬的"
				+ user.getNickName()
				+ "用户，您好：</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>点击下面链接将您的账号："
				+ user.getUserName()
				+ "<br/>重置后的密码为：123456</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>"
				+ "请您点击下面的链接重置密码。</div><br/><div style='margin-top:15px;margin-bottom:5px;float:right;width:1000px;'><a href='"
				+ path
				+ "'>"
				+ path
				+ "</a></div><br /><div style='color:red;margin-top:15px;margin-bottom:5px;float:right;width:1000px;'>有效期一个小时</div></body></html>");
		try {
			MailUtil.sendHtmlMail("DPS账号激活", builder.toString(), email);
		} catch (EmailException e) {
			logger.error("邮件发送失败："+e.getMessage());
		}
		
	}

	@Override
	public boolean activeAccount(String userId, String activeCode) {
		User user = userDao.findUserById(userId);
		if(null != user && null != activeCode){

			if(activeCode.equals(user.getCode())){
				Integer result = userDao.updateStatus(userId, 1);
				return result == 1 ?  true: false;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean resetPassword(String userId, String activeCode) {
		User user = userDao.findUserById(userId);
		if(null != user && null != activeCode){

			if(activeCode.equals(user.getCode())){
				Integer result = userDao.updatePassword(userId, EncryptUtil.MD5("123456"));
				return result == 1 ?  true: false;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean updatePassWord(String userId, String newPassword) {
		Integer result = userDao.updatePassword(userId, EncryptUtil.MD5(newPassword));
		return result == 1 ?  true: false;
		
	}

	@Override
	public Page<Map<String, Object>> getUsers(PageRequest pageRequest) {
		Page<Map<String, Object>> page = new Page<>();
		List<Map<String, Object>> lstUser = new ArrayList<>();
		lstUser = userDao.getUsers(pageRequest);
		for(Map<String, Object> map : lstUser){
			map.put("select", "<input type=\"checkbox\" class=\"usercheck\" value=\""+map.get("user_id")+"\" />");
		}
		Integer pageCount = userDao.getUserCount(pageRequest.getsearch());
		page.setLstEntity(lstUser);
		page.setPageCount(pageCount);
		return page;
	}

	@Override
	public List<Map<String, Object>> getUserById(String userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public List<Map<String, Object>> getUserPermission(String userId) {
		List<Map<String, Object>> lstUser = new ArrayList<>();
		List<Map<String, Object>> lstResource = new ArrayList<>();
		Map<String, Object> mapResource = new HashMap<>();
		lstUser = userDao.getUserById(userId);
		lstResource = roleDao.getResources();
		
		//遍历资源，并将Resource以id为key，name为value存入map中
		for(Map<String, Object> map : lstResource){
			mapResource.put(map.get("resource_id").toString(), map.get("resource_addr"));
		}
			
		//遍历角色，将id换成name
		for(Map<String, Object> map : lstUser){
			String permission = (String) map.get("permission");
			if("all".equalsIgnoreCase(permission)){
				map.put("permission", mapResource);
			}else{
				
				String[] perms = permission.split(":");
				Map<String, Object> mapPermission = new HashMap<>();
				for(int i=0; i<perms.length; i++){
					mapPermission.put(perms[i], mapResource.get(perms[i]));
				}
				map.put("permission", mapPermission);
			}
			
		}
		return lstUser;
	}

	@Override
	public Integer updateUserRole(String[] userIds, String roleId) {
		return userDao.updateUserRole(userIds, roleId);
	}


}

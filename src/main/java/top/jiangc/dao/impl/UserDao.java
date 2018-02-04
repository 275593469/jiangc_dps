package top.jiangc.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;

import top.jiangc.dao.IUserDao;
import top.jiangc.dao.IdataTemplate;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.User;
import top.jiangc.repository.IUserRepository;

@Repository("userDao")
public class UserDao implements IUserDao {

	@Autowired
	private IUserRepository userRepository;
	
	@Resource(name = "dataTemplate")
	private IdataTemplate dataTemplate;
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
		
	}

	@Override
	public User findUserByName(String userName) {
		return userRepository.findUserByName(userName);
	}
	
	@Override
	public Integer addUser(User user, String code){
		String sql = "INSERT INTO user_account(user_id,user_name,nick_name,password,email,address,status,code,create_time,update_time)"
				+" VALUES(:userId,:userName,:nickName,:password,:email,:address,:status,:code,:createTime,:updateTime);";
		Map<String, Object> params = new HashMap<>();
		Date date = new Date();
		params.put("userId", user.getUserId());
		params.put("userName", user.getUserName());
		params.put("nickName", user.getNickName());
		params.put("password", user.getPassword());
		params.put("email", user.getEmail());
		params.put("address", user.getAddress());
		params.put("status", user.getStatus());
		params.put("code", code);
		params.put("createTime", date);
		params.put("updateTime", date);
		return dataTemplate.deleteOrUpDate(sql, params);
	}
	
	@Override
	public Integer addUser(){
		String sql = "INSERT INTO user_account(user_id,user_name,nick_name,password,email,address,status)"
				+" VALUES('123','123','13','13','13','34',0)";
		return dataTemplate.deleteOrUpDate(sql);
	}

	@Override
	public User findUserById(String userId) {
		return userRepository.findUserById(userId);
	}

	@Override
	public Integer updateStatus(String userId, Integer status) {
		String sql = "update user_account set `status`="+status+" where user_id = '"+userId+"'";
		return dataTemplate.deleteOrUpDate(sql);
	}

	@Override
	public Integer updatePassword(String userId, String password) {
		String sql = "update user_account set `password`="+password+" where user_id = '"+userId+"'";
		return dataTemplate.deleteOrUpDate(sql);
	}

	@Override
	public List<Map<String, Object>> getUsers(PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<>();
		String search = pageRequest.getsearch();
		String sql = "SELECT u.user_id,u.user_name,u.nick_name,u.email,u.address ,u.create_time,u.update_time,r.role_name "
				+ " FROM user_account u LEFT JOIN role r ON u.role_id=r.role_id WHERE u.status=1";
		if(!StringUtils.isEmpty(search)){
			sql += " and u.nick_name like :nickName";
			params.put("nickName", search+"%");
		}
		sql += " limit :start,:length";
		params.put("start", pageRequest.getstart());
		params.put("length", pageRequest.getlength());
			
		return dataTemplate.queryListEntity(sql, params);
	}

	@Override
	public Integer getUserCount(String search) {
		Map<String, Object> params = new HashMap<>();
		String sql = "SELECT count(1) "
				+ " FROM user_account u WHERE u.status=1";
		if(!StringUtils.isEmpty(search)){
			sql += " and u.nick_name like :nickName";
			params.put("nickName", search+"%");
		}
		return dataTemplate.getCountBy(sql, params);
	}

	@Override
	public List<Map<String, Object>> getUserById(String userId) {
		String sql ="SELECT u.user_id,u.user_name,u.nick_name,u.email,u.address ,"
				+ " date_format(u.create_time, '%Y-%m-%d %H:%i:%s') as create_time,date_format(u.update_time, '%Y-%m-%d %H:%i:%s') as update_time, u.role_id,r.permission"
				+ " FROM user_account u left join role r on u.role_id=r.role_id WHERE u.status=1 and u.user_id = ?";
		
		return dataTemplate.queryListEntity(sql, new Object[]{userId});
	}

	@Override
	public Integer updateUserRole(String[] userIds, String roleId) {
		StringBuffer sbUserId = new StringBuffer();
		sbUserId.append("(");
		for(int i=0; i<userIds.length; i++){
			sbUserId.append("'"+userIds[i]+"',");
		}
		String inUserIds = sbUserId.substring(0, sbUserId.lastIndexOf(","));
		inUserIds += ")";
		String sql = "update user_account set role_id='"+roleId+"' where user_id in "+inUserIds;
		return dataTemplate.deleteOrUpDate(sql);
	}

}

package top.jiangc.service;

import java.util.List;
import java.util.Map;

import top.jiangc.entity.Page;
import top.jiangc.entity.PageRequest;
import top.jiangc.entity.User;

public interface IUserService {

	public User findUserByName(String userName);

	public Integer addUser(User user, String code);

	/**
	 * 注册成功后发送邮件
	 * @author: jiangcheng
	 *
	 * @param user
	 * @param path
	 * @param email
	 */
	public void sendActiveMail(User user, String path, String email);

	/**
	 * 激活账号
	 *
	 * jiangc
	 * @param userId
	 * @param activeCode
	 * @return
	 */
	public boolean activeAccount(String userId, String activeCode);

	/**
	 * 发送重置密码的邮件
	 * @author: jiangcheng
	 *
	 * @param user
	 * @param path
	 * @param email
	 */
	public void sendResetMail(User user, String path, String email);

	/**
	 * 密码重置
	 *
	 * jiangc
	 * @param userId
	 * @param activeCode
	 * @return
	 */
	public boolean resetPassword(String userId, String activeCode);

	/**
	 * 修改密码
	 * @author: jiangcheng
	 *
	 * @param userId
	 * @param newPassword
	 */
	public boolean updatePassWord(String userId, String newPassword);

	/**
	 * 分页查询用户信息
	 * @author: jiangcheng
	 *
	 * @param pageRequest
	 * @return
	 */
	public Page<Map<String, Object>> getUsers(PageRequest pageRequest);

	/**
	 * 根据用户id获取用户信息
	 *
	 * jiangc
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserById(String userId);

	/**
	 * 获取用户信息和权限
	 * @author: jiangcheng
	 *
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserPermission(String userId);

	/**
	 * 修改用户角色
	 * @author: jiangcheng
	 *
	 * @param userIds
	 * @param roleId
	 * @return
	 */
	public Integer updateUserRole(String[] userIds, String roleId);

}

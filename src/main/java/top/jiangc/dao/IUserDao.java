package top.jiangc.dao;

import java.util.List;
import java.util.Map;

import top.jiangc.entity.PageRequest;
import top.jiangc.entity.User;

public interface IUserDao {

	public List<User> findAll();

	/**
	 * 通过用户名查询用户
	 * @author: jiangcheng
	 *
	 * @param userName
	 * @return
	 */
	public User findUserByName(String userName);

	/**
	 * 添加用户
	 * @author: jiangcheng
	 *
	 * @param user
	 * @return
	 */
	public Integer addUser(User user, String code);

	public Integer addUser();

	/**
	 * 通过userId查询用户
	 *
	 * jiangc
	 * @param userId
	 * @return
	 */
	public User findUserById(String userId);

	/**
	 * 修改用户状态
	 *
	 * jiangc
	 * @param userId
	 * @param i
	 * @return
	 */
	public Integer updateStatus(String userId, Integer status);

	/**
	 * 修改密码
	 *
	 * jiangc
	 * @param userId
	 * @param string
	 * @return
	 */
	public Integer updatePassword(String userId, String password);

	/**
	 * 分页查询出用户信息
	 * @author: jiangcheng
	 *
	 * @param pageRequest
	 * @return
	 */
	public List<Map<String, Object>> getUsers(PageRequest pageRequest);

	/**
	 * 查询用户信息的记录数
	 * @author: jiangcheng
	 *
	 * @param getsearch
	 * @return
	 */
	public Integer getUserCount(String search);

	public List<Map<String, Object>> getUserById(String userId);

	public Integer updateUserRole(String[] userIds, String roleId);
}

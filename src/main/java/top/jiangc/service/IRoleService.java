package top.jiangc.service;

import java.util.List;
import java.util.Map;

import top.jiangc.entity.Role;

public interface IRoleService {

	/**
	 * 添加角色
	 *
	 * jiangc
	 * @param role
	 * @return
	 */
	public boolean addRole(Role role);

	/**
	 * 判断角色名是否存在(true 表示角色名已存在)
	 *
	 * jiangc
	 * @param roleName
	 * @return
	 */
	public boolean isExistRole(String roleName);

	/**
	 * 查询角色和角色所具有的权限
	 *
	 * jiangc
	 * @return
	 */
	public List<Map<String, Object>> getRolesAndPermission();

	/**
	 * 修改角色名及其权限
	 * @author: jiangcheng
	 *
	 * @param role
	 * @return
	 */
	public boolean updateRole(Role role);

	public List<Map<String, Object>> getResources();

	/**
	 * 根据角色id获取角色信息
	 * @author: jiangcheng
	 *
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> getRoleById(String roleId);

	/**
	 * 修改角色时判断是否存在其他角色名
	 * @author: jiangcheng
	 *
	 * @param roleId
	 * @param roleName
	 * @return
	 */
	public boolean isExistRole(Integer roleId, String roleName);

}
